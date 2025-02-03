insert into user_details(id,birth_date,name)
values(10001, current_date(),'Cristina');

insert into user_details(id,birth_date,name)
values(10002, current_date(),'Test');

insert into user_details(id,birth_date,name)
values(10003, current_date(),'Cris');

insert into post(id,description,user_id)
values(2001,'My first post', 10001);

insert into post(id,description,user_id)
values(2002,'My second post', 10002);

insert into post(id,description,user_id)
values(2003,'My 3th post', 10002);