package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.RegiaoDAO;
import br.com.fiap.hidrosafe.entities.Regiao;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegiaoBO {

    private final RegiaoDAO regiaoDAO;

    public RegiaoBO(RegiaoDAO regiaoDAO) {
        this.regiaoDAO = regiaoDAO;
    }

    public Regiao cadastrar(Regiao regiao) {
        validar(regiao);
        return regiaoDAO.inserir(regiao);
    }

    public List<Regiao> listar() {
        return regiaoDAO.listar();
    }

    public Regiao buscarPorId(Long id) {
        Regiao regiao = regiaoDAO.buscarPorId(id);
        if (regiao == null) {
            throw new RecursoNaoEncontradoException("Região não encontrada com o id " + id + ".");
        }
        return regiao;
    }

    public Regiao atualizar(Long id, Regiao regiao) {
        buscarPorId(id);
        validar(regiao);
        regiao.setIdRegiao(id);
        regiaoDAO.atualizar(regiao);
        return regiao;
    }

    public void remover(Long id) {
        buscarPorId(id);
        regiaoDAO.deletar(id);
    }

    private void validar(Regiao regiao) {
        regiao.setNmRegiao(regiao.getNmRegiao().trim());
        regiao.setNmCidade(regiao.getNmCidade().trim());

        String risco = regiao.getDsNivelRisco().trim().toUpperCase();
        if (!risco.equals("BAIXO") && !risco.equals("MEDIO") && !risco.equals("ALTO")) {
            throw new RegraNegocioException("Nível de risco inválido. Use BAIXO, MEDIO ou ALTO.");
        }
        regiao.setDsNivelRisco(risco);
    }
}
