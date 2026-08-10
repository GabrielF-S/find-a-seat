CREATE TABLE tb_waitlist (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGSERIAL,
    duration INTERVAL,
    reservation_status VARCHAR(30),
    reservation_day DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP

)
