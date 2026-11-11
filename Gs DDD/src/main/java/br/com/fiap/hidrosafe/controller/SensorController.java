package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.SensorBO;
import br.com.fiap.hidrosafe.entities.Sensor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sensores")
public class SensorController {

    private final SensorBO sensorBO;

    public SensorController(SensorBO sensorBO) {
        this.sensorBO = sensorBO;
    }

    @PostMapping
    public ResponseEntity<Sensor> cadastrar(@Valid @RequestBody Sensor sensor) {
        Sensor criado = sensorBO.cadastrar(sensor);
        URI local = URI.create("/sensores/" + criado.getIdSensor());
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public List<Sensor> listar() {
        return sensorBO.listar();
    }

    @GetMapping("/{id}")
    public Sensor buscar(@PathVariable Long id) {
        return sensorBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Sensor atualizar(@PathVariable Long id, @Valid @RequestBody Sensor sensor) {
        return sensorBO.atualizar(id, sensor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        sensorBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}