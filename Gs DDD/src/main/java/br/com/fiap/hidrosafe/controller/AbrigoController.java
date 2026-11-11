package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.AbrigoBO;
import br.com.fiap.hidrosafe.entities.Abrigo;
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
@RequestMapping("/abrigos")
public class AbrigoController {

    private final AbrigoBO abrigoBO;

    public AbrigoController(AbrigoBO abrigoBO) {
        this.abrigoBO = abrigoBO;
    }

    @PostMapping
    public ResponseEntity<Abrigo> cadastrar(@Valid @RequestBody Abrigo abrigo) {
        Abrigo criado = abrigoBO.cadastrar(abrigo);
        URI local = URI.create("/abrigos/" + criado.getIdAbrigo());
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public List<Abrigo> listar() {
        return abrigoBO.listar();
    }

    @GetMapping("/{id}")
    public Abrigo buscar(@PathVariable Long id) {
        return abrigoBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Abrigo atualizar(@PathVariable Long id, @Valid @RequestBody Abrigo abrigo) {
        return abrigoBO.atualizar(id, abrigo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        abrigoBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}