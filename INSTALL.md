# Guia Rápido de Instalação - OdontoAgenda

## 🚀 Início Rápido

### 1. Pré-requisitos
- ✅ JDK 17 instalado
- ✅ Node.js 18+ instalado
- ✅ PostgreSQL 12+ instalado e rodando
- ✅ Maven 3.8+ instalado

### 2. Configurar Banco de Dados

```bash
# No terminal PostgreSQL
createdb odontoagenda

# Ou via psql
psql -U postgres
CREATE DATABASE odontoagenda;
\q
```

### 3. Backend

```bash
cd backend

# Editar application.properties se necessário
# Configurar username/password do PostgreSQL

# Executar script SQL inicial
psql -U postgres -d odontoagenda -f src/main/resources/init.sql

# Compilar e executar
mvn clean install
mvn spring-boot:run
```

Backend estará rodando em: **http://localhost:8080**

### 4. Frontend

```bash
cd frontend

# Instalar dependências
npm install

# Executar em desenvolvimento
npm run dev
```

Frontend estará rodando em: **http://localhost:5173**

### 5. Acessar Sistema

Abra o navegador em: http://localhost:5173

**Credenciais padrão (Admin):**
- Email: `admin@odontoagenda.com`
- Senha: `admin123`

## 📋 Comandos Úteis

### Backend
```bash
# Compilar
mvn clean compile

# Rodar testes
mvn test

# Gerar JAR
mvn clean package

# Executar JAR
java -jar target/odontoagenda-backend-1.0.0.jar
```

### Frontend
```bash
# Desenvolvimento
npm run dev

# Build produção
npm run build

# Preview build
npm run preview

# Lint
npm run lint
```

## 🔧 Troubleshooting

### Backend não inicia
- Verifique se o PostgreSQL está rodando
- Confirme as credenciais em application.properties
- Verifique se a porta 8080 está livre

### Frontend não conecta no backend
- Certifique-se que o backend está rodando
- Verifique o proxy no vite.config.ts
- Limpe cache: `npm run dev -- --force`

### Erro de CORS
- Verifique se a origem está configurada em application.properties
- Reinicie o backend após alterar configurações

## 📦 Estrutura de Arquivos Importantes

```
OdontoAgenda/
├── backend/
│   ├── pom.xml                          # Dependências Maven
│   ├── src/main/resources/
│   │   ├── application.properties       # Configurações
│   │   └── init.sql                     # Script inicial BD
│   └── src/main/java/com/odontoagenda/
│       ├── OdontoAgendaApplication.java # Classe principal
│       ├── config/                      # Configurações
│       ├── controller/                  # REST Controllers
│       ├── service/                     # Lógica de negócio
│       ├── repository/                  # Acesso a dados
│       └── security/                    # JWT e Security
└── frontend/
    ├── package.json                     # Dependências NPM
    ├── vite.config.ts                   # Config Vite
    ├── src/
    │   ├── main.tsx                     # Entry point
    │   ├── App.tsx                      # Componente raiz
    │   ├── services/                    # API calls
    │   ├── contexts/                    # React contexts
    │   ├── components/                  # Componentes
    │   └── pages/                       # Páginas
    └── index.html
```

## 🎯 Próximos Passos

1. Altere a senha do administrador padrão
2. Configure o banner institucional
3. Cadastre especialidades
4. Cadastre dentistas
5. Teste o fluxo de agendamento

## 📞 Suporte

Para problemas ou dúvidas, consulte o README.md principal do projeto.
