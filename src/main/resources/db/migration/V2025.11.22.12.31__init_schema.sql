CREATE TABLE IF NOT EXISTS author
(
    id          VARCHAR PRIMARY KEY,
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    created_at  timestamptz,
    updated_at  timestamptz
);

CREATE TABLE IF NOT EXISTS book
(
    id         VARCHAR PRIMARY KEY,
    isbn_code  VARCHAR unique NOT NULL,
    name       VARCHAR        NOT NULL,
    author_id  VARCHAR        NOT NULL REFERENCES author (id),
    created_at timestamptz,
    updated_at timestamptz
);

CREATE TABLE IF NOT EXISTS client
(
    id            VARCHAR PRIMARY KEY,
    first_name    VARCHAR NOT NULL,
    middle_name   VARCHAR,
    last_name     VARCHAR NOT NULL,
    date_of_birth DATE    NOT NULL,
    created_at    timestamptz,
    updated_at    timestamptz
);

CREATE TABLE IF NOT EXISTS booking
(
    id         VARCHAR PRIMARY KEY,
    book_id    VARCHAR NOT NULL REFERENCES book (id),
    client_id  VARCHAR NOT NULL REFERENCES client (id),
    created_at timestamptz,
    updated_at timestamptz
);

