package br.edu.filmes.service;

import java.math.BigDecimal;
import java.util.Optional;

import br.edu.filmes.model.Filme;
import br.edu.filmes.model.GeneroFilme;
import br.edu.filmes.repository.FilmeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceTest {

    @Mock
    private FilmeRepository repository;

    @InjectMocks
    private FilmeService service;

    @Test
    void deveAlugarFilmeQuandoTemEstoque() throws Exception {
        Filme filme = novoFilme(1L, 3);

        when(repository.findById(1L)).thenReturn(Optional.of(filme));
        when(repository.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = service.alugar(1L);

        assertEquals(2, resultado.getEstoque());
    }

    @Test
    void naoDeveAlugarFilmeSemEstoque() {
        Filme filme = novoFilme(1L, 0);


        when(repository.findById(1L)).thenReturn(Optional.of(filme));

        assertThrows(Exception.class, () -> service.alugar(1L));

        verify(repository, never()).save(any(Filme.class));
    }

    @Test
    void naoDeveAlugarFilmeInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.alugar(99L));

        verify(repository, never()).save(any(Filme.class));
    }

    @Test
    void deveDevolverFilmeAumentandoEstoque() throws Exception {
        Filme filme = novoFilme(1L, 2);

        when(repository.findById(1L)).thenReturn(Optional.of(filme));
        when(repository.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = service.devolver(1L);

        assertEquals(3, resultado.getEstoque());
    }

    @Test
    void naoDeveDevolverFilmeInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> service.devolver(99L));

        verify(repository, never()).save(any(Filme.class));
    }

    @Test
    void deveChamarRepositoryUmaUnicaVezAoAlugar() throws Exception {
        Filme filme = novoFilme(1L, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(filme));
        when(repository.save(any(Filme.class))).thenReturn(filme);

        service.alugar(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Filme.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 10, 50})
    void deveDiminuirEstoqueEmUmAoAlugar(int estoqueInicial) throws Exception {
        Filme filme = novoFilme(1L, estoqueInicial);

        when(repository.findById(1L)).thenReturn(Optional.of(filme));
        when(repository.save(any(Filme.class))).thenReturn(filme);

        Filme resultado = service.alugar(1L);

        assertEquals(estoqueInicial - 1, resultado.getEstoque());
    }

    private Filme novoFilme(Long id, int estoque) {
        Filme filme = new Filme();
        filme.setId(id);
        filme.setTitulo("Matrix");
        filme.setGenero(GeneroFilme.ACAO);
        filme.setAnoLancamento(1999);
        filme.setValorAluguel(new BigDecimal("9.90"));
        filme.setEstoque(estoque);
        return filme;
    }
}