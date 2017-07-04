insert into users (ID, name, password, role)
values (1, 'admin', 'admin', 'ADMIN');

insert into users (ID, name, password, role)
values (2, 'manager', 'manager', 'MANAGER');

insert into users (ID, name, password, role)
values (3, 'user', 'user', 'USER');

insert into runs (userid, runtime, distance, rundate, lat, lon)
values (3, 2400, 5.0, '2017-07-04', 55.7558, 37.6173);