use jogging;

create table users (
 id int not null AUTO_INCREMENT,
 name varchar(255) not null,
 password varchar(255) not null,
 role varchar(10) not null,
 PRIMARY KEY (ID)
);

insert into users (ID, name, password, role)
values (1, 'admin', 'admin', 'ADMIN');

insert into users (ID, name, password, role)
values (2, 'manager', 'manager', 'MANAGER');

insert into users (ID, name, password, role)
values (3, 'user', 'user', 'USER');
