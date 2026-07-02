CREATE TABLE tb_floors (

    id UUID PRIMARY KEY,

    tower_name VARCHAR(100) NOT NULL,

    floor_name VARCHAR(100) NOT NULL,

    slug VARCHAR(150) NOT NULL,

    layout TEXT,

    tb_business_uuid UUID NOT NULL,

    CONSTRAINT fk_floor_business
        FOREIGN KEY (tb_business_uuid)
        REFERENCES tb_business(uuid)
        ON DELETE CASCADE
);

CREATE INDEX idx_floor_business
ON tb_floors(tb_business_uuid);

CREATE UNIQUE INDEX idx_floor_slug
ON tb_floors(slug);
