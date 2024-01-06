CREATE TABLE users (
 id bigint NOT NULL AUTO_INCREMENT,
 username varchar(25),
 password varchar(255),
 name varchar(25),
 surname varchar(25),
 email varchar(50),
 PRIMARY KEY (id)
);

CREATE TABLE roles (
 id bigint NOT NULL AUTO_INCREMENT,
 name varchar(25),
 primary key (id)
);

CREATE TABLE users_roles (

 user_id bigint NOT NULL,
 role_id bigint NOT NULL,
 primary key (user_id, role_id),
 foreign key (user_id) references users (id),
 foreign key (role_id) references roles (id)
);

insert into roles (name)
values
    ('ROLE_ADMIN'), ('ROLE_USER');

insert into users (username, password, name, surname, email)
values
    ('admin', '$2a$12$hKIMlaiTYct7tC1.J63awOmFKVO9/FH8vCrylwYXBDi7nCp6ayq52', 'Aleksandr', 'Petrov', 'user@mail.ru');

insert into users_roles (user_id, role_id)
values
    (1, 1),
    (1, 2);