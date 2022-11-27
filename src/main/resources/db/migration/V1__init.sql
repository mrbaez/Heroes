CREATE TABLE hero
(
    id    IDENTITY NOT NULL PRIMARY KEY,
    name  VARCHAR  NOT NULL
);


insert into hero (name) values ('batman');
insert into hero (name) values ('superman');
