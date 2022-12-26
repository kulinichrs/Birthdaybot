create index idx_user_birthday on users (birthday);
create unique index uidx_team_user_t on team_user (team_id, user_id);
create unique index uidx_team_user_u on team_user (user_id, team_id);