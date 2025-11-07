# Manual para Cadastro de Paciente - Front-end

## 📋 Visão Geral
Este manual descreve os endpoints e formatos de dados necessários para realizar o cadastro completo de um paciente no sistema Auramed.

## 🔐 Autenticação
Todos os endpoints (exceto login) requerem autenticação via Bearer Token no header:
```http
Authorization: Bearer seu-token-jwt
```

## 🚀 Endpoints Principais

### 1. 🔑 Login do Médico
**Endpoint:** `POST /auth/login`

**Request:**
```json
{
  "email": "medico@exemplo.com",
  "senha": "senha123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. 📝 Cadastro Completo de Paciente (Recomendado)

**Endpoint:** `POST /pacientes-completo`

**Descrição:** Cria um paciente com todas as informações em uma única requisição.

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

### 3. 📋 Listar Todos os Pacientes do Médico Logado

**Endpoint:** `GET /pacientes-completo`

**Descrição:** Retorna todos os pacientes do médico logado com dados completos.

**Response:**
```json
[
  {
    "pessoa": {
      "id": 1,
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
      "idPessoa": 1,
      "idMedicoResponsavel": 5,
      "nrCartaoSUS": "123456789012345",
      "dataCadastro": "2024-01-15T10:30:00",
      "ativo": "S"
    },
    "infoTeleconsulta": {
      "idInfoTeleconsulta": 1,
      "idPaciente": 1,
      "cdHabilidadeDigital": "MEDIA",
      "cdCanalLembrete": "WHATSAPP",
      "inPrecisaCuidador": "N",
      "inJaFezTele": "S",
      "dataCadastro": "2024-01-15T10:30:00",
      "dataAtualizacao": "2024-01-15T10:30:00"
    },
    "perfilCognitivo": {
      "idPerfilCognitivo": 1,
      "idPaciente": 1,
      "inDificuldadeVisao": "N",
      "inUsaOculos": "S",
      "inDificuldadeAudicao": "N",
      "inUsaAparelhoAud": "N",
      "inDificuldadeCogn": "N",
      "dataCadastro": "2024-01-15T10:30:00",
      "dataAtualizacao": "2024-01-15T10:30:00"
    }
  }
]
```

### 4. 🔍 Buscar Paciente Específico

**Endpoint:** `GET /pacientes-completo/{idPaciente}`

**Descrição:** Busca um paciente específico por ID com todos os dados.

**Response:** Igual ao cadastro, mas para um paciente específico.

## ✏️ Endpoints de Atualização (PUT)

### 5. Atualizar Dados da Pessoa
**Endpoint:** `PUT /pessoas/{id}`

```json
{
  "nome": "João da Silva Santos Atualizado",
  "email": "joao.novo@email.com",
  "cpf": "12345678901",
  "dataNascimento": "1980-05-15",
  "genero": "M",
  "telefone": "11999998888",
  "tipoPessoa": "PACIENTE"
}
```

### 6. Atualizar Dados do Paciente
**Endpoint:** `PUT /pacientes/{idPessoa}`

```json
{
  "idMedicoResponsavel": 1,
  "nrCartaoSUS": "123456789012345"
}
```

### 7. Atualizar Info Teleconsulta
**Endpoint:** `PUT /info-teleconsulta/{idInfoTeleconsulta}`

```json
{
  "cdHabilidadeDigital": "ALTA",
  "cdCanalLembrete": "EMAIL",
  "inPrecisaCuidador": "S",
  "inJaFezTele": "N"
}
```

### 8. Atualizar Perfil Cognitivo
**Endpoint:** `PUT /perfil-cognitivo/{idPerfilCognitivo}`

```json
{
  "inDificuldadeVisao": "S",
  "inUsaOculos": "S",
  "inDificuldadeAudicao": "N",
  "inUsaAparelhoAud": "N",
  "inDificuldadeCogn": "S"
}
```

## 🗑️ Endpoints de Exclusão (DELETE)

### 9. Excluir Paciente Completo
**Endpoint:** `DELETE /pacientes/{idPessoa}`

**Descrição:** Remove o paciente e todas as informações relacionadas (cascata).

**Response:** `204 No Content`

### 10. Excluir Info Teleconsulta
**Endpoint:** `DELETE /info-teleconsulta/{idInfoTeleconsulta}`

**Response:** `204 No Content`

### 11. Excluir Perfil Cognitivo
**Endpoint:** `DELETE /perfil-cognitivo/{idPerfilCognitivo}`

**Response:** `204 No Content`

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
- **Habilidade Digital:** Opcional, valores: `BAIXA`, `MEDIA`, `ALTA`
- **Canal Lembrete:** Opcional, valores: `WHATSAPP`, `SMS`, `EMAIL`, `TELEFONE`
- **Precisa Cuidador:** Opcional, valores: `S`, `N`
- **Já Fez Teleconsulta:** Opcional, valores: `S`, `N`

### Perfil Cognitivo
- Todos os campos são opcionais, aceitam valores: `S`, `N`

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
        'Authorization': 'Bearer ' + localStorage.getItem('token')
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

// Listar pacientes do médico
async function listarPacientes() {
  try {
    const response = await fetch('/pacientes-completo', {
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    });
    
    if (response.ok) {
      return await response.json();
    } else {
      throw new Error('Erro ao listar pacientes');
    }
  } catch (error) {
    console.error('Erro:', error);
    throw error;
  }
}

// Atualizar paciente
async function atualizarPaciente(idPessoa, dadosAtualizados) {
  try {
    const response = await fetch(`/pacientes/${idPessoa}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      },
      body: JSON.stringify(dadosAtualizados)
    });
    
    if (response.ok) {
      return await response.json();
    } else {
      const error = await response.text();
      throw new Error(`Erro na atualização: ${error}`);
    }
  } catch (error) {
    console.error('Erro:', error);
    throw error;
  }
}

// Excluir paciente
async function excluirPaciente(idPessoa) {
  try {
    const response = await fetch(`/pacientes/${idPessoa}`, {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    });
    
    if (response.status !== 204) {
      throw new Error('Erro ao excluir paciente');
    }
  } catch (error) {
    console.error('Erro:', error);
    throw error;
  }
}
```

## ⚠️ Tratamento de Erros

### Códigos de Status HTTP
- `200 OK`: Requisição bem-sucedida
- `201 Created`: Cadastro realizado com sucesso
- `204 No Content`: Exclusão realizada com sucesso
- `400 Bad Request`: Dados inválidos ou validação falhou
- `401 Unauthorized`: Token inválido ou não fornecido
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
5. **Fallback:** Tenha um plano para quando o endpoint completo falhar

## 🔄 Fluxo Recomendado

1. **Login** → Obter token JWT
2. **Cadastrar** → Usar `/pacientes-completo` para criar paciente
3. **Listar** → Usar `/pacientes-completo` para ver todos os pacientes
4. **Buscar** → Usar `/pacientes-completo/{id}` para detalhes específicos
5. **Atualizar** → Usar endpoints PUT específicos
6. **Excluir** → Usar DELETE no paciente (remove em cascata)

## 🔒 Observações de Segurança

- O campo `idMedicoResponsavel` no cadastro é **ignorado** e substituído automaticamente pelo ID do médico logado
- Médicos só podem acessar/editar/excluir seus próprios pacientes
- Todas as operações são validadas contra o médico logado

Este manual fornece todas as informações necessárias para implementar o CRUD completo de pacientes no front-end de forma eficiente e segura.