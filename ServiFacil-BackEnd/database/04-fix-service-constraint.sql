-- ===================================
-- ServiFácil - Correção Constraint Serviços
-- Script 04: Fix Service Unique Constraint
-- ===================================

USE dbServi_Facil;

-- Verificar constraints da tabela tb_services
SELECT 
    'Constraints atuais:' AS Info,
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'dbServi_Facil' 
AND TABLE_NAME = 'tb_services';

-- Verificar foreign keys
SELECT 
    'Foreign Keys:' AS Info,
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'dbServi_Facil' 
AND TABLE_NAME = 'tb_services'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- Remover foreign key da categoria (se existir)
ALTER TABLE tb_services DROP FOREIGN KEY IF EXISTS FKs8gaenjx1srncd8vhxk68v2ie;

-- Remover constraint UNIQUE na coluna Category_ID
ALTER TABLE tb_services DROP INDEX IF EXISTS UK5uorylykh5ey64e9ardca76bw;

-- Recriar foreign key sem UNIQUE
ALTER TABLE tb_services 
ADD CONSTRAINT FKs8gaenjx1srncd8vhxk68v2ie 
FOREIGN KEY (Category_ID) 
REFERENCES tb_categories(Category_ID);

-- Verificar se foi corrigida
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN 'Constraint UNIQUE removida com sucesso!'
        ELSE 'Constraint UNIQUE ainda existe'
    END AS Status
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'dbServi_Facil' 
AND TABLE_NAME = 'tb_services'
AND CONSTRAINT_NAME = 'UK5uorylykh5ey64e9ardca76bw';

-- Mensagem final
SELECT 'Correcao aplicada!' AS Status;
SELECT 'IMPORTANTE: Reinicie o backend agora' AS Acao;
