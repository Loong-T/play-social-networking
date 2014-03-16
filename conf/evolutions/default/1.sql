# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table t_users (
  users_id                  bigint auto_increment not null,
  users_username            varchar(255) not null,
  users_email               varchar(255) not null,
  users_password            varchar(255),
  users_gender              integer,
  users_avatar              varchar(255),
  users_website             varchar(255),
  users_signup              datetime not null,
  users_lastlogin           datetime not null,
  users_notescheck          datetime not null,
  user_activated            tinyint(1) default 0 not null,
  constraint ck_t_users_users_gender check (users_gender in (0,1,2)),
  constraint uq_t_users_users_username unique (users_username),
  constraint uq_t_users_users_email unique (users_email),
  constraint pk_t_users primary key (users_id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table t_users;

SET FOREIGN_KEY_CHECKS=1;

