UPDATE produto
SET estoque_em_alerta = (quantidade_estoque <= estoque_minimo);