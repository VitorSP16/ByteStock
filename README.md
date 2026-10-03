# ByteStock

ByteStock é um sistema web de controle de estoque de hardware, desenvolvido como projeto para o Etec Portas Abertas.

O sistema permite cadastrar e gerenciar produtos de hardware, além de controlar entradas, saídas e o histórico do estoque.

## Funcionalidades

- Cadastro de produtos
- Listagem de produtos
- Edição de produtos
- Exclusão de produtos
- Entrada de produtos no estoque
- Saída de produtos do estoque
- Histórico de movimentações
- Controle de estoque mínimo

## Tecnologias utilizadas

### Back-end

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Thymeleaf
- Lombok
- Maven

### Banco de dados

- H2 em modo arquivo (embutido e persistente)
- Não precisa instalar PostgreSQL ou usar pgAdmin
- Os dados ficam salvos localmente em `bytestock/data/bytestock.mv.db`

### Front-end

- HTML
- CSS
- Bootstrap
- Thymeleaf

## Como executar

Entre na pasta do projeto:

```bash
cd bytestock
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse:

```text
http://localhost:8080
```

O banco é criado automaticamente na primeira execução. Ao fechar e abrir o sistema novamente, os produtos e movimentações continuam salvos.

## Estrutura do Back-end

O projeto utiliza uma estrutura baseada em camadas:

- **Entity:** representa as entidades do sistema.
- **Repository:** realiza o acesso aos dados.
- **Service:** contém as regras de negócio.
- **Controller:** recebe e controla as requisições da aplicação.

## Projeto

Projeto desenvolvido para apresentação no **Etec Portas Abertas**.
