--name: create-table!
create table test (id int not null)

--name: insert!
insert into test (id) values (1)

-- name: select-everything
select id from test
