ALTER TABLE tb_waitlist
    ADD COLUMN business_uuid UUID;

ALTER TABLE tb_waitlist
    ADD CONSTRAINT fk_waitlist_employee
        FOREIGN KEY (employee_id)
        REFERENCES tb_employees(id);

ALTER TABLE tb_waitlist
    ADD CONSTRAINT fk_waitlist_business
        FOREIGN KEY (business_uuid)
        REFERENCES tb_business(uuid)
        ON DELETE CASCADE;
ALTER TABLE tb_waitlist
    ALTER COLUMN employee_id TYPE BIGINT;