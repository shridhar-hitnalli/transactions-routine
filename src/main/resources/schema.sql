drop table if exists `transaction`;
drop table if exists `account`;
drop table if exists `operation_type`;

create table `operation_type`
(
    id                    bigint(20) not null auto_increment,
    description           varchar(100) not null,
    primary key (id)
);

create table `account`
(
    id                    bigint(20) not null auto_increment,
    document_number       varchar(100) not null unique,
    primary key (id)
);

create table `transaction`
(
    id                    bigint(20) not null auto_increment,
    account_id            bigint(20) references account(id) on delete cascade,
    operation_type_id     bigint(20) references operation_type(id) on delete cascade,
    amount                numeric,
    event_date            datetime NOT NULL,
    primary key (id)
);
