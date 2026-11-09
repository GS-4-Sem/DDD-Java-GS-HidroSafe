package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.RegiaoBO;
import br.com.fiap.hidrosafe.entities.Regiao;
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
@RequestMapping("/regioes")
public class RegiaoController {

    private final RegiaoBO regiaoBO;

    public RegiaoController(RegiaoBO regiaoBO) {
        this.regiaoBO = regiaoBO;
    }

    @PostMapping
    public ResponseEntity<Regiao> cadastrar(@Valid @RequestBody Regiao regiao) {
        Regiao criada = regiaoBO.cadastrar(regiao);
        URI local = URI.create("/regioes/" + criada.getIdRegiao());
        return ResponseEntity.created(local).body(criada);
    }

    @GetMapping
    public List<Regiao> listar() {
        return regiaoBO.listar();
    }

    @GetMapping("/{id}")
    public Regiao buscar(@PathVariable Long id) {
        return regiaoBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Regiao atualizar(@PathVariable Long id, @Valid @RequestBody Regiao regiao) {
        return regiaoBO.atualizar(id, regiao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        regiaoBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}
