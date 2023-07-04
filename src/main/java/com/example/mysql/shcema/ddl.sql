CREATE TABLE user (
                        id BIGINT AUTO_INCREMENT,
                        email VARCHAR(20) NOT NULL,
                        nickname VARCHAR(20) NOT NULL,
                        birthday DATE NOT NULL,
                        createdAt DATETIME NOT NULL,
                        PRIMARY KEY (id)
);