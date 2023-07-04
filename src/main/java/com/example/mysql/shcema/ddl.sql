CREATE TABLE user (
                        id BIGINT AUTO_INCREMENT,
                        email VARCHAR(20) NOT NULL,
                        nickname VARCHAR(20) NOT NULL,
                        birthday DATE NOT NULL,
                        createdAt DATETIME NOT NULL,
                        PRIMARY KEY (id)
);

create table UserNicknameHistory
(
    id int auto_increment,
    memberId int not null,
    nickname varchar(20) not null,
    createdAt datetime not null,
        primary key (id)
);

create table Follow
(
    id int auto_increment,
    fromMemberId int not null,
    toMemberId int not null,
    createdAt datetime not null,

        primary key (id)
);

create unique index Follow_fromMemberId_toMemberId_uindex
    on Follow (fromMemberId, toMemberId);