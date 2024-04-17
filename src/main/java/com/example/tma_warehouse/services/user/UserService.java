package com.example.tma_warehouse.services.user;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.administrator.Administrator;
import com.example.tma_warehouse.models.coordinator.Coordinator;
import com.example.tma_warehouse.models.employee.Employee;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.repositories.*;
import com.example.tma_warehouse.security.services.CustomUserDetailsService;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final AdministratorRepository administratorRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserDetailsService userDetailsService;
    private final RequestRepository requestRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public User getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("User", "No user found with email: " + email);
        }
        return userOptional.get();
    }

    public String getAccessTokenByEmail(String email) {
        return userRepository.findAccessTokenByEmail(email);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public void setTokens(String email, GoogleTokenResponse tokens) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setAccessToken(tokens.getAccessToken());
            user.setRefreshToken(tokens.getRefreshToken());
            user.setIdToken(tokens.getIdToken());
            userRepository.saveAndFlush(user);
        } else {
            User user = new User(email, tokens.getAccessToken(), tokens.getRefreshToken(), tokens.getIdToken());
            userRepository.saveAndFlush(user);
        }
    }

    public void removeTokens(String email) {
        User user = getUserByEmail(email);
        user.setAccessToken(null);
        user.setRefreshToken(null);
        user.setIdToken(null);
        userRepository.saveAndFlush(user);
    }


    public String getUserEmailByUserId(Long userId) {
        return userRepository.findUserEmailByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "No user found with id: " + userId));
    }

    public Long getUserIdByUserEmail(String email) {
        return userRepository.getUserIdByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", "No user found with email: " + email));
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User createUserWithRole(User user, List<String> roles) {
        user.setRoles(roles);
        return userRepository.save(user); // Assuming userRepository handles User entities.
    }

    @Transactional
    public User updateUserRoles(Long userId, List<String> newRoles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        System.out.println("Before updating roles: " + user.getRoles());

        user.getRoles().clear();
        user.getRoles().addAll(newRoles.stream().map(String::toUpperCase).collect(Collectors.toSet()));

        // Transition user type if necessary
        List<Request> requests =  requestRepository.findAllByEmployeeId(user.getId());
        user = transitionUserTypeBasedOnRoles(user, newRoles);

        for (Request request : requests){
            request.setEmployee((Employee) user);
        }
        requestRepository.saveAll(requests);


        user = userRepository.save(user);  // Make sure the user is saved after changes
        entityManager.flush();  // Ensure that changes are flushed immediately

        System.out.println("After updating roles: " + user.getRoles());
        return user;
    }
    private User transitionUserTypeBasedOnRoles(User user, List<String> newRoles) {
        if (newRoles.contains("COORDINATOR") && !(user instanceof Coordinator)) {
            return transitionToNewUserType(user, Coordinator.class);
        } else if (newRoles.contains("EMPLOYEE") && !(user instanceof Employee)) {
            return transitionToNewUserType(user, Employee.class);
        }
        // Handle other transitions similarly
        return user;
    }
    private <T extends User> T transitionToNewUserType(User oldUser, Class<T> newType) {
        // Remove the old user from the database
        userRepository.delete(oldUser);
        entityManager.flush();  // Flush to ensure delete is executed immediately

        // Create a new instance of the target type
        T newUser = newType.cast(createNewUserOfType(oldUser, newType));
        return userRepository.save(newUser);  // Persist the new user type and return it
    }
    private <T extends User> T createNewUserOfType(User user, Class<T> newTypeClass) {
        try {
            T newUser = newTypeClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(user, newUser, "id", "class");
            newUser.setId(user.getId());  // Maintain the same ID if your ID strategy allows
            return newUser;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate user type " + newTypeClass.getSimpleName(), e);
        }
    }

    private User updateOrTransitionUserType(User user, List<String> newRoles) {
        Class<? extends User> targetType = determineTargetType(newRoles);
        if (!targetType.isInstance(user)) {
            return transitionUserToNewType(user, targetType);
        }
        return user;
    }

    private Class<? extends User> determineTargetType(List<String> roles) {
        if (roles.contains("ADMINISTRATOR")) return Administrator.class;
        if (roles.contains("COORDINATOR")) return Coordinator.class;
        if (roles.contains("EMPLOYEE")) return Employee.class;
        return User.class;  // Fallback to base User class
    }

    private <T extends User> T transitionUserToNewType(User user, Class<T> newTypeClass) {
        entityManager.remove(user);  // Detach and remove the current instance from persistence context
        entityManager.flush();  // Flush to execute removal immediately

        T newUser;
        try {
            newUser = newTypeClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(user, newUser, "id");
            newUser.setId(user.getId());  // Set the same ID to maintain identity
            entityManager.persist(newUser);  // Persist the new type instance
            entityManager.flush();  // Ensure it's immediately saved
        } catch (Exception e) {
            throw new RuntimeException("Failed to transition user type", e);
        }
        return newUser;
    }

    private void refreshAuthentication(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
