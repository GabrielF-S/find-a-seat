CREATE TABLE tb_employees (

    id BIGSERIAL PRIMARY KEY,

    employee_name VARCHAR(120) NOT NULL,

    department VARCHAR(100),

    document VARCHAR(30) NOT NULL,

    tb_business_uuid UUID NOT NULL,

    type VARCHAR(20) NOT NULL,

    CONSTRAINT fk_employee_business
        FOREIGN KEY (tb_business_uuid)
        REFERENCES tb_business(uuid)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_employee_document
ON tb_employees(document);

CREATE INDEX idx_employee_business
ON tb_employees(tb_business_uuid);