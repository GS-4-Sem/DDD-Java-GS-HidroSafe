package br.com.fiap.hidrosafe.excecoes;

import java.time.LocalDateTime;

public class ErroResposta {

    private int status;
    private String erro;
    private String mensagem;
    private LocalDateTime dataHora;

    public ErroResposta(int status, String erro, String mensagem) {
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
