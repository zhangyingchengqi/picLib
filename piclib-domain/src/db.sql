use testmis81;

create table pic
(
    id          int primary key auto_increment,
    path        varchar(2000),
    description varchar(2000)
);

-- path :   /xxxxx.jpg   ->   dfs://xxxxx:8888/xxx.jpg