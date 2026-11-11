package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Sensor {

    private Long idSensor;

    @NotNull(message = "A rua é obrigatória")
    private Long idRua;

    @NotBlank(message = "O nome do sensor é obrigatório")
    @Size(max = 100, message = "O nome do sensor deve ter no máximo 100 caracteres")
    private String nmSensor;

    @Size(max = 10, message = "O status deve ter no máximo 10 caracteres")
    private String dsStatus;

    public Sensor() {
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public Long getIdRua() {
        return idRua;
    }

    public void setIdRua(Long idRua) {
        this.idRua = idRua;
    }

    public String getNmSensor() {
        return nmSensor;
    }

    public void setNmSensor(String nmSensor) {
        this.nmSensor = nmSensor;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }
}