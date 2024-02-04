CREATE TABLE auth(
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(100),
    email VARCHAR(100),
    token VARCHAR(255) NOT NULL,
    token_exp BIGINT,
    role SMALLINT NOT NULL,
    status SMALLINT NOT NULL,
    PRIMARY KEY (username),
    UNIQUE (token)
) ENGINE InnoDB;

CREATE TABLE user(
    username VARCHAR(100) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    photo_identitas VARCHAR(255),
    photo VARCHAR(255),
    identitas VARCHAR(100),
    PRIMARY KEY (username)
) ENGINE InnoDB;

CREATE TABLE address(
    id Integer(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    prov_id SMALLINT(100) NOT NULL,
    city_id SMALLINT(100) NOT NULL,
    dis_id SMALLINT(100) NOT NULL,
    subdis_id SMALLINT(100) NOT NULL,
    detail VARCHAR(100),
    status VARCHAR(100),
    PRIMARY KEY (id)
) ENGINE InnoDB;
