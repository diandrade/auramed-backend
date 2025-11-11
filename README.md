# 🩺 Auramed - REST API de Gerenciamento de Pacientes

Uma API RESTful desenvolvida com **Quarkus** para cadastro e gerenciamento de pacientes, médicos e teleconsultas, seguindo os princípios da **Clean Architecture**.

-----

## 🚀 Tecnologias

  - **Java 21** – Linguagem de programação
  - **Quarkus 3.29.0** – Framework Supersonic Subatomic Java
  - **Oracle Database** – Banco de dados relacional
  - **LangChain4j (Gemini)** – Integração com IA Generativa
  - **JAX-RS (quarkus-rest)** – API REST
  - **JDBC** – Acesso a dados
  - **Maven** – Gerenciamento de dependências

-----

## 🏗️ Arquitetura do Projeto

A estrutura do projeto segue os princípios da Clean Architecture, separando responsabilidades em camadas distintas:

```
📦 auramed
├── 📁 domain         # Camada de Domínio
│   ├── model        # Entidades (Paciente, Medico, Pessoa)
│   ├── repository   # Interfaces de repositório
│   ├── service      # Interfaces de serviço
│   └── exception    # Exceções de domínio
├── 📁 application    # Camada de Aplicação
│   └── service      # Implementações de serviço (Regras de negócio)
├── 📁 infrastructure # Camada de Infraestrutura
│   ├── api/rest     # Controllers REST (Exposição da API)
│   ├── persistence  # Implementações JDBC dos repositórios
│   ├── config       # Configurações (Banco, CORS)
│   └── exception    # Exceções de infraestrutura
└── 📁 interfaces     # Camada de Interface (Adapters)
    ├── controllers  # Interfaces dos Controllers
    ├── dto          # Data Transfer Objects (Request/Response)
    └── mappers      # Mappers (DTO ↔ Domain)
```

-----

## 📋 Endpoints da API

### 🔐 Autenticação

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/auth/login` | Realiza login do médico e retorna um token JWT. |

### ❤️ Pacientes (Endpoints Principais)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/pacientes-completo` | Cadastra um paciente com todos os dados (Pessoa, Paciente, InfoTeleconsulta, PerfilCognitivo). |
| `GET` | `/pacientes-completo` | Lista todos os pacientes (com dados completos) vinculados ao médico logado. |
| `GET` | `/pacientes-completo/{id}` | Busca um paciente específico (com dados completos) por ID. |
| `DELETE`| `/pacientes/{idPessoa}` | Exclui um paciente e todos os seus dados relacionados (em cascata). |

### ✏️ Entidades Individuais (Atualização)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `PUT` | `/pessoas/{id}` | Atualiza dados básicos da pessoa (nome, email, telefone, etc). |
| `PUT` | `/pacientes/{idPessoa}` | Atualiza dados específicos do paciente (Cartão SUS). |
| `PUT` | `/info-teleconsulta/{id}` | Atualiza as informações de teleconsulta do paciente. |
| `PUT` | `/perfil-cognitivo/{id}` | Atualiza o perfil cognitivo do paciente. |

### 🤖 IA e Relatórios

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/chatbot` | Envia uma pergunta para o chatbot (Gemini AI) sobre a base de conhecimento. |
| `GET` | `/relatorios/dashboard` | Retorna dados consolidados para o dashboard do médico. |

-----

## 🛠️ Como Executar

### ✅ Pré-requisitos

  - **Java 21**
  - **Maven 3.8+**
  - **Oracle Database** (configurado e acessível)

-----

### 📦 Passo a Passo

1.  Clone o repositório:

    ```bash
    git clone https://github.com/seu-usuario/auramed-sem2-java.git
    cd auramed-sem2-java
    ```

2.  Configure a conexão com o banco de dados no arquivo `src/main/resources/application.properties`.

3.  Execute o projeto em modo de desenvolvimento:

    ```bash
    mvn quarkus:dev
    ```

-----

## 📝 Exemplos de Uso

### Obter Token de Autenticação

```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "medico@exemplo.com",
  "senha": "senha123"
}
```

### Cadastrar Paciente (Completo)

```bash
POST http://localhost:8080/pacientes-completo
Content-Type: application/json
Authorization: Bearer seu-token-jwt-obtido-no-login

{
  "pessoa": {
    "nome": "João da Silva Santos",
    "email": "joao.silva@email.com",
    "cpf": "12345678901",
    "dataNascimento": "1980-05-15",
    "genero": "M",
    "telefone": "11999998888",
    "tipoPessoa": "PACIENTE"
  },
  "paciente": {
    "idMedicoResponsavel": 1,
    "nrCartaoSUS": "123456789012345"
  },
  "infoTeleconsulta": {
    "cdHabilidadeDigital": "MEDIA",
    "cdCanalLembrete": "WHATSAPP",
    "inPrecisaCuidador": "N",
    "inJaFezTele": "S"
  },
  "perfilCognitivo": {
    "inDificuldadeVisao": "N",
    "inUsaOculos": "S",
    "inDificuldadeAudicao": "N",
    "inUsaAparelhoAud": "N",
    "inDificuldadeCogn": "N"
  }
}
```

### Listar Pacientes do Médico

```bash
GET http://localhost:8080/pacientes-completo
Authorization: Bearer seu-token-jwt-obtido-no-login
```

-----

## 🧪 Testando a API

### Com Insomnia/Postman

1.  Importe a coleção de endpoints (se disponível) ou crie as requisições manualmente.
2.  Execute a requisição de `/auth/login` primeiro para obter o `Bearer Token`.
3.  Configure o token nas demais requisições que exigem autenticação.

### Com `curl`

```bash
# Obter token (substitua com dados válidos)
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "medico@exemplo.com", "senha": "senha123"}' | jq -r .token)

echo "Token obtido: $TOKEN"

# Listar pacientes usando o token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/pacientes-completo
```

-----

## 🗂️ Estrutura do Projeto (Detalhada)

```
src/
├── main/
│   ├── java/
│   │   └── br/
│   │       └── com/
│   │           └── auramed/
│   │               ├── domain/
│   │               │   ├── model/
│   │               │   ├── repository/
│   │               │   ├── service/
│   │               │   └── exception/
│   │               ├── application/
│   │               │   └── service/
│   │               ├── infrastructure/
│   │               │   ├── api/rest/
│   │               │   ├── persistence/
│   │               │   ├── config/
│   │               │   └── exception/
│   │               └── interfaces/
│   │                   ├── controllers/
│   │                   ├── dto/
│   │                   └── mappers/
│   └── resources/
│       └── application.properties
└── test/
```

-----

## 🔧 Desenvolvimento

### Comandos Úteis

```bash
# Desenvolvimento com hot reload
mvn quarkus:dev

# Compilar
mvn clean compile

# Executar testes
mvn test

# Empacotar (cria o .jar)
mvn package

# Verificar árvore de dependências
mvn dependency:tree
```

💡 O Quarkus oferece **hot reload** durante o desenvolvimento. Basta salvar um arquivo `.java` e o Quarkus recompila e atualiza a aplicação automaticamente.

-----

## 📊 Modelo de Dados (Simplificado)

### Pessoa

  - `ID_PESSOA (PK)`: Identificador único
  - `NM_PESSOA`: Nome da pessoa
  - `NM_EMAIL`: Email
  - `NR_CPF`: CPF
  - `DT_NASCIMENTO`: Data de Nascimento
  - `CD_GENERO`: Gênero (M, F, O)
  - `NR_TELEFONE`: Telefone
  - `TP_PESSOA`: Tipo (PACIENTE, MEDICO, CUIDADOR)

### Paciente

  - `ID_PESSOA (PK, FK)`: Referência à Pessoa
  - `ID_MEDICO (FK)`: Referência ao Médico responsável
  - `NR_CARTAO_SUS`: Número do Cartão SUS

### InfoTeleconsulta

  - `ID_INFO_TELECONSULTA (PK)`: Identificador único
  - `ID_PACIENTE (FK)`: Referência ao Paciente
  - `CD_HABILIDADE_DIGITAL`: (BAIXA, MEDIA, ALTA)
  - `CD_CANAL_LEMBRETE`: (WHATSAPP, SMS, EMAIL, TELEFONE)
  - `IN_PRECISA_CUIDADOR`: (S/N)
  - `IN_JA_FEZ_TELE`: (S/N)

### PerfilCognitivo

  - `ID_PERFIL_COGNITIVO (PK)`: Identificador único
  - `ID_PACIENTE (FK)`: Referência ao Paciente
  - `IN_DIFICULDADE_VISAO`: (S/N)
  - `IN_USA_OCULOS`: (S/N)
  - `IN_DIFICULDADE_AUDICAO`: (S/N)
  - ... (e outros indicadores cognitivos)

-----

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo `LICENSE` para mais detalhes.

-----

## 👥 Autor

(Substitua pelo seu nome e link do GitHub)
**Diego Andrade dos Santos** – [@diandrade](https://github.com/diandrade)

-----

## 🙏 Agradecimentos

  - Equipe FIAP
  - Comunidade Quarkus
  - Oracle
