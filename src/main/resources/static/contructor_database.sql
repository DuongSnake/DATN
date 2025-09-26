CREATE TABLE users
(
    id bigint IDENTITY(1,1) NOT NULL,
    password character varying(120) NOT NULL,
    username character varying(120) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id bigint IDENTITY(1,1) NOT NULL,
    name character varying(20),
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);
CREATE TABLE user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

INSERT INTO roles (name) VALUES('ROLE_USER');
INSERT INTO roles (name) VALUES('ROLE_MODERATOR');
INSERT INTO roles (name) VALUES('ROLE_ADMIN');