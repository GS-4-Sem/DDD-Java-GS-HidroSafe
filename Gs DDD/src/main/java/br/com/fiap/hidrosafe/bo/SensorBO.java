package br.com.fiap.hidrosafe.bo;

import br.com.fiap.hidrosafe.dao.RuaDAO;
import br.com.fiap.hidrosafe.dao.SensorDAO;
import br.com.fiap.hidrosafe.entities.Sensor;
import br.com.fiap.hidrosafe.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.hidrosafe.excecoes.RegraNegocioException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorBO {

    private final SensorDAO sensorDAO;
    private final RuaDAO ruaDAO;

    public SensorBO(SensorDAO sensorDAO, RuaDAO ruaDAO) {
        this.sensorDAO = sensorDAO;
        this.ruaDAO = ruaDAO;
    }

    public Sensor cadastrar(Sensor sensor) {
        validar(sensor);
        return sensorDAO.inserir(sensor);
    }

    public List<Sensor> listar() {
        return sensorDAO.listar();
    }

    public Sensor buscarPorId(Long id) {
        Sensor sensor = sensorDAO.buscarPorId(id);
        if (sensor == null) {
            throw new RecursoNaoEncontradoException("Sensor não encontrado com o id " + id + ".");
        }
        return sensor;
    }

    public Sensor atualizar(Long id, Sensor sensor) {
        buscarPorId(id);
        validar(sensor);
        sensor.setIdSensor(id);
        sensorDAO.atualizar(sensor);
        return sensor;
    }

    public void remover(Long id) {
        buscarPorId(id);
        sensorDAO.deletar(id);
    }

    private void validar(Sensor sensor) {
        sensor.setNmSensor(sensor.getNmSensor().trim());

        String status = sensor.getDsStatus();
        if (status == null || status.isBlank()) {
            status = "ATIVO";
        }
        sensor.setDsStatus(EnumeracoesBO.normalizar(status, "Status inválido", "ATIVO", "INATIVO"));

        if (ruaDAO.buscarPorId(sensor.getIdRua()) == null) {
            throw new RegraNegocioException("Rua informada não existe.");
        }
    }
}