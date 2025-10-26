-- ===================================
-- ServiFácil - Correção Profile Photo
-- Script 03: Fix Profile Photo Column
-- ===================================

USE dbServi_Facil;

-- Verificar se a tabela existe
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN 'Tabela tb_users encontrada'
        ELSE 'ERRO: Tabela tb_users não existe. Inicie o backend primeiro!'
    END AS Status
FROM information_schema.tables 
WHERE table_schema = 'dbServi_Facil' 
AND table_name = 'tb_users';

-- Alterar coluna Profile_Photo de TEXT para LONGTEXT
ALTER TABLE tb_users 
MODIFY COLUMN Profile_Photo LONGTEXT;

-- Verificar alteração
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    CHARACTER_MAXIMUM_LENGTH,
    CASE 
        WHEN DATA_TYPE = 'longtext' THEN '✅ Coluna corrigida com sucesso!'
        ELSE '❌ Erro: Coluna ainda não é LONGTEXT'
    END AS Status
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'dbServi_Facil' 
AND TABLE_NAME = 'tb_users' 
AND COLUMN_NAME = 'Profile_Photo';

-- Mensagem final
SELECT 'Correção aplicada com sucesso!' AS Status;
SELECT 'Agora você pode fazer upload de fotos maiores' AS Observacao;
