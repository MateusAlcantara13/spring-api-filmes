package br.edu.filmes.controller;


import br.edu.filmes.model.Filme;
import br.edu.filmes.model.GeneroFilme;
import br.edu.filmes.repository.FilmeRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    private final FilmeRepository repository;

    public FilmeController(FilmeRepository repository) {
        this.repository = repository;
    }

    // GET /api/filmes
    @GetMapping
    public List<Filme> listar() {
        return repository.findAll();
    }

    // GET /api/filmes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable Long id) {
        Filme filme = repository.findById(id).orElse(null);

        if (filme == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(filme);

    }

    // POST /api/filmes
    @PostMapping
    public ResponseEntity<Filme> cadastrar(@Valid @RequestBody Filme filme) {
        Filme salvo = repository.save(filme);

        return ResponseEntity
                .created(URI.create("/api/filmes/" + salvo.getId()))
                .body(salvo);
    }

    // PUT /api/filmes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Filme> atualizar(@PathVariable Long id, @Valid @RequestBody Filme filme) {
        Filme existente = repository.findById(id).orElse(null);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setTitulo(filme.getTitulo());
        existente.setGenero(filme.getGenero());
        existente.setAnoLancamento(filme.getAnoLancamento());
        existente.setValorAluguel(filme.getValorAluguel());

        return ResponseEntity.ok(repository.save(existente));
    }

    // DELETE /api/filmes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/filmes/busca?titulo=matrix
    @GetMapping("/busca")
    public List<Filme> buscarPorTitulo(@RequestParam String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    // GET /api/filmes/genero/ACAO
    @GetMapping("/genero/{genero}")
    public List<Filme> buscarPorGenero(@PathVariable GeneroFilme genero) {
        return repository.findByGenero(genero);
    }
}
