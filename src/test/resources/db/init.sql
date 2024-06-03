create table account
(
    account_id              bigint       not null auto_increment,
    version                 bigint       not null,
    email                   varchar(255) not null,
    password                varchar(255) not null,
    fullname                varchar(255) null,
    birth_date              date         null,
    enabled                 boolean      not null,
    account_non_expired     boolean      not null,
    account_non_locked      boolean      not null,
    credentials_non_expired boolean      not null,
    created_at              datetime(6)  not null,
    last_modified_at        datetime(6)  not null,
    profile_id              bigint       not null,
    primary key (account_id)
) engine = InnoDB;


alter table account
    add constraint uk_account__email unique (email);

create index idx_account__last_modified_at
    on account (last_modified_at desc);



create table account_roles
(
    account_id     bigint       not null,
    role           enum ('admin','user','manager') not null
) engine = InnoDB;



create table profile
(
    profile_id         bigint       not null auto_increment,
    version            bigint       not null,
    nickname           varchar(255) null,
    file_type          varchar(255) not null,
    image_source       varchar(255) not null,
    created_at         datetime(6)  not null,
    last_modified_at   datetime(6)  not null,
    account_id         bigint       null,
    primary key (profile_id)
) engine = InnoDB;



alter table account
    add constraint uk_account__profile unique (profile_id);

create index idx_profile__last_modified_at
    on profile (last_modified_at desc);


create table oauth2_account
(
    oauth2_account_id   bigint          not null auto_increment,
    version             bigint          not null,
    provider            varchar(255)    not null,
    provider_id          varchar(255)    not null,
    email               varchar(255)    not null,
    account_id          bigint          not null,
    primary key (oauth2_account_id)
) engine = InnoDB;



create table category
(
    category_id        bigint       not null auto_increment,
    category_order     int          not null,
    name               varchar(255) not null,
    visibility         boolean      not null,
    created_at         datetime(6)  not null,
    last_modified_at   datetime(6)  not null,
    profile_id         bigint       not null,
    account_id         bigint       not null,
    primary key (category_id)
) engine = InnoDB;



create index idx_category__profile_id_last_modified_at
    on category (profile_id, last_modified_at desc);


create table article
(
    article_id         bigint       not null auto_increment,
    version            bigint       not null,
    title              varchar(255) not null,
    body               text         not null,
    visibility         boolean      not null,
    created_at         datetime(6)  not null,
    last_modified_at   datetime(6)  not null,
    profile_id         bigint       not null,
    category_id        bigint       not null,
    primary key (article_id)
) engine = InnoDB;



create index idx_article__profile_id_last_modified_at
    on article (profile_id, last_modified_at desc);


create table article_tags
(
    article_id bigint       not null,
    tag_name   varchar(255) not null,
    primary key (article_id, tag_name)
) engine = InnoDB;






create table content
(
    content_id     bigint       not null auto_increment,
    version        bigint       not null,
    dtype          varchar(31)  not null,
    name           varchar(255) not null,
    published_date date         not null,    
    primary key (content_id)
) engine = InnoDB;



create table book
(
    content_id bigint not null,
    isbn       bigint not null,
    primary key (content_id)
) engine = InnoDB;


create table movie
(
    content_id bigint       not null,
    director   varchar(128) not null,
    genre      varchar(128),
    primary key (content_id)
) engine = InnoDB;



create table template
(
    template_id bigint       not null auto_increment,
    version     bigint       not null,
    name        varchar(255) not null,
    body        text         not null,
    visibility  boolean      not null,
    profile_id  bigint       not null,
    primary key (template_id)
) engine = InnoDB;

create table token_record
(
    token_record_id          bigint        not null auto_increment,
    username                 varchar(255)  not null,
    access_token_value       varchar(1000) null,
    refresh_token_value      varchar(1000) null,
    access_token_expires_at  datetime(6)   null,
    access_token_issued_at   datetime(6)   null,
    refresh_token_expires_at datetime(6)   null,
    refresh_token_issued_at  datetime(6)   null,
    primary key (token_record_id)
) engine = InnoDB;

