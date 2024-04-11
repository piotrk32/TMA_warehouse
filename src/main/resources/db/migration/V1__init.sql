--DB initialization
CREATE SEQUENCE IF NOT EXISTS id_seq INCREMENT BY 1 START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS item_row_id_seq INCREMENT BY 1 START WITH 1;


CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE
);
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    access_token TEXT,
    refresh_token TEXT,
    id_token TEXT,
    status VARCHAR(50) DEFAULT 'REGISTRATION_INCOMPLETE',
    role_id BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
CREATE TABLE IF NOT EXISTS employees (

                                         id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('id_seq') REFERENCES users(id),
                                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                         modified_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS coordinatores (

                                             id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('id_seq') REFERENCES users(id),
                                             created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                             modified_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
    item_group VARCHAR(255) NOT NULL,
    unit_of_measurement VARCHAR(255) NOT NULL,
    quantity NUMERIC(10, 2) NOT NULL,
    price_without_vat NUMERIC(10, 2) NOT NULL,
    status VARCHAR(50),
    storage_location VARCHAR(255),
    contact_person VARCHAR(255),
    photo VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_at TIMESTAMP WITHOUT TIME ZONE
);


CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
                                        request_row_id BIGINT NOT NULL DEFAULT nextval('item_row_id_seq'),
                                        employee_id BIGINT NOT NULL,
                                        item_id BIGINT UNIQUE NOT NULL,
                                        unit_of_measurement VARCHAR(255) NOT NULL,
                                        quantity NUMERIC(10, 2) NOT NULL,
                                        price_without_vat NUMERIC(10, 2) NOT NULL,
                                        comment TEXT,
                                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        modified_at TIMESTAMP WITHOUT TIME ZONE,
                                        FOREIGN KEY (employee_id) REFERENCES users (id),
                                        FOREIGN KEY (item_id) REFERENCES items (id)
);




