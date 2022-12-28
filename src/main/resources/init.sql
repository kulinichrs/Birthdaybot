create table team (
       team_id serial,
       default_text varchar(1024),
       description varchar(255),
       name varchar(255) unique,
       primary key (team_id)
    );

create table users (
       user_id bigint not null,
       birthday date,
       fio varchar(512),
       primary key (user_id)
    );

create table team_user (
       team_id serial not null,
       user_id bigint not null,
       foreign key (user_id) references users,
       foreign key (team_id) references team
    );
