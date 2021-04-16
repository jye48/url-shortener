DROP TABLE IF EXISTS url_map;

CREATE TABLE url_map
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    shortened_link    VARCHAR(16)   NOT NULL,
    original_url      VARCHAR(2048) NOT NULL,
    created_timestamp TIMESTAMP     NOT NULL
);