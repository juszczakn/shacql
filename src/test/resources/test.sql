--name: create-table-test!
create table theBestTable (id int not null)

--name: insert!
insert into test (id, name) values (2, 'Bobby Tables')

-- name: select-everything
select id, name from test
