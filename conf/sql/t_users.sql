# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table t_relationship (
  rs_id                     bigint auto_increment not null,
  rs_fromuser               bigint not null,
  rs_touser                 bigint not null,
  rs_makedate               datetime not null,
  rs_accepted               tinyint(1) default 0 not null,
  constraint pk_t_relationship primary key (rs_id))
;

create table t_users (
  users_id                  bigint auto_increment not null,
  users_username            varchar(255) not null,
  users_email               varchar(255) not null,
  users_password            varchar(255),
  users_salt                varchar(255),
  users_gender              varchar(6),
  users_avatar              varchar(255),
  users_website             varchar(255),
  users_signup              datetime not null,
  users_lastlogin           datetime not null,
  users_notescheck          datetime not null,
  user_activated            tinyint(1) default 0 not null,
  constraint ck_t_users_users_gender check (users_gender in ('famale','other','male')),
  constraint uq_t_users_users_username unique (users_username),
  constraint uq_t_users_users_email unique (users_email),
  constraint pk_t_users primary key (users_id))
;

alter table t_relationship add constraint fk_t_relationship_fromUser_1 foreign key (rs_fromuser) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_relationship_fromUser_1 on t_relationship (rs_fromuser);
alter table t_relationship add constraint fk_t_relationship_toUser_2 foreign key (rs_touser) references t_users (users_id) on delete restrict on update restrict;
create index ix_t_relationship_toUser_2 on t_relationship (rs_touser);

ALTER TABLE t_users AUTO_INCREMENT = 20146701;

INSERT INTO `t_users` (`users_id`,`users_username`,`users_email`,`users_password`,`users_salt`,`users_gender`,`users_avatar`,`users_website`,`users_signup`,`users_lastlogin`,`users_notescheck`,`user_activated`) VALUES (20146701,'Loong_T','talentloong@163.com','f96697f479435ec0b5c830793a6a029b8594e8a4b8dcebdbf04f62339bd0cf91','4367cf59-652d-4b41-9814-858879f1effb',NULL,NULL,NULL,'2014-03-25 19:09:07','2014-04-01 15:52:38','2014-03-25 19:09:07',1);
INSERT INTO `t_users` (`users_id`,`users_username`,`users_email`,`users_password`,`users_salt`,`users_gender`,`users_avatar`,`users_website`,`users_signup`,`users_lastlogin`,`users_notescheck`,`user_activated`) VALUES (20146702,'zheng','talentloong@gmail.com','3b16cfadd318f966ef8e7c3dc58395ac89766c9fbdc2273db357813cefa9058f','59a2a1c6-f2be-4cd2-8159-0eb09c0252fe',NULL,NULL,NULL,'2014-03-25 20:34:08','2014-03-28 16:37:34','2014-03-25 20:34:08',1);
INSERT INTO `t_users` (`users_id`,`users_username`,`users_email`,`users_password`,`users_salt`,`users_gender`,`users_avatar`,`users_website`,`users_signup`,`users_lastlogin`,`users_notescheck`,`user_activated`) VALUES (20146703,'Xuqiang','274660603@51uc.com','841806fbe05ca38de997ebd483b0719651b7f936a173f75df1722519354270a9','230df7cf-9929-4735-97bd-d8410645e044',2,NULL,NULL,'2014-04-01 15:48:30','2014-04-01 15:48:30','2014-04-01 15:48:30',1);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table t_relationship;

drop table t_users;

SET FOREIGN_KEY_CHECKS=1;
