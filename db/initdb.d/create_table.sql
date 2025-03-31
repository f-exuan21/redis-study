CREATE TABLE booking
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    showtime_id BIGINT NULL,
    seat_id     BIGINT NULL,
    created_by  VARCHAR(255) NULL,
    created_at  datetime NULL,
    updated_by  VARCHAR(255) NULL,
    updated_at  datetime NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE genre
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NULL,
    created_by VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_by VARCHAR(255) NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_genre PRIMARY KEY (id)
);

CREATE TABLE movie
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    title        VARCHAR(255) NULL,
    thumbnailurl VARCHAR(255) NULL,
    release_date date NULL,
    duration     INT NOT NULL,
    genre_id     BIGINT NULL,
    rating       VARCHAR(255) NULL,
    created_by   VARCHAR(255) NULL,
    created_at   datetime NULL,
    updated_by   VARCHAR(255) NULL,
    updated_at   datetime NULL,
    CONSTRAINT pk_movie PRIMARY KEY (id)
);

CREATE TABLE seat
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    theater_id BIGINT NOT NULL,
    name       VARCHAR(255) NULL,
    created_by VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_by VARCHAR(255) NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_seat PRIMARY KEY (id)
);

CREATE TABLE showtime
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    theater_id BIGINT NULL,
    movie_id   BIGINT NULL,
    start_time time NULL,
    end_time   time NULL,
    created_by VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_by VARCHAR(255) NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_showtime PRIMARY KEY (id)
);

CREATE TABLE theater
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NULL,
    created_by VARCHAR(255) NULL,
    created_at datetime NULL,
    updated_by VARCHAR(255) NULL,
    updated_at datetime NULL,
    CONSTRAINT pk_theater PRIMARY KEY (id)
);

ALTER TABLE booking ADD CONSTRAINT uq_showtime_seat UNIQUE (showtime_id, seat_id);