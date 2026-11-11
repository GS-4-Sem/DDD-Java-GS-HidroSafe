package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.AlertaBO;
import br.com.fiap.hidrosafe.entities.Alerta;
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
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaBO alertaBO;

    public AlertaController(AlertaBO alertaBO) {
        this.alertaBO = alertaBO;
    }

    @PostMapping
    public ResponseEntity<Alerta> cadastrar(@Valid @RequestBody Alerta alerta) {
        Alerta criado = alertaBO.cadastrar(alerta);
        URI local = URI.create("/alertas/" + criado.getIdAlerta());
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public List<Alerta> listar() {
        return alertaBO.listar();
    }

    @GetMapping("/{id}")
    public Alerta buscar(@PathVariable Long id) {
        return alertaBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Alerta atualizar(@PathVariable Long id, @Valid @RequestBody Alerta alerta) {
        return alertaBO.atualizar(id, alerta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alertaBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}