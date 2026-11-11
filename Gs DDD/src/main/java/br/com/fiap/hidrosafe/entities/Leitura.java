package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class Leitura {

    private Long idLeitura;

    @NotNull(message = "A rua é obrigatória")
    private Long idRua;

    private Long idSensor;

    @NotBlank(message = "A origem da leitura é obrigatória")
    @Size(max = 10, message = "A origem deve ter no máximo 10 caracteres")
    private String dsOrigem;

    @NotNull(message = "O nível da água é obrigatório")
    @PositiveOrZero(message = "O nível da água não pode ser negativo")
    @DecimalMax(value = "9999.99", message = "O nível da água deve ser no máximo 9999.99 cm")
    private Double nrNivelCm;

    // gerada pelo banco (SYSTIMESTAMP); se vier no JSON, é ignorada
    private LocalDateTime dtLeitura;

    @Size(max = 100, message = "O nome do reportante deve ter no máximo 100 caracteres")
    private String nmReportante;

    public Leitura() {
    }

    public Long getIdLeitura() {
        return idLeitura;
    }

    public void setIdLeitura(Long idLeitura) {
        this.idLeitura = idLeitura;
    }

    public Long getIdRua() {
        return idRua;
    }

    public void setIdRua(Long idRua) {
        this.idRua = idRua;
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public String getDsOrigem() {
        return dsOrigem;
    }

    public void setDsOrigem(String dsOrigem) {
        this.dsOrigem = dsOrigem;
    }

    public Double getNrNivelCm() {
        return nrNivelCm;
    }

    public void setNrNivelCm(Double nrNivelCm) {
        this.nrNivelCm = nrNivelCm;
    }

    public LocalDateTime getDtLeitura() {
        return dtLeitura;
    }

    public void setDtLeitura(LocalDateTime dtLeitura) {
        this.dtLeitura = dtLeitura;
    }

    public String getNmReportante() {
        return nmReportante;
    }

    public void setNmReportante(String nmReportante) {
        this.nmReportante = nmReportante;
    }
}