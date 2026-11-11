package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.RegiaoDAO;
import br.com.fiap.hidrosafe.dao.RuaDAO;
import br.com.fiap.hidrosafe.entities.Rua;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuaBO {

    private static final double LIMITE_PADRAO_CM = 30.0;

    private final RuaDAO ruaDAO;
    private final RegiaoDAO regiaoDAO;

    public RuaBO(RuaDAO ruaDAO, RegiaoDAO regiaoDAO) {
        this.ruaDAO = ruaDAO;
        this.regiaoDAO = regiaoDAO;
    }

    public Rua cadastrar(Rua rua) {
        validar(rua);
        return ruaDAO.inserir(rua);
    }

    public List<Rua> listar() {
        return ruaDAO.listar();
    }

    public Rua buscarPorId(Long id) {
        Rua rua = ruaDAO.buscarPorId(id);
        if (rua == null) {
            throw new RecursoNaoEncontradoException("Rua não encontrada com o id " + id + ".");
        }
        return rua;
    }

    public Rua atualizar(Long id, Rua rua) {
        buscarPorId(id);
        validar(rua);
        rua.setIdRua(id);
        ruaDAO.atualizar(rua);
        return rua;
    }

    public void remover(Long id) {
        buscarPorId(id);
        ruaDAO.deletar(id);
    }

    private void validar(Rua rua) {
        rua.setNmRua(rua.getNmRua().trim());

        if (rua.getNrLimiteCm() == null) {
            rua.setNrLimiteCm(LIMITE_PADRAO_CM);
        }
        if (rua.getNrLimiteCm() <= 0) {
            throw new RegraNegocioException("O limite de alagamento deve ser maior que zero.");
        }

        if (regiaoDAO.buscarPorId(rua.getIdRegiao()) == null) {
            throw new RegraNegocioException("Região informada não existe.");
        }
    }
}