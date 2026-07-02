CREATE TABLE tb_users (

    id UUID PRIMARY KEY,

    email VARCHAR(200) NOT NULL,

    password VARCHAR(255) NOT NULL,
    roles TEXT[],
    tb_employees_id BIGINT UNIQUE,

    CONSTRAINT fk_user_employee
        FOREIGN KEY (tb_employees_id)
        REFERENCES tb_employees(id)
);

CREATE UNIQUE INDEX idx_user_email
ON tb_users(email);