CREATE TABLE tb_seats (

    id UUID PRIMARY KEY,

    seat_name VARCHAR(100) NOT NULL,

    nick VARCHAR(50),

    slug VARCHAR(100) NOT NULL,

    status VARCHAR(20) NOT NULL,

    exclusive BOOLEAN NOT NULL,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    seats_quantity BIGINT,

    type VARCHAR(30) NOT NULL,

    tb_floors_id UUID NOT NULL,

    CONSTRAINT fk_seat_floor
        FOREIGN KEY (tb_floors_id)
        REFERENCES tb_floors(id)
        ON DELETE CASCADE
);
CREATE UNIQUE INDEX idx_seat_slug
ON tb_seats(slug);