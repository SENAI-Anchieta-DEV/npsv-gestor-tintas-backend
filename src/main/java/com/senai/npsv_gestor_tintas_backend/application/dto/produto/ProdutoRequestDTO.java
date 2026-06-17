package com.senai.npsv_gestor_tintas_backend.application.dto.produto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Objeto de transferência para registo ou atualização de um Produto")
public record ProdutoRequestDTO(

        @Schema(description = "Código de barras único do produto", example = "7891234567890")
        @NotBlank(message = "O código de barras é obrigatório.")
        String codigoBarras,

        @Schema(description = "Nome e especificações do produto", example = "Tinta Acrílica Fosca Branca 18L")
        @NotBlank(message = "A descrição do produto é obrigatória.")
        String descricao,

        @Schema(description = "Quantidade inicial ou atual em armazém", example = "50.0")
        @NotNull(message = "A quantidade inicial em estoque é obrigatória.")
        @DecimalMin(value = "0.0", message = "A quantidade de estoque não pode ser negativa.")
        BigDecimal quantidadeEstoque,

        @Schema(description = "Preço de aquisição do produto", example = "120.50")
        @NotNull(message = "O preço de custo é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço de custo deve ser maior que zero.")
        BigDecimal precoCusto,

        @Schema(description = "Preço final de venda ao público", example = "189.90")
        @NotNull(message = "O preço de venda é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço de venda deve ser maior que zero.")
        BigDecimal precoVenda,

        @Schema(description = "Unidade de medida adotada para o produto", example = "L")
        @NotNull(message = "A unidade de medida é obrigatória.")
        UnidadeMedida unidadeMedida,

        @Schema(description = "Identificador (UUID) da categoria à qual este produto pertence", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank(message = "O ID da categoria é obrigatório.")
        String categoriaId,

        @Schema(description = "Nível de estoque mínimo que dispara um alerta automático", example = "10.0")
        @NotNull(message = "O estoque mínimo é obrigatório.")
        @DecimalMin(value = "0.0", message = "O estoque mínimo não pode ser negativo.")
        BigDecimal estoqueMinimo
) {
    public Produto toEntity() {
        return Produto.builder()
                .codigoBarras(this.codigoBarras)
                .descricao(this.descricao)
                .quantidadeEstoque(this.quantidadeEstoque)
                .precoCusto(this.precoCusto)
                .precoVenda(this.precoVenda)
                .unidadeMedida(this.unidadeMedida)
                .categoria(CategoriaProduto.builder().id(this.categoriaId).build())
                .estoqueMinimo(this.estoqueMinimo)
                .estoqueEmAlerta(this.quantidadeEstoque.compareTo(this.estoqueMinimo) <= 0)
                .build();
    }
}
