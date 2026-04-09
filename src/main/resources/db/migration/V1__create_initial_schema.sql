CREATE TABLE usuario
(
    id    VARCHAR(36) PRIMARY KEY,
    nome  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN      NOT NULL,
    role  VARCHAR(50)  NOT NULL
);

CREATE TABLE categoria_produto
(
    id        VARCHAR(36) PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL
);

CREATE TABLE produto
(
    id                 VARCHAR(36) PRIMARY KEY,
    codigo_barras      VARCHAR(255)   NOT NULL UNIQUE,
    descricao          VARCHAR(255)   NOT NULL,
    quantidade_estoque NUMERIC(19, 2) NOT NULL,
    preco_custo        NUMERIC(19, 2) NOT NULL,
    preco_venda        NUMERIC(19, 2) NOT NULL,
    unidade_medida     VARCHAR(50)    NOT NULL,
    categoria_id       VARCHAR(36),
    CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_produto (id)
);

CREATE TABLE formula
(
    id               VARCHAR(36) PRIMARY KEY,
    codigo_interno   VARCHAR(255) NOT NULL UNIQUE,
    nome_cor         VARCHAR(255) NOT NULL,
    data_criacao     TIMESTAMP    NOT NULL,
    data_atualizacao TIMESTAMP    NOT NULL
);

CREATE TABLE item_formula
(
    id                    VARCHAR(36) PRIMARY KEY,
    quantidade_necessaria NUMERIC(19, 4) NOT NULL,
    ordem_adicao          INTEGER        NOT NULL,
    formula_id            VARCHAR(36)    NOT NULL,
    insumo_id             VARCHAR(36),
    CONSTRAINT fk_item_formula_formula FOREIGN KEY (formula_id) REFERENCES formula (id),
    CONSTRAINT fk_item_formula_insumo FOREIGN KEY (insumo_id) REFERENCES produto (id)
);

CREATE TABLE producao
(
    id           VARCHAR(36) PRIMARY KEY,
    data_hora    TIMESTAMP   NOT NULL,
    status       VARCHAR(50) NOT NULL,
    colorista_id VARCHAR(36),
    formula_id   VARCHAR(36),
    CONSTRAINT fk_producao_colorista FOREIGN KEY (colorista_id) REFERENCES usuario (id),
    CONSTRAINT fk_producao_formula FOREIGN KEY (formula_id) REFERENCES formula (id)
);

CREATE TABLE pesagem_evento
(
    id           VARCHAR(36) PRIMARY KEY,
    peso_lido    NUMERIC(19, 4) NOT NULL,
    timestamp    TIMESTAMP      NOT NULL,
    foi_aprovado BOOLEAN        NOT NULL,
    producao_id  VARCHAR(36),
    CONSTRAINT fk_pesagem_producao FOREIGN KEY (producao_id) REFERENCES producao (id)
);

CREATE TABLE venda
(
    id              VARCHAR(36) PRIMARY KEY,
    status          VARCHAR(50)    NOT NULL,
    data_abertura   TIMESTAMP      NOT NULL,
    data_fechamento TIMESTAMP,
    forma_pagamento VARCHAR(50),
    valor_total     NUMERIC(19, 2) NOT NULL,
    vendedor_id     VARCHAR(36),
    CONSTRAINT fk_venda_vendedor FOREIGN KEY (vendedor_id) REFERENCES usuario (id)
);

CREATE TABLE item_venda
(
    id              VARCHAR(36) PRIMARY KEY,
    quantidade      NUMERIC(19, 4) NOT NULL,
    preco_praticado NUMERIC(19, 2) NOT NULL,
    produto_id      VARCHAR(36),
    venda_id        VARCHAR(36),
    CONSTRAINT fk_item_venda_produto FOREIGN KEY (produto_id) REFERENCES produto (id),
    CONSTRAINT fk_item_venda_venda FOREIGN KEY (venda_id) REFERENCES venda (id)
);