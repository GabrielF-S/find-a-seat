CREATE TABLE tb_reservation (

    id UUID PRIMARY KEY,

    reservation_day DATE NOT NULL,

    start_time_location TIME NOT NULL,

    end_time_location TIME NOT NULL,

    seat_id UUID NOT NULL,

    tb_employees_id BIGINT NOT NULL,

    active BOOLEAN NOT NULL,

    CONSTRAINT fk_reservation_seat
        FOREIGN KEY (seat_id)
        REFERENCES tb_seats(id),

    CONSTRAINT fk_reservation_employee
        FOREIGN KEY (tb_employees_id)
        REFERENCES tb_employees(id)
);

CREATE INDEX idx_reservation_employee
ON tb_reservation(tb_employees_id);

CREATE INDEX idx_reservation_seat
ON tb_reservation(seat_id);

CREATE INDEX idx_reservation_day
ON tb_reservation(reservation_day);

CREATE UNIQUE INDEX idx_unique_reservation
ON tb_reservation (
    seat_id,
    reservation_day,
    start_time_location,
    end_time_location
);