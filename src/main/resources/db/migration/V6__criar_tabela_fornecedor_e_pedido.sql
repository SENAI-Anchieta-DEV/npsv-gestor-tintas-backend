CREATE TABLE fornecedor (
                            id VARCHAR(36) PRIMARY KEY,
                            razao_social VARCHAR(255) NOT NULL,
                            cnpj VARCHAR(255) NOT NULL UNIQUE,
                            nome_contato VARCHAR(255) NOT NULL,
                            telefone VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL,
                            endereco VARCHAR(255) NOT NULL,
                            ativo BOOLEAN NOT NULL DEFAULT TRUE,
                            data_cadastro TIMESTAMP NOT NULL
);

CREATE TABLE pedido (
                        id VARCHAR(36) PRIMARY KEY,
                        fornecedor_id VARCHAR(36) NOT NULL,
                        CONSTRAINT fk_pedido_fornecedor FOREIGN KEY (fornecedor_id) REFERENCES fornecedor (id)
);