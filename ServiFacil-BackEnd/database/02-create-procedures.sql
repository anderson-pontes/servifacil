-- ===================================
-- ServiFácil - Stored Procedures
-- Script 02: Create Procedures
-- ===================================

USE dbServi_Facil;

-- ===================================
-- NOTA: As tabelas serão criadas automaticamente
-- pelo Hibernate/JPA quando o backend iniciar
-- ===================================

-- Este script está preparado para quando você
-- precisar criar procedures customizadas

-- Exemplo de procedure (descomente se necessário):
/*
DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS sp_exemplo()
BEGIN
    SELECT 'Procedure de exemplo' AS mensagem;
END$$

DELIMITER ;
*/

-- Mensagem de sucesso
SELECT 'Procedures criadas com sucesso!' AS Status;
SELECT 'Execute o próximo script: 03-fix-profile-photo.sql' AS ProximoPasso;
SELECT 'IMPORTANTE: Inicie o backend para criar as tabelas automaticamente' AS Observacao;
