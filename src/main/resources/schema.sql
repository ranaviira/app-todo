create table todo (todo_id bigint not null, description varchar(255), start_date smalldatetime, stop_date smalldatetime, status varchar(255), user_id bigint, parent_todo_id bigint, primary key (todo_id))

create table users (user_id bigint not null, full_name varchar(255), login varchar(255), password varchar(255), primary key (user_id))


alter table todo add constraint FKg1mdb2uha9v6t2ujkvlmj3tuq foreign key (todo_id) references todo

alter table users add constraint FKft8pmhndq1kntvyfaqcybhxvx foreign key (user_id) references users
