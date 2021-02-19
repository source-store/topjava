DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
    ('Admin2', 'admin2@gmail.com', 'admin2');

INSERT INTO users (id, name, email, password)
VALUES (100, 'AdminTest', 'adminTest@gmail.com', 'adminTest'),
       (200, 'UserTest', 'userTest@yandex.ru', 'passwordTest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100),
       ('USER', 200);
