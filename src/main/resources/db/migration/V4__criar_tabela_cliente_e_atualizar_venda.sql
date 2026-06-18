CREATE TABLE cliente (
                         id VARCHAR(255) PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         cpf VARCHAR(255) NOT NULL UNIQUE,
                         telefone VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         endereco VARCHAR(255) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         data_cadastro TIMESTAMP NOT NULL
);

ALTER TABLE venda
    ADD COLUMN cliente_id VARCHAR(255);

ALTER TABLE venda
    ADD CONSTRAINT fk_venda_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente(id);