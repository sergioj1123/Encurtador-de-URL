# Encurtador de URL

Projeto de encurtador de URL desenvolvido em Java com Spring Boot. Permite criar URLs curtas a partir de URLs originais
e redirecionar para o destino original.

## Tecnologias

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger

## Configuração do Banco de Dados

No arquivo `src/main/resources/application.properties`, adicione:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/encurtador
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

Altere `seu_usuario` e `sua_senha` conforme sua configuração local.

## Como executar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/sergioj1123/Encurtador-de-URL.git
   cd seu-repo/encurtador
   ```

2. **Crie o banco de dados PostgreSQL:**
   ```sql
   CREATE DATABASE encurtador;
   ```

   Certifique-se de que o PostgreSQL esteja instalado e em execução.

3. **Configure o banco de dados PostgreSQL** conforme acima.

4. **Instale as dependências e execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

5. **Acesse a documentação Swagger:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

## Endpoints

### Encurtar URL

- **POST** `/url/shorten`
- Corpo da requisição:
  ```json
  {
    "originalUrl": "https://exemplo.com"
  }
  ```
- Resposta:
  ```json
  {
    "shortUrl": "http://localhost:8080/url/abcde"
  }
  ```

### Redirecionar para URL original

- **GET** `/url/{shortUrl}`
- Redireciona (HTTP 302) para a URL original.

## Observações

- URLs encurtadas expiram após 10 minutos.
- Erros são retornados em formato JSON.

