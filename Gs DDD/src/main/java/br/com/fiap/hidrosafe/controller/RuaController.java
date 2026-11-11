package br.com.fiap.hidrosafe.controller;

import br.com.fiap.hidrosafe.bo.RuaBO;
import br.com.fiap.hidrosafe.entities.Rua;
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
@RequestMapping("/ruas")
public class RuaController {

    private final RuaBO ruaBO;

    public RuaController(RuaBO ruaBO) {
        this.ruaBO = ruaBO;
    }

    @PostMapping
    public ResponseEntity<Rua> cadastrar(@Valid @RequestBody Rua rua) {
        Rua criado = ruaBO.cadastrar(rua);
        URI local = URI.create("/ruas/" + criado.getIdRua());
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public List<Rua> listar() {
        return ruaBO.listar();
    }

    @GetMapping("/{id}")
    public Rua buscar(@PathVariable Long id) {
        return ruaBO.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Rua atualizar(@PathVariable Long id, @Valid @RequestBody Rua rua) {
        return ruaBO.atualizar(id, rua);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        ruaBO.remover(id);
        return ResponseEntity.noContent().build();
    }
}