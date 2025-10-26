# 🗄️ Scripts de Banco de Dados - ServiFácil

## 📋 Ordem de Execução

Execute os scripts na seguinte ordem:

### 1. Configuração Inicial

```bash
mysql -u root -p < 01-database-setup.sql
```

### 2. Stored Procedures

```bash
mysql -u servifacil_user -pxpto1661WIN < 02-create-procedures.sql
```

### 3. Correções e Melhorias

```bash
mysql -u servifacil_user -pxpto1661WIN < 03-fix-profile-photo.sql
```

---

## 📁 Descrição dos Scripts

### 01-database-setup.sql

**Propósito:** Configuração inicial do banco de dados

-   Cria o banco `dbServi_Facil`
-   Cria o usuário `servifacil_user`
-   Concede permissões
-   Cria tabelas principais

**Quando executar:** Primeira instalação

---

### 02-create-procedures.sql

**Propósito:** Cria todas as stored procedures necessárias

-   Procedures de usuários (insert, update)
-   Procedures de serviços
-   Procedures de agendamentos
-   Procedures de avaliações

**Quando executar:** Após criar o banco

---

### 03-fix-profile-photo.sql

**Propósito:** Corrige a coluna de foto de perfil

-   Altera `Profile_Photo` de TEXT para LONGTEXT
-   Permite imagens maiores em base64

**Quando executar:** Após criar as tabelas

---

## 🚀 Instalação Rápida

### Windows

```bash
cd ServiFacil-BackEnd/database
install-database.bat
```

### Linux/Mac

```bash
cd ServiFacil-BackEnd/database
chmod +x install-database.sh
./install-database.sh
```

---

## 🔧 Comandos Úteis

### Verificar Banco

```sql
SHOW DATABASES;
USE dbServi_Facil;
SHOW TABLES;
```

### Verificar Procedures

```sql
SHOW PROCEDURE STATUS WHERE Db = 'dbServi_Facil';
```

### Verificar Coluna Profile_Photo

```sql
DESCRIBE tb_users;
```

### Backup do Banco

```bash
mysqldump -u servifacil_user -pxpto1661WIN dbServi_Facil > backup.sql
```

### Restaurar Backup

```bash
mysql -u servifacil_user -pxpto1661WIN dbServi_Facil < backup.sql
```

---

## ⚠️ Observações

1. **Credenciais Padrão:**

    - Usuário: `servifacil_user`
    - Senha: `xpto1661WIN`
    - Banco: `dbServi_Facil`

2. **Alterações de Senha:**
   Se alterar a senha, atualize também:

    - `application.properties`
    - Scripts de instalação

3. **Backup:**
   Sempre faça backup antes de executar scripts de alteração

4. **Ordem:**
   Execute os scripts na ordem indicada

---

## 🐛 Troubleshooting

### Erro: Access denied

```sql
-- Recriar usuário
DROP USER IF EXISTS 'servifacil_user'@'localhost';
CREATE USER 'servifacil_user'@'localhost' IDENTIFIED BY 'xpto1661WIN';
GRANT ALL PRIVILEGES ON dbServi_Facil.* TO 'servifacil_user'@'localhost';
FLUSH PRIVILEGES;
```

### Erro: Database exists

```sql
-- Remover banco existente (CUIDADO!)
DROP DATABASE IF EXISTS dbServi_Facil;
-- Depois execute 01-database-setup.sql novamente
```

### Erro: Procedure already exists

```sql
-- Remover procedures existentes
DROP PROCEDURE IF EXISTS spInsertUser;
DROP PROCEDURE IF EXISTS spUpdateUser;
-- Depois execute 02-create-procedures.sql novamente
```

---

## 📚 Documentação Adicional

-   [README.md](../../README.md) - Guia completo do projeto
-   [MELHORIAS-BACKEND.md](../../MELHORIAS-BACKEND.md) - Melhorias implementadas
