package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class Abrigo {

    private Long idAbrigo;

    @NotNull(message = "A região é obrigatória")
    private Long idRegiao;

    @NotBlank(message = "O nome do abrigo é obrigatório")
    @Size(max = 100, message = "O nome do abrigo deve ter no máximo 100 caracteres")
    private String nmAbrigo;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    private String dsEndereco;

    @NotNull(message = "A capacidade é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    @Max(value = 99999, message = "A capacidade deve ser no máximo 99999")
    private Integer nrCapacidade;

    @PositiveOrZero(message = "A ocupação não pode ser negativa")
    @Max(value = 99999, message = "A ocupação deve ser no máximo 99999")
    private Integer nrOcupacao;

    @PositiveOrZero(message = "O estoque de kits não pode ser negativo")
    @Max(value = 99999, message = "O estoque de kits deve ser no máximo 99999")
    private Integer nrKitsEstoque;

    @Size(max = 10, message = "O status deve ter no máximo 10 caracteres")
    private String dsStatus;

    public Abrigo() {
    }

    public Long getIdAbrigo() {
        return idAbrigo;
    }

    public void setIdAbrigo(Long idAbrigo) {
        this.idAbrigo = idAbrigo;
    }

    public Long getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(Long idRegiao) {
        this.idRegiao = idRegiao;
    }

    public String getNmAbrigo() {
        return nmAbrigo;
    }

    public void setNmAbrigo(String nmAbrigo) {
        this.nmAbrigo = nmAbrigo;
    }

    public String getDsEndereco() {
        return dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public Integer getNrCapacidade() {
        return nrCapacidade;
    }

    public void setNrCapacidade(Integer nrCapacidade) {
        this.nrCapacidade = nrCapacidade;
    }

    public Integer getNrOcupacao() {
        return nrOcupacao;
    }

    public void setNrOcupacao(Integer nrOcupacao) {
        this.nrOcupacao = nrOcupacao;
    }

    public Integer getNrKitsEstoque() {
        return nrKitsEstoque;
    }

    public void setNrKitsEstoque(Integer nrKitsEstoque) {
        this.nrKitsEstoque = nrKitsEstoque;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }
}