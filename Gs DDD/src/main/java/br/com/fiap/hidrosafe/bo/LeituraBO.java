package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.LeituraDAO;
import br.com.fiap.hidrosafe.dao.RuaDAO;
import br.com.fiap.hidrosafe.dao.SensorDAO;
import br.com.fiap.hidrosafe.entities.Alerta;
import br.com.fiap.hidrosafe.entities.Leitura;
import br.com.fiap.hidrosafe.entities.Rua;
import br.com.fiap.hidrosafe.entities.Sensor;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeituraBO {

    private static final double FATOR_EMERGENCIA = 1.5;

    private final LeituraDAO leituraDAO;
    private final RuaDAO ruaDAO;
    private final SensorDAO sensorDAO;

    public LeituraBO(LeituraDAO leituraDAO, RuaDAO ruaDAO, SensorDAO sensorDAO) {
        this.leituraDAO = leituraDAO;
        this.ruaDAO = ruaDAO;
        this.sensorDAO = sensorDAO;
    }

    // grava a leitura e, se o nível chegou ao limite da rua, o alerta automático (mesma transação)
    public Leitura cadastrar(Leitura leitura) {
        Rua rua = validar(leitura);
        Alerta alerta = gerarAlerta(leitura, rua);
        return leituraDAO.inserirComAlerta(leitura, alerta);
    }

    public List<Leitura> listar() {
        return leituraDAO.listar();
    }

    public Leitura buscarPorId(Long id) {
        Leitura leitura = leituraDAO.buscarPorId(id);
        if (leitura == null) {
            throw new RecursoNaoEncontradoException("Leitura não encontrada com o id " + id + ".");
        }
        return leitura;
    }

    // a atualização corrige a leitura, mas não gera alerta automático
    public Leitura atualizar(Long id, Leitura leitura) {
        buscarPorId(id);
        validar(leitura);
        leitura.setIdLeitura(id);
        leituraDAO.atualizar(leitura);
        return leituraDAO.buscarPorId(id);
    }

    public void remover(Long id) {
        buscarPorId(id);
        leituraDAO.deletar(id);
    }

    private Rua validar(Leitura leitura) {
        leitura.setDsOrigem(EnumeracoesBO.normalizar(leitura.getDsOrigem(), "Origem inválida", "SENSOR", "RELATO"));

        if (leitura.getNrNivelCm() < 0) {
            throw new RegraNegocioException("O nível da água não pode ser negativo.");
        }

        String reportante = leitura.getNmReportante();
        if (reportante != null) {
            reportante = reportante.trim();
            leitura.setNmReportante(reportante.isEmpty() ? null : reportante);
        }

        Rua rua = ruaDAO.buscarPorId(leitura.getIdRua());
        if (rua == null) {
            throw new RegraNegocioException("Rua informada não existe.");
        }

        if (leitura.getDsOrigem().equals("SENSOR")) {
            if (leitura.getIdSensor() == null) {
                throw new RegraNegocioException("Leitura de origem SENSOR exige o idSensor.");
            }
            Sensor sensor = sensorDAO.buscarPorId(leitura.getIdSensor());
            if (sensor == null) {
                throw new RegraNegocioException("Sensor informado não existe.");
            }
            if (!sensor.getIdRua().equals(leitura.getIdRua())) {
                throw new RegraNegocioException("O sensor informado não pertence à rua da leitura.");
            }
        } else if (leitura.getIdSensor() != null) {
            throw new RegraNegocioException("Leitura de origem RELATO não pode ter sensor.");
        }

        return rua;
    }

    private Alerta gerarAlerta(Leitura leitura, Rua rua) {
        double nivel = leitura.getNrNivelCm();
        double limite = rua.getNrLimiteCm();

        if (nivel < limite) {
            return null;
        }

        Alerta alerta = new Alerta();
        alerta.setIdRua(rua.getIdRua());
        alerta.setDsGravidade(nivel >= FATOR_EMERGENCIA * limite ? "EMERGENCIA" : "ALERTA");
        alerta.setDsMensagem("Nível de " + nivel + " cm acima do limite de " + limite + " cm");
        alerta.setDsStatus("ATIVO");
        return alerta;
    }
}