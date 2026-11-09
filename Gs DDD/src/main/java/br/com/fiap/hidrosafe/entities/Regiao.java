package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Regiao {

    private Long idRegiao;

    @NotBlank(message = "O nome da região é obrigatório")
    @Size(max = 100, message = "O nome da região deve ter no máximo 100 caracteres")
    private String nmRegiao;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String nmCidade;

    @NotBlank(message = "O nível de risco é obrigatório")
    private String dsNivelRisco;

    public Regiao() {
    }

    public Long getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(Long idRegiao) {
        this.idRegiao = idRegiao;
    }

    public String getNmRegiao() {
        return nmRegiao;
    }

    public void setNmRegiao(String nmRegiao) {
        this.nmRegiao = nmRegiao;
    }

    public String getNmCidade() {
        return nmCidade;
    }

    public void setNmCidade(String nmCidade) {
        this.nmCidade = nmCidade;
    }

    public String getDsNivelRisco() {
        return dsNivelRisco;
    }

    public void setDsNivelRisco(String dsNivelRisco) {
        this.dsNivelRisco = dsNivelRisco;
    }
}
