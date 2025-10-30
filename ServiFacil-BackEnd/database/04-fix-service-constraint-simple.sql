-- ===================================
-- ServiFácil - Correção Constraint Serviços
-- Script 04: Fix Service Unique Constraint (Simplificado)
-- ===================================

USE dbServi_Facil;

-- Remover foreign key da categoria
ALTER TABLE tb_services DROP FOREIGN KEY FKs8gaenjx1srncd8vhxk68v2ie;

-- Remover constraint UNIQUE
ALTER TABLE tb_services DROP INDEX UK5uorylykh5ey64e9ardca76bw;

-- Recriar foreign key SEM UNIQUE
ALTER TABLE tb_services 
ADD CONSTRAINT FKs8gaenjx1srncd8vhxk68v2ie 
FOREIGN KEY (Category_ID) 
REFERENCES tb_categories(Category_ID);

-- Verificar
SELECT 'Correcao aplicada com sucesso!' AS Status;
SELECT 'Reinicie o backend agora' AS Acao;
