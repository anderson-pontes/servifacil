@echo off
echo ========================================
echo ServiFacil - Correcao Constraint Servicos
echo ========================================
echo.

echo Verificando e corrigindo constraint problematica...
mysql -u servifacil_user -pxpto1661WIN < 04-fix-service-constraint.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Verificacao concluida!
    echo.
    echo IMPORTANTE:
    echo 1. Reinicie o backend
    echo 2. Tente criar o servico novamente
    echo ========================================
) else (
    echo.
    echo ========================================
    echo ERRO ao executar script!
    echo ========================================
)

echo.
pause
