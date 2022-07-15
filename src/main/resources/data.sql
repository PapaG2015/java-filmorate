INSERT INTO genres (description)
VALUES ('Комедия')
ON CONFLICT DO NOTHING;

INSERT INTO genres (description)
VALUES ('Драма')
ON CONFLICT DO NOTHING;

INSERT INTO genres (description)
VALUES ('Мультфильм')
ON CONFLICT DO NOTHING;

INSERT INTO genres (description)
VALUES ('Триллер')
ON CONFLICT DO NOTHING;

INSERT INTO genres (description)
VALUES ('Документальный')
ON CONFLICT DO NOTHING;

INSERT INTO genres (description)
VALUES ('Боевик')
ON CONFLICT DO NOTHING;

INSERT INTO ratings (mpa, description)
VALUES ('G', 'у фильма нет возрастных ограничений')
ON CONFLICT DO NOTHING;

INSERT INTO ratings (mpa, description)
VALUES ('PG', 'детям рекомендуется смотреть фильм с родителями')
ON CONFLICT DO NOTHING;

INSERT INTO ratings (mpa, description)
VALUES ('PG-13', 'детям до 13 лет просмотр не желателен')
ON CONFLICT DO NOTHING;

INSERT INTO ratings (mpa, description)
VALUES ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого')
ON CONFLICT DO NOTHING;

INSERT INTO ratings (mpa, description)
VALUES ('NC-17', 'лицам до 18 лет просмотр запрещён')
ON CONFLICT DO NOTHING;

INSERT INTO friendship_status (status)
VALUES ('true')
ON CONFLICT DO NOTHING;

INSERT INTO friendship_status (status)
VALUES ('false')
ON CONFLICT DO NOTHING;