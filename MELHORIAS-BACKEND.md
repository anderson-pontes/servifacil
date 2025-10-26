# Melhorias Implementadas - Backend e Banco de Dados

## 📋 Índice

1. [Banco de Dados](#banco-de-dados)
2. [Endpoints](#endpoints)
3. [Segurança](#segurança)
4. [Configurações](#configurações)

---

## 🗄️ Banco de Dados

### 1. Alteração da Coluna Profile_Photo

**Problema:** Coluna `Profile_Photo` era do tipo `TEXT` (máx 65KB), insuficiente para imagens em base64.

**Solução:**

```sql
USE dbServi_Facil;
ALTER TABLE tb_users MODIFY COLUMN Profile_Photo LONGTEXT;
```

**Arquivo:** `ServiFacil-BackEnd/fix-profile-photo-column.sql`

**Resultado:**

-   ✅ Suporta até 4GB de dados
-   ✅ Permite imagens em base64 sem limitação
-   ✅ Compatível com compressão de imagens

**Como Executar:**

```bash
cd ServiFacil-BackEnd
mysql -u servifacil_user -pxpto1661WIN < fix-profile-photo-column.sql
```

Ou use o script batch:

```bash
executar-fix-photo.bat
```

---

## 🔌 Endpoints

### 1. Novo Endpoint: Buscar Serviço por ID

**Endpoint:** `GET /api/services/{serviceId}`

**Arquivo:** `ServiceController.java`

```java
@GetMapping("/{serviceId}")
public ResponseEntity<EntityResponse<?>> getServiceById(@PathVariable int serviceId) {
    ServiceModel service = serviceService.getServiceById(serviceId);
    EntityResponse<?> getResponse = new EntityResponse<>(
        true,
        "Serviço carregado com sucesso!",
        service
    );
    return ResponseEntity.status(HttpStatus.OK).body(getResponse);
}
```

**Service:** `ServiceService.java`

```java
@Transactional
public ServiceModel getServiceById(int serviceId) {
    ServiceModel service = serviceRepository.findById(serviceId)
        .orElseThrow(() -> new ApiException("Serviço não encontrado!", HttpStatus.NOT_FOUND));
    return service;
}
```

**Funcionalidade:**

-   ✅ Retorna dados completos do serviço
-   ✅ Inclui informações do profissional (relacionamento JPA)
-   ✅ Inclui categoria do serviço
-   ✅ Tratamento de erro 404 se não encontrado

**Resposta:**

```json
{
    "success": true,
    "message": "Serviço carregado com sucesso!",
    "data": {
        "serviceId": 1,
        "title": "Instalação Elétrica",
        "price": 150.0,
        "details": "Serviço completo de instalação elétrica",
        "serviceStatus": "Ativo",
        "professional": {
            "userId": 2,
            "userName": "João Silva",
            "profession": "Eletricista",
            "profilePhoto": "base64...",
            "userType": "Profissional"
        },
        "category": {
            "categoryId": 1,
            "category": "Elétrica"
        }
    }
}
```

---

## 🔒 Segurança

### 1. Configuração de Segurança Atualizada

**Arquivo:** `SecurityConfig.java`

**Problema:** Todos os endpoints `/api/services/**` estavam bloqueados para profissionais apenas.

**Solução:** Separação por método HTTP

```java
.authorizeHttpRequests(auth -> auth
    // Endpoints públicos
    .requestMatchers("/api/users/login", "/api/users/register").permitAll()

    // Endpoints de LEITURA - Requerem autenticação (qualquer usuário)
    .requestMatchers(HttpMethod.GET,
        "/api/services/getall",
        "/api/services/filter/**",
        "/api/services/{serviceId}").authenticated()

    // Endpoints de ESCRITA - Apenas profissionais
    .requestMatchers(HttpMethod.POST, "/api/services/**").hasRole("PROFISSIONAL")
    .requestMatchers(HttpMethod.PATCH, "/api/services/**").hasRole("PROFISSIONAL")
    .requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("PROFISSIONAL")

    .requestMatchers("/api/appointments/service/{id}/{serviceId}").hasRole("PROFISSIONAL")
    .anyRequest().authenticated()
)
```

**Regras de Acesso:**

| Endpoint                          | Método | Acesso       |
| --------------------------------- | ------ | ------------ |
| `/api/users/login`                | POST   | Público      |
| `/api/users/register`             | POST   | Público      |
| `/api/services/getall`            | GET    | Autenticado  |
| `/api/services/filter/{category}` | GET    | Autenticado  |
| `/api/services/{serviceId}`       | GET    | Autenticado  |
| `/api/services/{id}`              | POST   | Profissional |
| `/api/services/{id}/{serviceId}`  | PATCH  | Profissional |
| `/api/services/{id}/{serviceId}`  | DELETE | Profissional |

**Benefícios:**

-   ✅ Mais granular e seguro
-   ✅ Permite leitura para usuários autenticados
-   ✅ Restringe escrita apenas para profissionais
-   ✅ Segue princípio do menor privilégio

---

## ⚙️ Configurações

### 1. Limites de Upload

**Arquivo:** `application.properties`

**Adicionado:**

```properties
# Configurações para upload de arquivos (base64)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
server.tomcat.max-http-form-post-size=10MB
```

**Funcionalidade:**

-   ✅ Permite uploads de até 10MB
-   ✅ Suporta imagens em base64
-   ✅ Configuração do Tomcat ajustada

### 2. Configuração Completa

**Arquivo:** `application.properties`

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

---

## 📝 Modelos Atualizados

### 1. UserModel

**Arquivo:** `UserModel.java`

**Alteração:**

```java
@Column(name = "Profile_Photo", columnDefinition = "LONGTEXT")
private String profilePhoto;
```

### 2. EditUserDTO

**Arquivo:** `EditUserDTO.java`

**Alteração:**

```java
@Column(name = "Profile_Photo", columnDefinition = "LONGTEXT")
private String profilePhoto;
```

---

## 🔄 Fluxo de Dados

### Upload de Foto de Perfil

```
Frontend (React)
    ↓
Compressão (200x200, qualidade 0.3-0.4)
    ↓
Base64 String (~10-30 KB)
    ↓
API POST /users/register
    ↓
Backend (Spring Boot)
    ↓
Validação e Processamento
    ↓
Banco de Dados (MySQL)
    ↓
Coluna LONGTEXT (até 4GB)
    ↓
✅ Foto Salva
```

### Busca de Serviço

```
Frontend (React)
    ↓
GET /api/services/{serviceId}
    ↓
Backend (Spring Boot)
    ↓
ServiceService.getServiceById()
    ↓
ServiceRepository.findById()
    ↓
JPA carrega relacionamentos
    ↓
Retorna ServiceModel completo
    ↓
✅ Dados do Serviço + Profissional
```

---

## 📊 Resumo das Melhorias

### Banco de Dados

-   ✅ Coluna `Profile_Photo` alterada para LONGTEXT
-   ✅ Suporte para imagens maiores em base64

### Endpoints

-   ✅ Novo endpoint `GET /api/services/{serviceId}`
-   ✅ Retorna dados completos do serviço
-   ✅ Inclui informações do profissional

### Segurança

-   ✅ Separação de regras por método HTTP
-   ✅ Leitura para usuários autenticados
-   ✅ Escrita apenas para profissionais

### Configurações

-   ✅ Limites de upload aumentados (10MB)
-   ✅ Configuração do Tomcat ajustada
-   ✅ Suporte para base64

---

## 🚀 Como Aplicar as Melhorias

### 1. Banco de Dados

```bash
cd ServiFacil-BackEnd
mysql -u servifacil_user -pxpto1661WIN < fix-profile-photo-column.sql
```

### 2. Backend

```bash
# As alterações já estão no código
# Basta reiniciar o servidor Spring Boot
```

### 3. Verificação

```bash
# Verificar se a coluna foi alterada
mysql -u servifacil_user -psenha_do_banco -e "
USE dbServi_Facil;
DESCRIBE tb_users;
"
```

---

## 📚 Arquivos Modificados

### Backend

1. `ServiceController.java` - Novo endpoint
2. `ServiceService.java` - Novo método
3. `SecurityConfig.java` - Regras de segurança
4. `application.properties` - Limites de upload
5. `UserModel.java` - Tipo da coluna
6. `EditUserDTO.java` - Tipo da coluna

### Scripts SQL

1. `fix-profile-photo-column.sql` - Alteração da coluna
2. `executar-fix-photo.bat` - Script de execução

---

## ⚠️ Observações Importantes

1. **Backup:** Faça backup do banco antes de executar o script SQL
2. **Reiniciar:** Reinicie o backend após as alterações
3. **Autenticação:** Usuários precisam estar logados para acessar serviços
4. **Token JWT:** O token é enviado automaticamente pelo Axios
5. **CORS:** Já está configurado para aceitar requisições do frontend

---

## 🐛 Troubleshooting

### Erro 403 Forbidden

**Causa:** Usuário não autenticado ou sem permissão
**Solução:** Fazer login e verificar tipo de usuário

### Erro ao salvar foto

**Causa:** Coluna ainda é TEXT
**Solução:** Executar o script SQL de alteração

### Serviço não encontrado

**Causa:** ServiceId inválido ou serviço não existe
**Solução:** Verificar se o serviço existe no banco

---

## 📞 Suporte

Para mais informações, consulte:

-   `README.md` - Guia completo de execução
-   `TESTE-RAPIDO.md` - Guia de testes
-   `RESUMO-CORRECOES.md` - Resumo de todas as correções
