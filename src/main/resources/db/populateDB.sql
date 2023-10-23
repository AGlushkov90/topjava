DELETE
FROM user_role;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2023-10-30 10:00:00', 'Завтрак юзер', 500),
       (100000, '2023-10-30 13:00:00', 'Обед юзер', 1000),
       (100000, '2023-10-30 20:00:00', 'Ужин юзер', 500),
       (100000, '2023-10-31 00:00:00', 'Еда на граничное значение юзер', 100),
       (100000, '2023-10-31 10:00:00', 'Завтрак юзер', 1000),
       (100000, '2023-10-31 13:00:00', 'Обед юзер', 500),
       (100000, '2023-10-31 20:00:00', 'Ужин юзер', 410),
       (100001, '2023-10-29 10:00:00', 'Завтрак админ', 500),
       (100001, '2023-10-31 12:00:00', 'ужин админ', 700);
