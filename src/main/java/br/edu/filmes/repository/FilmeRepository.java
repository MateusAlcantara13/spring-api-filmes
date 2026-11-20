package br.edu.filmes.repository;

import br.edu.filmes.model.Filme;
import br.edu.filmes.model.GeneroFilme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmeRepository extends JpaRepository<Filme, Long> {

    List<Filme> findByTituloContainingIgnoreCase(String titulo);

    List<Filme> findByGenero(GeneroFilme genero);

    List<Filme> id(long id);
}
