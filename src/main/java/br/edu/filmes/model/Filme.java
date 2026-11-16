package br.edu.filmes.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "filmes")
@Getter
@Setter
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Informe o título")
    @Size(min = 2, max = 80, message = "O título deve ter de 2 a 80 caracteres")
    @Column(nullable = false, length = 80)
    private String titulo;

    @NotNull(message = "Informe o gênero")
    @Enumerated(EnumType.STRING)
    private GeneroFilme genero;

    @NotNull(message = "Informe o ano de lançamento")
    @Min(value = 1900, message = "Ano inválido")
    private Integer anoLancamento;

    @NotNull(message = "Informe o valor do aluguel")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valorAluguel;
}
