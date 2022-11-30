CREATE TABLE hero
(
    id    IDENTITY NOT NULL PRIMARY KEY,
    name  VARCHAR  NOT NULL
);

CREATE TABLE users
(
    id    IDENTITY NOT NULL PRIMARY KEY,
    username  VARCHAR(20)  NOT NULL,
    email  VARCHAR(50)  NOT NULL,
    password  VARCHAR(120)  NOT NULL
);

CREATE TABLE roles
(
    id    IDENTITY NOT NULL PRIMARY KEY,
    name  VARCHAR  NOT NULL
);

CREATE TABLE user_roles
(
    user_id  bigint NOT NULL,
    role_id  bigint NOT NULL
);

insert into hero (name) values ('batman');
insert into hero (name) values ('superman');

INSERT INTO roles(name) VALUES('USER');
INSERT INTO roles(name) VALUES('ADMIN');

INSERT INTO users(id, username, email, password) values (1, 'admin', 'admin@admin.com', '$2a$12$vKnFwdNZlrafCd.a4oxp6eTkJR9d6YoM8b6S1XgV7KgE7grdSyode');
INSERT INTO users(id, username, email, password) values (2, 'user', 'user@user.com', '$2a$12$rOae7ZE612BUUzgeVtXcUOwm2fsvfwx5JxBHbFz6CoZ57n5cYkp7q');

INSERT INTO user_roles(user_id,role_id) values (1,1);
INSERT INTO user_roles(user_id,role_id) values (1,2);
INSERT INTO user_roles(user_id,role_id) values (2,1);
