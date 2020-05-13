create schema stonks

create table stonks.user
(
id bigserial not null primary key,
nome character varying(150),
email character varying(100),
senha character varying(20),
saldo numeric(16,2)
)

select * from stonks.user
select * from stonks.papel

insert into stonks.user (nome, email, senha, saldo) values('user', 'email', 123, 0)
insert into stonks.papel (nome, valor, quantidade, user_id) values('papel1', 10, 10, 8)

select * from stonks.papel where papel.nome = 'papel1' and papel.user_id = 8

create table stonks.papel
(
id bigserial not null primary key,
nome character varying(150),
valor numeric(8,2),
quantidade integer,
user_id bigint references stonks.user (id)
)

drop table stonks.papel