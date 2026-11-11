package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.AbrigoDAO;
import br.com.fiap.hidrosafe.dao.RegiaoDAO;
import br.com.fiap.hidrosafe.entities.Abrigo;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbrigoBO {

    private final AbrigoDAO abrigoDAO;
    private final RegiaoDAO regiaoDAO;

    public AbrigoBO(AbrigoDAO abrigoDAO, RegiaoDAO regiaoDAO) {
        this.abrigoDAO = abrigoDAO;
        this.regiaoDAO = regiaoDAO;
    }

    public Abrigo cadastrar(Abrigo abrigo) {
        validar(abrigo);
        return abrigoDAO.inserir(abrigo);
    }

    public List<Abrigo> listar() {
        return abrigoDAO.listar();
    }

    public Abrigo buscarPorId(Long id) {
        Abrigo abrigo = abrigoDAO.buscarPorId(id);
        if (abrigo == null) {
            throw new RecursoNaoEncontradoException("Abrigo não encontrado com o id " + id + ".");
        }
        return abrigo;
    }

    public Abrigo atualizar(Long id, Abrigo abrigo) {
        buscarPorId(id);
        validar(abrigo);
        abrigo.setIdAbrigo(id);
        abrigoDAO.atualizar(abrigo);
        return abrigo;
    }

    public void remover(Long id) {
        buscarPorId(id);
        abrigoDAO.deletar(id);
    }

    private void validar(Abrigo abrigo) {
        abrigo.setNmAbrigo(abrigo.getNmAbrigo().trim());
        abrigo.setDsEndereco(abrigo.getDsEndereco().trim());

        if (abrigo.getNrCapacidade() <= 0) {
            throw new RegraNegocioException("A capacidade deve ser maior que zero.");
        }
        if (abrigo.getNrOcupacao() == null) {
            abrigo.setNrOcupacao(0);
        }
        if (abrigo.getNrKitsEstoque() == null) {
            abrigo.setNrKitsEstoque(0);
        }
        if (abrigo.getNrOcupacao() < 0) {
            throw new RegraNegocioException("A ocupação não pode ser negativa.");
        }
        if (abrigo.getNrKitsEstoque() < 0) {
            throw new RegraNegocioException("O estoque de kits não pode ser negativo.");
        }
        if (abrigo.getNrOcupacao() > abrigo.getNrCapacidade()) {
            throw new RegraNegocioException("A ocupação não pode superar a capacidade.");
        }

        String status = abrigo.getDsStatus();
        if (status == null || status.isBlank()) {
            status = "ABERTO";
        }
        status = EnumeracoesBO.normalizar(status, "Status inválido", "ABERTO", "LOTADO", "FECHADO");

        // ocupação cheia -> LOTADO; voltou a ter vaga -> LOTADO volta para ABERTO; FECHADO não muda
        if (!status.equals("FECHADO")) {
            if (abrigo.getNrOcupacao().equals(abrigo.getNrCapacidade())) {
                status = "LOTADO";
            } else if (status.equals("LOTADO")) {
                status = "ABERTO";
            }
        }
        abrigo.setDsStatus(status);

        if (regiaoDAO.buscarPorId(abrigo.getIdRegiao()) == null) {
            throw new RegraNegocioException("Região informada não existe.");
        }
    }
}