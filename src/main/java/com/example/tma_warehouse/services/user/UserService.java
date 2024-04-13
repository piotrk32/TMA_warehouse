package com.example.tma_warehouse.services.user;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

//    public void updateUserRoles(Long userId, List<String> roles) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        user.setRoles(roles);  // Assuming you have a setter for roles
//        userRepository.save(user);
//    }
}
