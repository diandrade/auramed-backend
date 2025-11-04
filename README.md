# Manual para Cadastro de Paciente - Front-end

## 📋 Visão Geral
Este manual descreve os endpoints e formatos de dados necessários para realizar o cadastro completo de um paciente no sistema Auramed.

## 🚀 Endpoints Principais

### 1. Cadastro Completo de Paciente (Recomendado)

**Endpoint:** `POST /pacientes-completo`

**Descrição:** Cria um paciente com todas as informações em uma única requisição.

**Headers:**
```http
Content-Type: application/json
```

**Request Body:**
```json
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

**Response (Sucesso - 201 Created):**
```json
{
  "pessoa": {
    "id": 100,
    "nome": "João da Silva Santos",
    "email": "joao.silva@email.com",
    "cpf": "12345678901",
    "dataNascimento": "1980-05-15",
    "genero": "M",
    "telefone": "11999998888",
    "tipoPessoa": "PACIENTE",
    "dataCadastro": "2024-01-15T10:30:00",
    "ativo": "S"
  },
  "paciente": {
    "idPessoa": 100,
    "idMedicoResponsavel": 1,
    "nrCartaoSUS": "123456789012345",
    "dataCadastro": "2024-01-15T10:30:00",
    "ativo": "S"
  },
  "infoTeleconsulta": {
    "idInfoTeleconsulta": 50,
    "idPaciente": 100,
    "cdHabilidadeDigital": "MEDIA",
    "cdCanalLembrete": "WHATSAPP",
    "inPrecisaCuidador": "N",
    "inJaFezTele": "S",
    "dataCadastro": "2024-01-15T10:30:00",
    "dataAtualizacao": "2024-01-15T10:30:00"
  },
  "perfilCognitivo": {
    "idPerfilCognitivo": 25,
    "idPaciente": 100,
    "inDificuldadeVisao": "N",
    "inUsaOculos": "S",
    "inDificuldadeAudicao": "N",
    "inUsaAparelhoAud": "N",
    "inDificuldadeCogn": "N",
    "dataCadastro": "2024-01-15T10:30:00",
    "dataAtualizacao": "2024-01-15T10:30:00"
  }
}
```

### 2. Cadastro em Etapas

#### Etapa 1: Criar Pessoa
**Endpoint:** `POST /pessoas`

```json
{
  "nome": "João da Silva Santos",
  "email": "joao.silva@email.com",
  "cpf": "12345678901",
  "dataNascimento": "1980-05-15",
  "genero": "M",
  "telefone": "11999998888",
  "tipoPessoa": "PACIENTE"
}
```

#### Etapa 2: Criar Paciente
**Endpoint:** `POST /pacientes`

```json
{
  "idPessoa": 100,
  "idMedicoResponsavel": 1,
  "nrCartaoSUS": "123456789012345"
}
```

#### Etapa 3: Informações de Teleconsulta (Opcional)
**Endpoint:** `POST /info-teleconsulta`

```json
{
  "idPaciente": 100,
  "cdHabilidadeDigital": "MEDIA",
  "cdCanalLembrete": "WHATSAPP",
  "inPrecisaCuidador": "N",
  "inJaFezTele": "S"
}
```

#### Etapa 4: Perfil Cognitivo (Opcional)
**Endpoint:** `POST /perfil-cognitivo`

```json
{
  "idPaciente": 100,
  "inDificuldadeVisao": "N",
  "inUsaOculos": "S",
  "inDificuldadeAudicao": "N",
  "inUsaAparelhoAud": "N",
  "inDificuldadeCogn": "N"
}
```

## 📝 Regras de Validação

### Pessoa
- **Nome:** Obrigatório, mínimo 2 palavras, cada palavra com pelo menos 2 caracteres
- **Telefone:** Obrigatório, 10-15 dígitos numéricos
- **Tipo Pessoa:** Obrigatório, valores: `CUIDADOR`, `MEDICO`, `PACIENTE`
- **Email:** Opcional, formato de email válido
- **CPF:** Opcional, exatamente 11 dígitos
- **Gênero:** Opcional, valores: `F` (Feminino), `M` (Masculino), `O` (Outro)

### Paciente
- **Cartão SUS:** Obrigatório, exatamente 15 dígitos numéricos
- **Médico Responsável:** Obrigatório, ID de médico existente

### Info Teleconsulta
- **Habilidade Digital:** Obrigatório, valores: `BAIXA`, `MEDIA`, `ALTA`, `NENHUMA`
- **Canal Lembrete:** Obrigatório, valores: `WHATSAPP`, `SMS`, `EMAIL`, `TELEFONE`
- **Precisa Cuidador:** Obrigatório, valores: `S`, `N`
- **Já Fez Teleconsulta:** Obrigatório, valores: `S`, `N`

### Perfil Cognitivo
Todos os campos são opcionais, aceitam valores: `S`, `N`

## 🔧 Endpoints de Consulta

### Buscar Paciente Completo
**Endpoint:** `GET /pacientes-completo/{idPaciente}`

### Buscar Paciente Básico
**Endpoint:** `GET /pacientes/{idPessoa}`

### Listar Todos os Pacientes
**Endpoint:** `GET /pacientes`

### Buscar Pacientes por Médico
**Endpoint:** `GET /pacientes/medico/{idMedico}`

## 🛠️ Códigos de Exemplo

### JavaScript/TypeScript
```javascript
// Cadastro completo de paciente
async function cadastrarPacienteCompleto(dadosPaciente) {
  try {
    const response = await fetch('/pacientes-completo', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dadosPaciente)
    });
    
    if (response.status === 201) {
      return await response.json();
    } else {
      const error = await response.text();
      throw new Error(`Erro no cadastro: ${error}`);
    }
  } catch (error) {
    console.error('Erro:', error);
    throw error;
  }
}

// Exemplo de uso
const paciente = {
  pessoa: {
    nome: "Maria Oliveira",
    email: "maria.oliveira@email.com",
    cpf: "98765432100",
    dataNascimento: "1975-08-20",
    genero: "F",
    telefone: "11988887777",
    tipoPessoa: "PACIENTE"
  },
  paciente: {
    idMedicoResponsavel: 1,
    nrCartaoSUS: "987654321098765"
  },
  infoTeleconsulta: {
    cdHabilidadeDigital: "ALTA",
    cdCanalLembrete: "EMAIL",
    inPrecisaCuidador: "S",
    inJaFezTele: "N"
  },
  perfilCognitivo: {
    inDificuldadeVisao: "S",
    inUsaOculos: "S",
    inDificuldadeAudicao: "N",
    inUsaAparelhoAud: "N",
    inDificuldadeCogn: "N"
  }
};

cadastrarPacienteCompleto(paciente)
  .then(response => console.log('Paciente cadastrado:', response))
  .catch(error => console.error('Falha no cadastro:', error));
```

## ⚠️ Tratamento de Erros

### Códigos de Status HTTP
- `201 Created`: Cadastro realizado com sucesso
- `400 Bad Request`: Dados inválidos ou validação falhou
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro interno do servidor

### Exemplo de Resposta de Erro
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Cartão SUS deve conter 15 dígitos numéricos.",
  "path": "/pacientes-completo"
}
```

## 💡 Dicas para o Front-end

1. **Validação Client-side:** Implemente validações básicas antes de enviar para a API
2. **Feedback ao Usuário:** Mostre mensagens claras de sucesso/erro
3. **Loading States:** Exiba indicadores de carregamento durante as requisições
4. **Formulários:** Agrupe campos relacionados e use validação em tempo real
5. **Fallback:** Tenha um plano para quando o endpoint completo falhar (cadastro em etapas)

## 🔄 Fluxo Recomendado

1. Coletar dados da pessoa (obrigatório)
2. Coletar dados do paciente (obrigatório)
3. Coletar informações de teleconsulta (opcional)
4. Coletar perfil cognitivo (opcional)
5. Enviar tudo em uma única requisição para `/pacientes-completo`
6. Se falhar, tentar cadastro em etapas

Este manual fornece todas as informações necessárias para implementar o cadastro de pacientes no front-end de forma eficiente e robusta.
