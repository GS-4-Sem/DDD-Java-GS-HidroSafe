package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.LeituraBO;
import br.com.fiap.hidrosafe.entities.Leitura;
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
@RequestMapping("/leituras")
public class LeituraController {

    private final LeituraBO leituraBO;

    public LeituraController(LeituraBO leituraBO) {
        this.leituraBO = leituraBO;
    }

    @PostMapping
    public ResponseEntity<Leitura> cadastrar(@Valid @RequestBody Leitura leitura) {
        Leitura criado = leituraBO.cadastrar(leitura);
        URI local = URI.create("/leituras/" + criado.getIdLeitura());
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public List<Leitura> listar() {
        return leituraBO.listar();
    }

    @GetMapping("/{id}")
    public Leitura buscar(@PathVariable Long id) {
        return leituraBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Leitura atualizar(@PathVariable Long id, @Valid @RequestBody Leitura leitura) {
        return leituraBO.atualizar(id, leitura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        leituraBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}