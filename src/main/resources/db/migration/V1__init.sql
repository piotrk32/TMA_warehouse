CREATE SEQUENCE IF NOT EXISTS public.id_seq AS BIGINT START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS item_row_id_seq INCREMENT BY 1 START WITH 1;

-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('id_seq'),
                                     first_name VARCHAR(255),
                                     last_name VARCHAR(255),
                                     birth_date DATE,
                                     email VARCHAR(255) UNIQUE,
                                     phone_number VARCHAR(20),
                                     access_token TEXT,
                                     refresh_token TEXT,
                                     id_token TEXT,
                                     status VARCHAR(50) DEFAULT 'REGISTRATION_INCOMPLETE',
                                     created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                     modified_at TIMESTAMP WITHOUT TIME ZONE
);


CREATE TABLE IF NOT EXISTS employees (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('id_seq')
);


CREATE TABLE IF NOT EXISTS coordinators (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('id_seq')
);

-- Items table
CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
                                     item_name VARCHAR(255),
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

-- Requests table
CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
                                        request_row_id BIGINT NOT NULL DEFAULT nextval('item_row_id_seq'),
                                        employee_id BIGINT NOT NULL,
                                        item_id BIGINT NOT NULL,
                                        unit_of_measurement VARCHAR(255) NOT NULL,
                                        quantity NUMERIC(10, 2) NOT NULL,
                                        price_without_vat NUMERIC(10, 2) NOT NULL,
                                        comment TEXT,
                                        status VARCHAR(50),
                                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        modified_at TIMESTAMP WITHOUT TIME ZONE,
                                        CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES public.users(id),
                                        CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES public.items(id)
);
);
