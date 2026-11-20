package br.edu.filmes.controller;

import java.net.URI;
import java.util.List;

import br.edu.filmes.model.Filme;
import br.edu.filmes.model.GeneroFilme;
import br.edu.filmes.service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    private final FilmeService service;

    public FilmeController(FilmeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Filme> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable Long id) {
        Filme filme = service.buscarPorId(id);

        if (filme == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(filme);
    }

    @PostMapping
    public ResponseEntity<Filme> cadastrar(@Valid @RequestBody Filme filme) {
        Filme salvo = service.salvar(filme);

        return ResponseEntity
                .created(URI.create("/api/filmes/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filme> atualizar(@PathVariable Long id,
                                           @Valid @RequestBody Filme filme) {
        Filme existente = service.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setTitulo(filme.getTitulo());
        existente.setGenero(filme.getGenero());
        existente.setAnoLancamento(filme.getAnoLancamento());
        existente.setValorAluguel(filme.getValorAluguel());

        return ResponseEntity.ok(service.salvar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!service.existe(id)) {
            return ResponseEntity.notFound().build();
        }

        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/busca")
    public List<Filme> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    @GetMapping("/genero/{genero}")
    public List<Filme> buscarPorGenero(@PathVariable GeneroFilme genero) {
        return service.buscarPorGenero(genero);
    }

    // ───── as operações novas ─────

    @PostMapping("/{id}/alugar")
    public ResponseEntity<?> alugar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.alugar(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.devolver(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}