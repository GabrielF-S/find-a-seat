# 🪑 FindASeat

Backend de um sistema de gerenciamento e reserva de assentos desenvolvido para ambientes corporativos.

O **FindASeat** permite gerenciar empresas, andares, assentos, colaboradores e reservas, além de possuir mecanismos para controle de disponibilidade e fila de espera.

O projeto foi desenvolvido com foco em **boas práticas de desenvolvimento backend, arquitetura em camadas, persistência de dados, testes automatizados, regras de negócio e integridade dos dados**.

---

## 📌 Sobre o projeto

O FindASeat foi desenvolvido para solucionar um problema comum em ambientes corporativos: o gerenciamento de espaços e assentos compartilhados.

A aplicação permite organizar os ambientes através da seguinte estrutura:

```text
Business
   └── Floor
        └── Seat
```

Os colaboradores podem realizar reservas de assentos de acordo com as regras de negócio da aplicação.

O backend é responsável por controlar a disponibilidade dos assentos, validar conflitos de horários, gerenciar o ciclo de vida das reservas e processar a fila de espera.

---

## 🚀 Principais funcionalidades

### 🏢 Gerenciamento de ambientes

* Cadastro de empresas;
* Cadastro de andares;
* Organização dos assentos por andar;
* Associação dos ambientes a uma empresa.

### 💺 Gerenciamento de assentos

* Cadastro de assentos;
* Diferentes tipos de assentos;
* Controle da quantidade de lugares;
* Assentos exclusivos;
* Controle de status;
* Associação do assento a um andar.

### 📅 Gerenciamento de reservas

* Criação de reservas;
* Definição do dia da reserva;
* Definição do horário de início e término;
* Associação da reserva a um colaborador;
* Associação da reserva a um assento;
* Controle do status da reserva;
* Controle de reservas ativas.

### ⏳ Fila de espera

Quando não existem assentos disponíveis, o colaborador pode entrar em uma fila de espera.

O backend verifica a disponibilidade dos assentos e pode processar os colaboradores presentes na fila conforme novos assentos se tornam disponíveis.

### 🔔 Controle de confirmação

As reservas possuem diferentes estados durante seu ciclo de vida.

Uma reserva pode ser criada com status `PENDING` e posteriormente ser confirmada através do status `CONFIRMED`.

Existe também um processo agendado responsável por verificar reservas pendentes e executar as ações necessárias de acordo com o horário da reserva.

---

# 🛠️ Tecnologias utilizadas

### Backend

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Hibernate**
* **Spring Validation**
* **PostgreSQL**
* **Flyway**
* **JUnit 5**
* **Mockito**
* **Docker**

### Ferramentas

* IntelliJ IDEA
* Docker / Docker Compose
* Git
* GitHub
* Postman

---

# 🏗️ Arquitetura

O projeto utiliza uma arquitetura em camadas, separando as responsabilidades de cada componente da aplicação.

```text
                    ┌─────────────────────┐
                    │      REST API       │
                    │     Controller      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       Service       │
                    │   Regras de negócio │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Repository      │
                    │    Spring Data JPA  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     PostgreSQL      │
                    └─────────────────────┘
```

A aplicação segue principalmente o fluxo:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Essa separação permite manter as regras de negócio isoladas da camada de exposição da API e da persistência.

---

# 📦 Principais componentes

### Controller

Responsável pela exposição dos endpoints REST e pelo recebimento das requisições HTTP.

### Service

Concentra as principais regras de negócio da aplicação, como:

* Validação de colaboradores;
* Verificação de disponibilidade de assentos;
* Validação de conflitos entre reservas;
* Criação e atualização de reservas;
* Processamento da fila de espera;
* Controle do ciclo de vida das reservas.

### Repository

Responsável pelo acesso aos dados utilizando **Spring Data JPA**.

São utilizadas consultas derivadas do Spring Data e consultas específicas quando necessário.

### Entity

Representa as principais entidades persistidas no banco de dados.

Entre elas:

```text
Business
Floor
Seat
Employee
Reservation
Waitlist
```

### DTO

Os DTOs são utilizados para separar os objetos utilizados na comunicação da API das entidades persistidas no banco.

### Mapper

Responsável pela conversão entre entidades e DTOs.

### Exception

Centraliza exceções relacionadas às regras de negócio e situações específicas da aplicação.

---

# 🗄️ Banco de dados

O FindASeat utiliza **PostgreSQL** como banco de dados relacional.

A evolução do schema é controlada utilizando **Flyway**, permitindo versionar as alterações realizadas na estrutura do banco.

Exemplo:

```text
src
└── main
    └── resources
        └── db
            └── migration
                ├── V1__create_business.sql
                ├── V2__create_floor.sql
                ├── V3__create_seat.sql
                └── ...
```

As principais entidades do sistema são:

```text
Business
    │
    └── Floor
          │
          └── Seat

Employee
    │
    ├── Reservation
    │       └── Seat
    │
    └── Waitlist
```

---

# 📅 Controle de conflitos de reservas

Um dos principais pontos da regra de negócio é impedir que um mesmo assento seja reservado simultaneamente por diferentes colaboradores.

Cada reserva possui:

```text
reservationDay
startTime
endTime
```

Antes de uma reserva ser persistida, o sistema verifica se já existe uma reserva ativa para o mesmo assento e dia.

Quando existe uma reserva, os períodos são analisados para identificar possíveis sobreposições.

Exemplo:

```text
Reserva existente
10:00 ───────── 11:00

Nova reserva
10:30 ───────── 11:30
```

Nesse cenário existe conflito.

Já:

```text
Reserva existente
10:00 ───────── 11:00

Nova reserva
11:00 ───────── 12:00
```

não existe sobreposição entre os períodos.

Além da validação realizada pela aplicação, o banco de dados possui mecanismos de integridade para ajudar a evitar inconsistências.

---

# ⏳ Processamento da fila de espera

Quando não há assentos disponíveis, o colaborador pode ser adicionado à fila de espera.

O backend possui um processo responsável por verificar a disponibilidade dos assentos para os colaboradores presentes na fila.

Esse fluxo envolve:

```text
Waitlist
   ↓
Verificação de disponibilidade
   ↓
Assento disponível?
   ↓
Sim
   ↓
Criação da Reservation
```

Um dos desafios técnicos desse processo é o **controle de concorrência**, já que múltiplos colaboradores podem estar aguardando pelo mesmo recurso.

---

# ⏰ Processamento automático de reservas

O projeto utiliza tarefas agendadas para realizar verificações periódicas das reservas.

O processo verifica reservas que ainda estão pendentes e avalia seu estado de acordo com o horário de início da reserva.

Esse mecanismo permite automatizar o ciclo de vida das reservas sem depender exclusivamente de uma requisição manual à API.

---

# 🧪 Testes

O projeto utiliza **JUnit 5 e Mockito** para testes unitários.

Os testes são utilizados principalmente para validar as regras de negócio dos Services e seus diferentes cenários.

Exemplo:

```java
@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;
}
```

As dependências externas são simuladas utilizando Mockito, permitindo testar a lógica do Service de forma isolada.

Entre os cenários testados estão:

* Criação de reservas;
* Validação de colaboradores;
* Validação de assentos;
* Verificação de disponibilidade;
* Validação de conflitos de horário;
* Tratamento de exceções;
* Processamento das regras de negócio.

---

# 🐳 Docker

O projeto utiliza Docker para facilitar a configuração do ambiente de desenvolvimento e execução dos serviços necessários.

O PostgreSQL pode ser executado em um container, evitando a necessidade de instalar e configurar o banco diretamente na máquina de desenvolvimento.

Exemplo:

```text
FindASeat
├── src
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# ⚙️ Como executar

## Pré-requisitos

* Java
* Maven
* Docker
* Docker Compose
* PostgreSQL
* Git

### 1. Clone o repositório

```bash
git clone https://github.com/SEU-USUARIO/findaseat.git
```

Entre no diretório:

```bash
cd findaseat
```

### 2. Execute o banco de dados

Caso esteja utilizando Docker Compose:

```bash
docker compose up -d
```

### 3. Configure a aplicação

Configure as propriedades de conexão com o PostgreSQL no arquivo de configuração da aplicação ou através de variáveis de ambiente.

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/findaseat
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 4. Execute o projeto

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

As migrations do Flyway serão executadas automaticamente durante a inicialização da aplicação.

---

# 📂 Estrutura do projeto

```text
src
├── main
│   ├── java
│   │   └── com.gabsdev.findaseat
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── model
│   │       │   ├── entity
│   │       │   ├── dto
│   │       │   └── enums
│   │       ├── mapper
│   │       ├── exception
│   │       └── config
│   │
│   └── resources
│       ├── application.properties
│       └── db
│           └── migration
│
└── test
    └── java
        └── com.gabsdev.findaseat
```

---

# 📈 Próximos passos

* [ ] Aumentar a cobertura dos testes unitários;
* [ ] Aprimorar o controle de concorrência no processamento da fila;
* [ ] Adicionar autenticação e autorização;
* [ ] Implementar CI/CD;
* [ ] Melhorar observabilidade e logging;
* [ ] Disponibilizar a aplicação em ambiente cloud.

---

# 🎯 Objetivo do projeto

O FindASeat é um projeto de estudo e portfólio desenvolvido para aplicar conceitos utilizados no desenvolvimento de aplicações backend reais.

O projeto busca colocar em prática conhecimentos em:

* Java;
* Spring Boot;
* APIs REST;
* Arquitetura em camadas;
* Programação Orientada a Objetos;
* Spring Data JPA;
* Hibernate;
* PostgreSQL;
* Modelagem relacional;
* Flyway;
* Testes unitários;
* Mockito;
* Docker;
* Regras de negócio;
* Integridade de dados;
* Concorrência;
* Agendamento de tarefas;
* Versionamento com Git.

---

# 👨‍💻 Desenvolvedor

**Gabriel Ferreira dos Santos**

Projeto desenvolvido com foco em evolução prática no desenvolvimento backend utilizando o ecossistema **Java + Spring Boot**.

---

⭐ Se o projeto foi interessante para você, considere deixar uma estrela no repositório!
