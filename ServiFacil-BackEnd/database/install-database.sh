#!/bin/bash

echo "========================================"
echo "ServiFácil - Instalação do Banco de Dados"
echo "========================================"
echo ""

echo "[1/3] Criando banco de dados e usuário..."
mysql -u root -p < 01-database-setup.sql
if [ $? -ne 0 ]; then
    echo "ERRO ao criar banco de dados!"
    exit 1
fi
echo "✅ Banco criado com sucesso!"
echo ""

echo "[2/3] Criando procedures..."
mysql -u servifacil_user -pxpto1661WIN < 02-create-procedures.sql
if [ $? -ne 0 ]; then
    echo "ERRO ao criar procedures!"
    exit 1
fi
echo "✅ Procedures criadas com sucesso!"
echo ""

echo "========================================"
echo "IMPORTANTE: Inicie o backend agora para criar as tabelas"
echo "Depois execute: ./fix-profile-photo.sh"
echo "========================================"
echo ""
