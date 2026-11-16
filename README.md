# API de Filmes

API REST para catálogo de filmes, com CRUD completo, validação e busca com filtros.

Projeto de estudo do curso de **Análise e Desenvolvimento de Sistemas** (SENAI-SP Vila Leopoldina) — primeira API REST da série, construída do zero.

## Stack

- Java 21
- Spring Boot 4 (Spring Web + Spring Data JPA)
- Bean Validation
- H2 (banco em memória)
- Lombok
- Maven

## Como rodar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. O console do banco fica em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:filmesdb`, usuário `sa`, sem senha).

## Endpoints

| Método | Rota | Resposta |
|---|---|---|
| GET | `/api/filmes` | 200 + lista |
| GET | `/api/filmes/{id}` | 200 + filme · 404 se não existir |
| POST | `/api/filmes` | 201 + header `Location` · 400 se inválido |
| PUT | `/api/filmes/{id}` | 200 + filme atualizado · 404 se não existir |
| DELETE | `/api/filmes/{id}` | 204 sem corpo · 404 se não existir |
| GET | `/api/filmes/busca?titulo=` | 200 + lista filtrada (ignora maiúsculas) |
| GET | `/api/filmes/genero/{genero}` | 200 + lista do gênero |

### Exemplo de cadastro

```json
POST /api/filmes

{
  "titulo": "Matrix",
  "genero": "ACAO",
  "anoLancamento": 1999,
  "valorAluguel": 9.90
}
```

Gêneros aceitos: `ACAO`, `COMEDIA`, `DRAMA`, `TERROR`.

## Estrutura

```
src/main/java/br/edu/filmes/
├── model/Filme.java            (entidade JPA + validações)
├── model/GeneroFilme.java      (enum)
├── repository/FilmeRepository.java
└── controller/FilmeController.java
```

## O que este projeto exercita

- `@RestController` e serialização automática para JSON
- `@RequestBody` convertendo JSON em objeto
- `@PathVariable` (identificar) × `@RequestParam` (filtrar)
- `ResponseEntity` para controlar status HTTP e headers
- Status codes: 200, 201, 204, 400, 404
- Bean Validation com `@Valid` respondendo 400 automaticamente
- Query methods derivados do nome (`findByTituloContainingIgnoreCase`)
- `BigDecimal` para valores monetários