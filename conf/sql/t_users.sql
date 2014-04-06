# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table t_comments (
  cmts_id                   bigint auto_increment not null,
  cmts_post                 bigint not null,
  cmts_author               bigint not null,
  cmts_content              varchar(512),
  constraint pk_t_comments primary key (cmts_id))
;

create table t_posts (
  posts_id                  bigint auto_increment not null,
  posts_author              bigint not null,
  posts_content             varchar(512),
  posts_time                datetime,
  posts_pics                varchar(255),
  constraint pk_t_posts primary key (posts_id))
;

create table t_relationship (
  rs_id                     bigint auto_increment not null,
  rs_fromuser               bigint not null,
  rs_touser                 bigint not null,
  rs_makedate               datetime not null,
  constraint pk_t_relationship primary key (rs_id))
;

create table t_users (
  users_id                  bigint auto_increment not null,
  users_username            varchar(255) not null,
  users_email               varchar(255) not null,
  users_password            varchar(255),
  users_salt                varchar(255),
  users_gender              varchar(6),
  users_address             varchar(255),
  users_birthday            datetime,
  users_description         varchar(50),
  users_avatar              varchar(255),
  users_website             varchar(255),
  users_signup              datetime not null,
  users_lastlogin           datetime not null,
  users_notescheck          datetime not null,
  users_activated           tinyint(1) default 0 not null,
  constraint ck_t_users_users_gender check (users_gender in ('female','other','male')),
  constraint uq_t_users_users_username unique (users_username),
  constraint uq_t_users_users_email unique (users_email),
  constraint pk_t_users primary key (users_id))
;

alter table t_comments add constraint fk_t_comments_post_1 foreign key (cmts_post) references t_posts (posts_id) on delete restrict on update restrict;
create index ix_t_comments_post_1 on t_comments (cmts_post);
alter table t_comments add constraint fk_t_comments_author_2 foreign key (cmts_author) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_comments_author_2 on t_comments (cmts_author);
alter table t_posts add constraint fk_t_posts_author_3 foreign key (posts_author) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_posts_author_3 on t_posts (posts_author);
alter table t_relationship add constraint fk_t_relationship_fromUser_4 foreign key (rs_fromuser) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_relationship_fromUser_4 on t_relationship (rs_fromuser);
alter table t_relationship add constraint fk_t_relationship_toUser_5 foreign key (rs_touser) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_relationship_toUser_5 on t_relationship (rs_touser);


ALTER TABLE t_users AUTO_INCREMENT = 20146701;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table t_comments;

drop table t_posts;

drop table t_relationship;

drop table t_users;

SET FOREIGN_KEY_CHECKS=1;
