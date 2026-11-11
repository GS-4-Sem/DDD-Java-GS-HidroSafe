package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;

public final class EnumeracoesBO {

    private EnumeracoesBO() {
    }

    public static String normalizar(String valor, String mensagemInvalido, String... aceitos) {
        String texto = valor == null ? "" : valor.trim().toUpperCase();
        for (String aceito : aceitos) {
            if (aceito.equals(texto)) {
                return texto;
            }
        }
        throw new RegraNegocioException(mensagemInvalido + ". Use " + listar(aceitos) + ".");
    }

    private static String listar(String[] aceitos) {
        StringBuilder lista = new StringBuilder();
        for (int i = 0; i < aceitos.length; i++) {
            if (i > 0) {
                lista.append(i == aceitos.length - 1 ? " ou " : ", ");
            }
            lista.append(aceitos[i]);
        }
        return lista.toString();
    }
}