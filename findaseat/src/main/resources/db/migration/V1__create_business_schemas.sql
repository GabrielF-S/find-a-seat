CREATE TABLE tb_business (
    uuid UUID PRIMARY KEY,

    business_name VARCHAR(150) NOT NULL,

    country VARCHAR(100),
    city VARCHAR(100),
    address VARCHAR(255),
    postal_code VARCHAR(20),

    business_type VARCHAR(30) NOT NULL
);

CREATE UNIQUE INDEX idx_business_name
ON tb_business(business_name);