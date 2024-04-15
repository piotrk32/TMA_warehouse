CREATE SEQUENCE IF NOT EXISTS public.id_seq AS BIGINT START WITH 1000;
CREATE SEQUENCE IF NOT EXISTS request_row_id_seq INCREMENT BY 1 START WITH 1;

-- Drop the old requests table if it exists
DROP TABLE IF EXISTS requests;

-- Recreate the requests table without the request_row_id as this will be in the row_requests table
CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT PRIMARY KEY DEFAULT nextval('id_seq'),
                                        employee_id BIGINT NOT NULL,
                                        status VARCHAR(50),
                                        comment TEXT,
                                        price_without_vat NUMERIC(10, 2),
                                        created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        modified_at TIMESTAMP WITHOUT TIME ZONE,
                                        CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES public.users(id)
);

-- Create the row_requests table
CREATE TABLE IF NOT EXISTS rows_requests (
                                            id BIGINT PRIMARY KEY DEFAULT nextval('request_row_id_seq'),
                                            request_id BIGINT NOT NULL,
                                            item_id BIGINT NOT NULL,
                                            unit_of_measurement VARCHAR(255) NOT NULL,
                                            quantity NUMERIC(10, 2) NOT NULL,
                                            price_without_vat NUMERIC(10, 2) NOT NULL,
                                            comment TEXT,
                                            created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                            modified_at TIMESTAMP WITHOUT TIME ZONE,
                                            CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES public.requests(id),
                                            CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES public.items(id)
);