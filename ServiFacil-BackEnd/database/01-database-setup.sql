-- ===================================
-- ServiFácil - Configuração Inicial
-- Script 01: Database Setup
-- ===================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS dbServi_Facil 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar o banco
USE dbServi_Facil;

-- Criar usuário (se não existir)
CREATE USER IF NOT EXISTS 'servifacil_user'@'localhost' IDENTIFIED BY 'xpto1661WIN';

-- Conceder permissões
GRANT ALL PRIVILEGES ON dbServi_Facil.* TO 'servifacil_user'@'localhost';
FLUSH PRIVILEGES;

-- Mensagem de sucesso
SELECT 'Banco de dados criado com sucesso!' AS Status;
SELECT 'Usuário servifacil_user criado com sucesso!' AS Status;
SELECT 'Execute o próximo script: 02-create-procedures.sql' AS ProximoPasso;
