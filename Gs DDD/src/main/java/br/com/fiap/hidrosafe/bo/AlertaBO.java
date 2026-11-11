package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.AlertaDAO;
import br.com.fiap.hidrosafe.dao.RuaDAO;
import br.com.fiap.hidrosafe.entities.Alerta;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertaBO {

    private final AlertaDAO alertaDAO;
    private final RuaDAO ruaDAO;

    public AlertaBO(AlertaDAO alertaDAO, RuaDAO ruaDAO) {
        this.alertaDAO = alertaDAO;
        this.ruaDAO = ruaDAO;
    }

    public Alerta cadastrar(Alerta alerta) {
        validar(alerta);
        return alertaDAO.inserir(alerta);
    }

    public List<Alerta> listar() {
        return alertaDAO.listar();
    }

    public Alerta buscarPorId(Long id) {
        Alerta alerta = alertaDAO.buscarPorId(id);
        if (alerta == null) {
            throw new RecursoNaoEncontradoException("Alerta não encontrado com o id " + id + ".");
        }
        return alerta;
    }

    // o PUT também serve para encerrar o alerta (dsStatus = ENCERRADO)
    public Alerta atualizar(Long id, Alerta alerta) {
        buscarPorId(id);
        validar(alerta);
        alerta.setIdAlerta(id);
        alertaDAO.atualizar(alerta);
        return alertaDAO.buscarPorId(id);
    }

    public void remover(Long id) {
        buscarPorId(id);
        alertaDAO.deletar(id);
    }

    private void validar(Alerta alerta) {
        alerta.setDsMensagem(alerta.getDsMensagem().trim());
        if (alerta.getDsMensagem().isEmpty()) {
            throw new RegraNegocioException("A mensagem é obrigatória.");
        }

        alerta.setDsGravidade(EnumeracoesBO.normalizar(alerta.getDsGravidade(), "Gravidade inválida",
                "ATENCAO", "ALERTA", "EMERGENCIA"));

        String status = alerta.getDsStatus();
        if (status == null || status.isBlank()) {
            status = "ATIVO";
        }
        alerta.setDsStatus(EnumeracoesBO.normalizar(status, "Status inválido", "ATIVO", "ENCERRADO"));

        if (ruaDAO.buscarPorId(alerta.getIdRua()) == null) {
            throw new RegraNegocioException("Rua informada não existe.");
        }
    }
}