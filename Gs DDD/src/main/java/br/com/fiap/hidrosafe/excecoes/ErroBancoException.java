package br.com.fiap.hidrosafe.excecoes;

public class ErroBancoException extends RuntimeException {

    private final int codigoErro;

    public ErroBancoException(String mensagem, int codigoErro) {
        super(mensagem);
        this.codigoErro = codigoErro;
    }

    public int getCodigoErro() {
        return codigoErro;
    }
}