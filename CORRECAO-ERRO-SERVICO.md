# Correção: Erro ao Criar Serviço

## 🐛 Problema

**Erro:**

```
Duplicate entry '1' for key 'tb_services.UK5uorylykh5ey64e9ardca76bw'
```

**Causa:**
O modelo `ServiceModel.java` tinha um relacionamento `@OneToOne` com `CategoryModel`, criando uma constraint UNIQUE na coluna `Category_ID`. Isso impedia que múltiplos serviços tivessem a mesma categoria.

---

## ✅ Solução

### 1. Correção no Código

**Arquivo:** `ServiceModel.java`

**Antes (Incorreto):**

```java
@OneToOne
@JoinColumn(name = "Category_ID")
private CategoryModel category;
```

**Depois (Correto):**

```java
@ManyToOne
@JoinColumn(name = "Category_ID")
private CategoryModel category;
```

**Explicação:**

-   `@OneToOne` = Um serviço tem UMA categoria exclusiva (cria UNIQUE constraint)
-   `@ManyToOne` = Vários serviços podem ter a MESMA categoria (correto!)

---

### 2. Correção no Banco de Dados

Execute o script para remover a constraint UNIQUE:

**Windows:**

```bash
cd ServiFacil-BackEnd/database
fix-service-issue.bat
```

**Linux/Mac:**

```bash
cd ServiFacil-BackEnd/database
mysql -u servifacil_user -pxpto1661WIN < 04-fix-service-constraint.sql
```

**Ou manualmente:**

```sql
USE dbServi_Facil;
ALTER TABLE tb_services DROP INDEX IF EXISTS UK5uorylykh5ey64e9ardca76bw;
```

---

### 3. Reiniciar o Backend

```bash
cd ServiFacil-BackEnd
mvn spring-boot:run
```

> **Importante:** O Hibernate irá recriar a estrutura correta sem a constraint UNIQUE.

---

## 🔄 Fluxo de Correção

```
1. Parar o backend
   ↓
2. Executar script SQL (remover constraint)
   ↓
3. Código já corrigido (@ManyToOne)
   ↓
4. Reiniciar backend
   ↓
5. Hibernate recria estrutura correta
   ↓
6. ✅ Criar serviço funciona!
```

---

## 🧪 Como Testar

1. **Fazer login como profissional**
2. Acessar Dashboard
3. Clicar em "Novo Serviço"
4. Preencher formulário
5. Selecionar categoria (ex: "Elétrica")
6. Criar serviço
7. **Criar OUTRO serviço com a MESMA categoria**
8. ✅ Deve funcionar sem erro!

---

## 📊 Antes vs Depois

### Antes (Erro)

```
Serviço 1: Categoria "Elétrica" ✅
Serviço 2: Categoria "Elétrica" ❌ ERRO: Duplicate entry
```

### Depois (Correto)

```
Serviço 1: Categoria "Elétrica" ✅
Serviço 2: Categoria "Elétrica" ✅
Serviço 3: Categoria "Elétrica" ✅
Serviço 4: Categoria "Hidráulica" ✅
```

---

## 🗄️ Estrutura do Banco

### Relacionamentos Corretos

```
tb_categories (1) ←──── (N) tb_services
     ↑                        ↑
     │                        │
  1 categoria          N serviços
  (ex: Elétrica)      (podem ter a mesma categoria)
```

### Constraints

| Tabela      | Coluna          | Tipo | Constraint                  |
| ----------- | --------------- | ---- | --------------------------- |
| tb_services | Service_ID      | INT  | PRIMARY KEY, AUTO_INCREMENT |
| tb_services | Category_ID     | INT  | FOREIGN KEY (sem UNIQUE) ✅ |
| tb_services | Professional_ID | INT  | FOREIGN KEY                 |

---

## 📁 Arquivos Alterados

### Backend

-   `ServiceModel.java` - Alterado `@OneToOne` para `@ManyToOne`

### Scripts SQL

-   `04-fix-service-constraint.sql` - Remove constraint UNIQUE
-   `fix-service-issue.bat` - Script de correção (Windows)

---

## ⚠️ Observações

1. **Backup:** Sempre faça backup antes de alterar o banco
2. **Reiniciar:** Reinicie o backend após executar o script
3. **Verificar:** Teste criando múltiplos serviços com a mesma categoria
4. **Produção:** Se já estiver em produção, execute o script no servidor

---

## 🔍 Como Verificar se Está Corrigido

### Verificar Constraint no Banco

```sql
USE dbServi_Facil;

-- Verificar constraints
SELECT
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'dbServi_Facil'
AND TABLE_NAME = 'tb_services';

-- Não deve aparecer UK5uorylykh5ey64e9ardca76bw
```

### Verificar Código

```java
// Deve estar assim:
@ManyToOne
@JoinColumn(name = "Category_ID")
private CategoryModel category;
```

---

## 📚 Documentação Relacionada

-   [README.md](README.md) - Guia completo
-   [MELHORIAS-BACKEND.md](MELHORIAS-BACKEND.md) - Melhorias técnicas
-   [database/README.md](ServiFacil-BackEnd/database/README.md) - Scripts SQL

---

## ✅ Checklist de Correção

-   [ ] Código alterado (`@OneToOne` → `@ManyToOne`)
-   [ ] Script SQL executado
-   [ ] Backend reiniciado
-   [ ] Teste: Criar serviço com categoria "Elétrica"
-   [ ] Teste: Criar outro serviço com categoria "Elétrica"
-   [ ] ✅ Ambos criados com sucesso!

---

**Versão:** 1.0.0  
**Data:** 2024  
**Status:** ✅ Corrigido
