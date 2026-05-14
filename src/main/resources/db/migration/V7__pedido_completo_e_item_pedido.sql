ALTER TABLE pedido
    ADD COLUMN admin_id              VARCHAR(36),
    ADD COLUMN status                VARCHAR(50)  NOT NULL DEFAULT 'PENDENTE',
    ADD COLUMN data_pedido           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN data_previsao_entrega DATE,
    ADD COLUMN observacao            TEXT,
    ADD CONSTRAINT fk_pedido_admin
        FOREIGN KEY (admin_id) REFERENCES usuario (id);

CREATE TABLE item_pedido
(
    id              VARCHAR(36)    PRIMARY KEY,
    pedido_id       VARCHAR(36)    NOT NULL,
    produto_id      VARCHAR(36)    NOT NULL,
    quantidade      NUMERIC(19, 4) NOT NULL,
    preco_unitario  NUMERIC(19, 2) NOT NULL,
    CONSTRAINT fk_item_pedido_pedido  FOREIGN KEY (pedido_id)  REFERENCES pedido (id),
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (produto_id) REFERENCES produto (id)
);