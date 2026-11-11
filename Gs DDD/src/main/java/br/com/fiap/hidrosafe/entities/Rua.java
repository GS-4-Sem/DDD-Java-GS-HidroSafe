package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Rua {

    private Long idRua;

    @NotNull(message = "A região é obrigatória")
    private Long idRegiao;

    @NotBlank(message = "O nome da rua é obrigatório")
    @Size(max = 150, message = "O nome da rua deve ter no máximo 150 caracteres")
    private String nmRua;

    @Positive(message = "O limite de alagamento deve ser maior que zero")
    @DecimalMax(value = "9999.99", message = "O limite de alagamento deve ser no máximo 9999.99 cm")
    private Double nrLimiteCm;

    public Rua() {
    }

    public Long getIdRua() {
        return idRua;
    }

    public void setIdRua(Long idRua) {
        this.idRua = idRua;
    }

    public Long getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(Long idRegiao) {
        this.idRegiao = idRegiao;
    }

    public String getNmRua() {
        return nmRua;
    }

    public void setNmRua(String nmRua) {
        this.nmRua = nmRua;
    }

    public Double getNrLimiteCm() {
        return nrLimiteCm;
    }

    public void setNrLimiteCm(Double nrLimiteCm) {
        this.nrLimiteCm = nrLimiteCm;
    }
}