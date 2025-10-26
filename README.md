# 🛠️ ServiFácil - Guia Completo de Instalação e Execução

Sistema completo de gerenciamento de serviços profissionais com frontend React e backend Spring Boot.

---

## 📋 Índice

1. [Pré-requisitos](#pré-requisitos)
2. [Instalação do Banco de Dados](#instalação-do-banco-de-dados)
3. [Configuração do Backend](#configuração-do-backend)
4. [Configuração do Frontend](#configuração-do-frontend)
5. [Executando o Sistema](#executando-o-sistema)
6. [Testes](#testes)
7. [Troubleshooting](#troubleshooting)

---

## 🔧 Pré-requisitos

### Software Necessário

| Software     | Versão Mínima | Download                                                          |
| ------------ | ------------- | ----------------------------------------------------------------- |
| **Java JDK** | 17+           | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Maven**    | 3.8+          | [Apache Maven](https://maven.apache.org/download.cgi)             |
| **Node.js**  | 18+           | [Node.js](https://nodejs.org/)                                    |
| **MySQL**    | 8.0+          | [MySQL](https://dev.mysql.com/downloads/mysql/)                   |
| **Git**      | 2.0+          | [Git](https://git-scm.com/downloads)                              |

### Verificar Instalações

```bash
# Java
java -version

# Maven
mvn -version

# Node.js e npm
node -v
npm -v

# MySQL
mysql --version

# Git
git --version
```

---

## 🗄️ Instalação do Banco de Dados

> **📁 Scripts Organizados:** Todos os scripts SQL estão em `ServiFacil-BackEnd/database/`

### Método Automático (Recomendado) ⚡

#### Windows

```bash
cd ServiFacil-BackEnd/database
install-database.bat
```

#### Linux/Mac

```bash
cd ServiFacil-BackEnd/database
chmod +x install-database.sh
./install-database.sh
```

> **Importante:** Após executar, inicie o backend para criar as tabelas, depois execute o script de correção da foto.

---

### Método Manual (Passo a Passo) 📝

#### Passo 1: Criar Banco e Usuário

```bash
cd ServiFacil-BackEnd/database
mysql -u root -p < 01-database-setup.sql
```

<details>
<summary>Ou execute manualmente no MySQL</summary>

```bash
mysql -u root -p
```

```sql
CREATE DATABASE dbServi_Facil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'servifacil_user'@'localhost' IDENTIFIED BY 'xpto1661WIN';
GRANT ALL PRIVILEGES ON dbServi_Facil.* TO 'servifacil_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

</details>

#### Passo 2: Criar Procedures (Opcional)

```bash
mysql -u servifacil_user -pxpto1661WIN < 02-create-procedures.sql
```

> **Nota:** As tabelas serão criadas automaticamente pelo Hibernate/JPA.

#### Passo 3: Iniciar o Backend

```bash
cd ServiFacil-BackEnd
mvn spring-boot:run
```

> ⏳ **Aguarde** o backend criar todas as tabelas (veja os logs do Spring Boot).

#### Passo 4: Aplicar Correção da Foto

Após as tabelas serem criadas:

**Windows:**

```bash
cd ServiFacil-BackEnd/database
fix-profile-photo.bat
```

**Linux/Mac:**

```bash
cd ServiFacil-BackEnd/database
chmod +x fix-profile-photo.sh
./fix-profile-photo.sh
```

<details>
<summary>Ou execute manualmente</summary>

```bash
mysql -u servifacil_user -pxpto1661WIN < 03-fix-profile-photo.sql
```

</details>

#### Passo 5: Verificar Instalação

```bash
mysql -u servifacil_user -pxpto1661WIN -e "
USE dbServi_Facil;
SHOW TABLES;
DESCRIBE tb_users;
"
```

**Resultado esperado:**

```
Profile_Photo | longtext | YES | | NULL |
```

---

## 🔙 Configuração do Backend

### 1. Clonar o Repositório (se necessário)

```bash
git clone <url-do-repositorio>
cd ServiFacil-BackEnd
```

### 2. Configurar application.properties

**Arquivo:** `src/main/resources/application.properties`

```properties
server.port=8081
spring.application.name=SF-BackEnd
spring.datasource.url=jdbc:mysql://localhost:3306/dbServi_Facil
spring.datasource.username=servifacil_user
spring.datasource.password=xpto1661WIN
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Configurações para upload de arquivos (base64)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
server.tomcat.max-http-form-post-size=10MB
```

### 3. Instalar Dependências

```bash
mvn clean install
```

### 4. Executar Backend

#### Opção 1: Via Maven

```bash
mvn spring-boot:run
```

#### Opção 2: Via IDE (IntelliJ/Eclipse)

1. Abrir o projeto na IDE
2. Localizar a classe principal (geralmente `Application.java`)
3. Clicar com botão direito → Run

#### Opção 3: Via JAR

```bash
mvn clean package
java -jar target/SF-BackEnd-0.0.1-SNAPSHOT.jar
```

### 5. Verificar Backend

Acesse: http://localhost:8081

**Teste de API:**

```bash
curl http://localhost:8081/api/services/getall
```

---

## 🎨 Configuração do Frontend

### 1. Navegar para o Frontend

```bash
cd ServiFacil-FrontEnd
```

### 2. Instalar Dependências

```bash
npm install
```

### 3. Configurar Variáveis de Ambiente (opcional)

**Arquivo:** `.env` (criar se não existir)

```env
VITE_API_URL=http://localhost:8081/api
```

### 4. Executar Frontend

```bash
npm run dev
```

### 5. Verificar Frontend

Acesse: http://localhost:5173

---

## 🚀 Executando o Sistema

### Ordem de Execução

1. **MySQL** (deve estar rodando)
2. **Backend** (Spring Boot)
3. **Frontend** (React + Vite)

### Script de Inicialização Completa

#### Windows (PowerShell)

```powershell
# Terminal 1 - Backend
cd ServiFacil-BackEnd
mvn spring-boot:run

# Terminal 2 - Frontend
cd ServiFacil-FrontEnd
npm run dev
```

#### Linux/Mac (Bash)

```bash
# Terminal 1 - Backend
cd ServiFacil-BackEnd
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd ServiFacil-FrontEnd
npm run dev
```

### Verificação Rápida

| Serviço      | URL                   | Status       |
| ------------ | --------------------- | ------------ |
| **Backend**  | http://localhost:8081 | ✅ Rodando   |
| **Frontend** | http://localhost:5173 | ✅ Rodando   |
| **MySQL**    | localhost:3306        | ✅ Conectado |

---

## 🧪 Testes

### 1. Teste de Cadastro

1. Acesse http://localhost:5173/cadastro
2. Preencha o formulário
3. Adicione uma foto (opcional)
4. Clique em "Criar Conta"

**Resultado esperado:**

-   ✅ Conta criada com sucesso
-   ✅ Redirecionamento para login
-   ✅ Foto salva no banco

### 2. Teste de Login

1. Acesse http://localhost:5173
2. Digite email e senha
3. Clique em "Entrar"

**Resultado esperado:**

-   ✅ Login bem-sucedido
-   ✅ Token JWT salvo
-   ✅ Redirecionamento para Home

### 3. Teste de Serviços

1. Faça login como profissional
2. Acesse o Dashboard
3. Crie um novo serviço
4. Visualize os detalhes

**Resultado esperado:**

-   ✅ Serviço criado
-   ✅ Dados do profissional aparecem
-   ✅ Avaliações funcionando

### 4. Teste de Agendamento

1. Faça login como cliente
2. Acesse a Home
3. Clique em um serviço
4. Clique em "Agendar Serviço"

**Resultado esperado:**

-   ✅ Modal abre
-   ✅ Formulário funciona
-   ✅ Agendamento criado

---

## 🐛 Troubleshooting

### Backend não inicia

**Erro:** `Port 8081 is already in use`

**Solução:**

```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8081 | xargs kill -9
```

---

**Erro:** `Access denied for user 'servifacil_user'`

**Solução:**

```sql
-- Recriar usuário
DROP USER 'servifacil_user'@'localhost';
CREATE USER 'servifacil_user'@'localhost' IDENTIFIED BY 'senha_do_banco';
GRANT ALL PRIVILEGES ON dbServi_Facil.* TO 'servifacil_user'@'localhost';
FLUSH PRIVILEGES;
```

---

**Erro:** `Table 'dbServi_Facil.tb_users' doesn't exist`

**Solução:**

```bash
# Deixar o Hibernate criar as tabelas
# Certifique-se que spring.jpa.hibernate.ddl-auto=update
# Reinicie o backend
```

---

### Frontend não inicia

**Erro:** `Cannot find module`

**Solução:**

```bash
# Limpar cache e reinstalar
rm -rf node_modules package-lock.json
npm install
```

---

**Erro:** `CORS policy`

**Solução:**
Verificar `SecurityConfig.java`:

```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
```

---

### Problemas com Foto

**Erro:** `Data too long for column 'Profile_Photo'`

**Solução:**

```bash
# Executar script de correção
cd ServiFacil-BackEnd
mysql -u servifacil_user -pxpto1661WIN < fix-profile-photo-column.sql
```

---

**Erro:** Foto não aparece

**Solução:**

1. Verificar console do navegador
2. Verificar tamanho da imagem (deve ser < 100KB)
3. Usar imagem menor ou de melhor qualidade

---

### Erro 403 Forbidden

**Causa:** Usuário não autenticado

**Solução:**

1. Fazer login
2. Verificar se o token está sendo enviado
3. Verificar tipo de usuário (Cliente/Profissional)

---

### Erro 404 Not Found

**Causa:** Endpoint não existe ou backend não está rodando

**Solução:**

1. Verificar se o backend está rodando
2. Verificar URL da API
3. Verificar logs do backend

---

## 📁 Estrutura do Projeto

```
ServiFacil/
├── ServiFacil-BackEnd/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/servifacil/SF_BackEnd/
│   │   │   │       ├── controllers/
│   │   │   │       ├── services/
│   │   │   │       ├── models/
│   │   │   │       ├── repositories/
│   │   │   │       ├── dto/
│   │   │   │       ├── config/
│   │   │   │       └── security/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── fix-profile-photo-column.sql
│   ├── executar-fix-photo.bat
│   └── pom.xml
│
├── ServiFacil-FrontEnd/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── hooks/
│   │   ├── utils/
│   │   └── App.jsx
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── README.md
├── MELHORIAS-BACKEND.md
├── TESTE-RAPIDO.md
└── RESUMO-CORRECOES.md
```

---

## 🔐 Credenciais Padrão

### Banco de Dados

-   **Host:** localhost
-   **Porta:** 3306
-   **Database:** dbServi_Facil
-   **Usuário:** servifacil_user
-   **Senha:** senha_do_banco

### Aplicação

-   **Backend:** http://localhost:8081
-   **Frontend:** http://localhost:5173

---

## 📚 Documentação Adicional

-   [MELHORIAS-BACKEND.md](MELHORIAS-BACKEND.md) - Detalhes das melhorias no backend
-   [TESTE-RAPIDO.md](TESTE-RAPIDO.md) - Guia rápido de testes
-   [RESUMO-CORRECOES.md](RESUMO-CORRECOES.md) - Resumo de todas as correções
-   [NOVO-LAYOUT-CADASTRO.md](NOVO-LAYOUT-CADASTRO.md) - Detalhes do novo layout

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

---

## 📝 Licença

Este projeto está sob a licença MIT.

---

## 👥 Equipe

-   **Backend:** Spring Boot + MySQL
-   **Frontend:** React + Vite + TailwindCSS
-   **Autenticação:** JWT

---

## 📞 Suporte

Para dúvidas ou problemas:

1. Consulte a documentação
2. Verifique os logs do backend e frontend
3. Abra uma issue no repositório

---

## ✅ Checklist de Instalação

-   [ ] Java JDK 17+ instalado
-   [ ] Maven instalado
-   [ ] Node.js 18+ instalado
-   [ ] MySQL 8.0+ instalado
-   [ ] Banco de dados criado
-   [ ] Script SQL executado
-   [ ] Backend rodando (porta 8081)
-   [ ] Frontend rodando (porta 5173)
-   [ ] Teste de cadastro realizado
-   [ ] Teste de login realizado

---

**Versão:** 1.0.0  
**Última atualização:** 2024

---

## 🚀 Deploy em Produção

### Preparação do Banco de Dados

#### 1. Servidor de Produção

```bash
# Conectar ao servidor MySQL de produção
mysql -h seu-servidor.com -u root -p
```

```sql
-- Criar banco
CREATE DATABASE dbServi_Facil CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Criar usuário com senha forte
CREATE USER 'servifacil_user'@'%' IDENTIFIED BY 'SENHA_FORTE_AQUI';

-- Conceder permissões
GRANT ALL PRIVILEGES ON dbServi_Facil.* TO 'servifacil_user'@'%';
FLUSH PRIVILEGES;
EXIT;
```

#### 2. Executar Scripts de Produção

```bash
# Upload dos scripts para o servidor
scp -r ServiFacil-BackEnd/database/ usuario@servidor:/opt/servifacil/

# No servidor
cd /opt/servifacil/database

# Executar scripts
mysql -h localhost -u servifacil_user -p < 01-database-setup.sql
mysql -h localhost -u servifacil_user -p < 02-create-procedures.sql
```

#### 3. Aguardar Backend Criar Tabelas

Após iniciar o backend pela primeira vez, execute:

```bash
mysql -h localhost -u servifacil_user -p < 03-fix-profile-photo.sql
```

---

### Configuração do Backend para Produção

#### 1. Criar application-prod.properties

**Arquivo:** `src/main/resources/application-prod.properties`

```properties
# Servidor
server.port=8081

# Banco de Dados de Produção
spring.datasource.url=jdbc:mysql://seu-servidor.com:3306/dbServi_Facil?useSSL=true&requireSSL=true
spring.datasource.username=servifacil_user
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=false

# Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
server.tomcat.max-http-form-post-size=10MB

# Logging
logging.level.root=INFO
logging.level.com.servifacil=INFO
logging.file.name=/var/log/servifacil/application.log
```

#### 2. Gerar JAR de Produção

```bash
cd ServiFacil-BackEnd

# Limpar e compilar
mvn clean package -DskipTests

# JAR gerado em:
# target/SF-BackEnd-0.0.1-SNAPSHOT.jar
```

#### 3. Deploy no Servidor

```bash
# Upload do JAR
scp target/SF-BackEnd-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/servifacil/

# Conectar ao servidor
ssh usuario@servidor

# Criar diretório de logs
sudo mkdir -p /var/log/servifacil
sudo chown servifacil:servifacil /var/log/servifacil
```

#### 4. Configurar como Serviço (Linux)

```bash
sudo nano /etc/systemd/system/servifacil.service
```

```ini
[Unit]
Description=ServiFacil Backend API
After=mysql.service network.target

[Service]
Type=simple
User=servifacil
WorkingDirectory=/opt/servifacil
Environment="DB_PASSWORD=SENHA_DO_BANCO"
Environment="SPRING_PROFILES_ACTIVE=prod"
ExecStart=/usr/bin/java -Xmx512m -Xms256m -jar /opt/servifacil/SF-BackEnd-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
StandardOutput=journal
StandardError=journal
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar inicialização automática
sudo systemctl enable servifacil

# Iniciar serviço
sudo systemctl start servifacil

# Verificar status
sudo systemctl status servifacil

# Ver logs
sudo journalctl -u servifacil -f
```

---

### Configuração do Frontend para Produção

#### 1. Configurar Variáveis de Ambiente

**Arquivo:** `.env.production`

```env
VITE_API_URL=https://api.servifacil.com.br/api
```

#### 2. Build de Produção

```bash
cd ServiFacil-FrontEnd

# Instalar dependências
npm install

# Gerar build
npm run build

# Arquivos gerados em: dist/
```

#### 3. Deploy com Nginx

```bash
# Copiar arquivos para servidor
scp -r dist/* usuario@servidor:/var/www/servifacil/

# Configurar Nginx
sudo nano /etc/nginx/sites-available/servifacil
```

```nginx
server {
    listen 80;
    server_name servifacil.com.br www.servifacil.com.br;

    root /var/www/servifacil;
    index index.html;

    # Frontend (React)
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Backend API (Proxy)
    location /api/ {
        proxy_pass http://localhost:8081/api/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
```

```bash
# Ativar site
sudo ln -s /etc/nginx/sites-available/servifacil /etc/nginx/sites-enabled/

# Testar configuração
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
```

#### 4. Configurar SSL (Let's Encrypt)

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx

# Obter certificado
sudo certbot --nginx -d servifacil.com.br -d www.servifacil.com.br

# Renovação automática
sudo certbot renew --dry-run
```

---

### Checklist de Deploy

#### Banco de Dados

-   [ ] MySQL instalado e configurado
-   [ ] Banco `dbServi_Facil` criado
-   [ ] Usuário `servifacil_user` criado
-   [ ] Scripts SQL executados
-   [ ] Coluna `Profile_Photo` corrigida (LONGTEXT)
-   [ ] Backup configurado

#### Backend

-   [ ] Java 17+ instalado
-   [ ] JAR compilado
-   [ ] `application-prod.properties` configurado
-   [ ] Variáveis de ambiente definidas
-   [ ] Serviço systemd configurado
-   [ ] Logs configurados
-   [ ] Firewall configurado (porta 8081)

#### Frontend

-   [ ] Build de produção gerado
-   [ ] Arquivos copiados para servidor
-   [ ] Nginx configurado
-   [ ] SSL/HTTPS configurado
-   [ ] DNS apontando para servidor

#### Segurança

-   [ ] Senhas fortes configuradas
-   [ ] Firewall ativo
-   [ ] SSL/HTTPS ativo
-   [ ] Backup automático configurado
-   [ ] Logs de acesso configurados

---

### Monitoramento

#### Logs do Backend

```bash
# Ver logs em tempo real
sudo journalctl -u servifacil -f

# Ver últimas 100 linhas
sudo journalctl -u servifacil -n 100

# Ver logs de hoje
sudo journalctl -u servifacil --since today
```

#### Logs do Nginx

```bash
# Access log
sudo tail -f /var/log/nginx/access.log

# Error log
sudo tail -f /var/log/nginx/error.log
```

#### Status dos Serviços

```bash
# Backend
sudo systemctl status servifacil

# MySQL
sudo systemctl status mysql

# Nginx
sudo systemctl status nginx
```

---

### Backup e Restauração

#### Backup do Banco

```bash
# Backup completo
mysqldump -u servifacil_user -p dbServi_Facil > backup_$(date +%Y%m%d).sql

# Backup com compressão
mysqldump -u servifacil_user -p dbServi_Facil | gzip > backup_$(date +%Y%m%d).sql.gz

# Backup automático (cron)
0 2 * * * /usr/bin/mysqldump -u servifacil_user -pSENHA dbServi_Facil | gzip > /backup/servifacil_$(date +\%Y\%m\%d).sql.gz
```

#### Restauração

```bash
# Restaurar backup
mysql -u servifacil_user -p dbServi_Facil < backup_20240101.sql

# Restaurar backup comprimido
gunzip < backup_20240101.sql.gz | mysql -u servifacil_user -p dbServi_Facil
```

---

### Atualizações

#### Atualizar Backend

```bash
# 1. Fazer backup do banco
mysqldump -u servifacil_user -p dbServi_Facil > backup_pre_update.sql

# 2. Parar serviço
sudo systemctl stop servifacil

# 3. Fazer backup do JAR atual
cp /opt/servifacil/SF-BackEnd-0.0.1-SNAPSHOT.jar /opt/servifacil/SF-BackEnd-0.0.1-SNAPSHOT.jar.bak

# 4. Copiar novo JAR
scp target/SF-BackEnd-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/servifacil/

# 5. Iniciar serviço
sudo systemctl start servifacil

# 6. Verificar logs
sudo journalctl -u servifacil -f
```

#### Atualizar Frontend

```bash
# 1. Gerar novo build
npm run build

# 2. Fazer backup dos arquivos atuais
ssh usuario@servidor "sudo cp -r /var/www/servifacil /var/www/servifacil.bak"

# 3. Copiar novos arquivos
scp -r dist/* usuario@servidor:/var/www/servifacil/

# 4. Limpar cache do Nginx
ssh usuario@servidor "sudo systemctl reload nginx"
```

---

## 📚 Scripts de Banco de Dados

Todos os scripts SQL estão organizados em `ServiFacil-BackEnd/database/`:

| Script                     | Descrição                         | Quando Usar              |
| -------------------------- | --------------------------------- | ------------------------ |
| `01-database-setup.sql`    | Cria banco e usuário              | Primeira instalação      |
| `02-create-procedures.sql` | Cria procedures                   | Após criar banco         |
| `03-fix-profile-photo.sql` | Corrige coluna foto               | Após criar tabelas       |
| `install-database.bat`     | Instalação automática (Windows)   | Desenvolvimento          |
| `install-database.sh`      | Instalação automática (Linux/Mac) | Desenvolvimento/Produção |
| `fix-profile-photo.bat`    | Correção automática (Windows)     | Desenvolvimento          |
| `fix-profile-photo.sh`     | Correção automática (Linux/Mac)   | Desenvolvimento/Produção |

**Documentação completa:** [database/README.md](ServiFacil-BackEnd/database/README.md)

---
