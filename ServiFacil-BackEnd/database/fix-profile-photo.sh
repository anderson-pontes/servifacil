#!/bin/bash

echo "========================================"
echo "ServiFácil - Correção Profile Photo"
echo "========================================"
echo ""

echo "Aplicando correção na coluna Profile_Photo..."
mysql -u servifacil_user -pxpto1661WIN < 03-fix-profile-photo.sql

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "✅ Correção aplicada com sucesso!"
    echo "Agora você pode fazer upload de fotos maiores"
    echo "========================================"
else
    echo ""
    echo "========================================"
    echo "❌ ERRO ao aplicar correção!"
    echo "Verifique se o backend foi iniciado e criou as tabelas"
    echo "========================================"
fi

echo ""
