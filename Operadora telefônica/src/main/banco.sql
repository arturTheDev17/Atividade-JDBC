create database if not exists db_operadora;
use db_operadora;

# select * from tb_plano;

create table if not exists tb_plano (
    id int primary key auto_increment not null,
    operadora varchar(255) not null,
    nome varchar(255) not null,
    quantidade_dados double not null,
    quantidade_dados_bonus double null,
    beneficios varchar(255) not null,
    valor double not null
);

create table if not exists tb_cliente (
    id int primary key auto_increment not null,
    nome varchar(255) not null,
    email varchar(255) not null,
    telefone varchar(63) not null,
    id_plano int not null,
    foreign key ( id_plano ) references tb_plano (id) on delete cascade on update cascade
);

create table if not exists tb_contrato (
    id int not null,
    termos varchar(255) not null,
    data_inicio date not null,
    data_fim date not null,
    foreign key ( id ) references tb_plano (id) on delete cascade on update cascade
);

create table if not exists tb_servico (
    id int primary key auto_increment not null,
    descricao varchar(255) not null,
    custo_mensal double not null
);

create table if not exists tb_planoServico (
    id_plano int not null,
    id_servico int not null,
    foreign key ( id_plano ) references tb_plano (id) on delete cascade on update cascade,
    foreign key ( id_servico ) references tb_servico (id) on delete cascade on update cascade,
    primary key ( id_plano , id_servico )

);

SELECT * FROM tb_plano
    LEFT JOIN db_operadora.tb_contrato tc
        on tb_plano.id = tc.id WHERE nome = 'pre-pago';