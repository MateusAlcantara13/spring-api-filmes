package br.edu.filmes.service;



import br.edu.filmes.model.Filme;
import br.edu.filmes.model.GeneroFilme;
import br.edu.filmes.repository.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmeService {

    private final FilmeRepository repository;

    public FilmeService(FilmeRepository repository) {
        this.repository = repository;
    }

    public List<Filme> listarTodos() {
        return repository.findAll();
    }

    public Filme buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Filme salvar(Filme filme) {
        return repository.save(filme);
    }

    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public List<Filme> buscarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Filme> buscarPorGenero(GeneroFilme genero) {
        return repository.findByGenero(genero);
    }

    // ---- as operações que têm regra ----

    public Filme alugar(Long id) throws Exception {
        Filme filme = repository.findById(id)
                .orElseThrow(() -> new Exception("Filme não encontrado"));

        if(!filme.temEstoque()) {
            throw new Exception("Filme sem estoque disponível");
        }

        filme.alugar();
        return repository.save(filme);
    }

    public Filme devolver(Long id) throws Exception {
        Filme filme = repository.findById(id)
                .orElseThrow(() -> new Exception("Filme não encontrado"));


        filme.devolver();
        return repository.save(filme);
    }
}
