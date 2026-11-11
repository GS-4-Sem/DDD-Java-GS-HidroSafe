package br.com.fiap.hidrosafe.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class Alerta {

    private Long idAlerta;

    @NotNull(message = "A rua é obrigatória")
    private Long idRua;

    @NotBlank(message = "A gravidade é obrigatória")
    @Size(max = 20, message = "A gravidade deve ter no máximo 20 caracteres")
    private String dsGravidade;

    @NotBlank(message = "A mensagem é obrigatória")
    @Size(max = 300, message = "A mensagem deve ter no máximo 300 caracteres")
    private String dsMensagem;

    @Size(max = 10, message = "O status deve ter no máximo 10 caracteres")
    private String dsStatus;

    // gerada pelo banco (SYSDATE); se vier no JSON, é ignorada
    private LocalDateTime dtEmissao;

    public Alerta() {
    }

    public Long getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Long getIdRua() {
        return idRua;
    }

    public void setIdRua(Long idRua) {
        this.idRua = idRua;
    }

    public String getDsGravidade() {
        return dsGravidade;
    }

    public void setDsGravidade(String dsGravidade) {
        this.dsGravidade = dsGravidade;
    }

    public String getDsMensagem() {
        return dsMensagem;
    }

    public void setDsMensagem(String dsMensagem) {
        this.dsMensagem = dsMensagem;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }

    public LocalDateTime getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(LocalDateTime dtEmissao) {
        this.dtEmissao = dtEmissao;
    }
}