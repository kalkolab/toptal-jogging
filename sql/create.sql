use jogging;

create table users (
 id int not null AUTO_INCREMENT,
 name varchar(255) not null,
 password varchar(255) not null,
 role varchar(10) not null,
 PRIMARY KEY (ID)
);

create table runs (
 id int not null AUTO_INCREMENT,
 userid int not null,
 duration long not null,
 distance float not null,
 'date' DATE not null,
 latitude float,
 longitude float,
 weather VARCHAR(1024),
 PRIMARY KEY (ID),
  FOREIGN KEY (userid) REFERENCES users(id)
   ON DELETE CASCADE
   ON UPDATE CASCADE
);
