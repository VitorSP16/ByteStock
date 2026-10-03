# ByteStock

ByteStock é um sistema web de controle de estoque de hardware, desenvolvido como projeto para o Etec Portas Abertas.

O sistema permite cadastrar e gerenciar produtos de hardware, controlar entradas e saídas e acompanhar indicadores do estoque.

## Áreas do sistema

- **Dashboard:** quantidade de produtos, total de unidades, itens com estoque baixo, valor estimado do estoque e movimentações recentes.
- **Produtos:** listagem completa com busca e filtros.
- **Novo produto:** cadastro de componentes e quantidade inicial.
- **Estoque baixo:** lista automática dos produtos que precisam de reposição.
- **Movimentações:** histórico geral de entradas e saídas.
- **Relatórios:** resumo por categoria, fluxo de estoque, valor total e itens críticos.
- **Histórico por produto:** movimentações individuais de cada item.

## Regras de estoque

- A quantidade inicial de um produto novo é registrada como uma entrada.
- Depois do cadastro, a quantidade não é alterada diretamente na edição.
- Alterações de estoque devem ser feitas pelas telas de **Entrada** e **Saída**, preservando o histórico.
- Não é permitida saída maior do que a quantidade disponível.

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

Entre na pasta Java do projeto:

```powershell
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
