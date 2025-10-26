@echo off
echo ========================================
echo ServiFacil - Instalacao do Banco de Dados
echo ========================================
echo.

echo [1/3] Criando banco de dados e usuario...
mysql -u root -p < 01-database-setup.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao criar banco de dados!
    pause
    exit /b 1
)
echo OK - Banco criado com sucesso!
echo.

echo [2/3] Criando procedures...
mysql -u servifacil_user -pxpto1661WIN < 02-create-procedures.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERRO ao criar procedures!
    pause
    exit /b 1
)
echo OK - Procedures criadas com sucesso!
echo.

echo ========================================
echo IMPORTANTE: Inicie o backend agora para criar as tabelas
echo Depois execute: fix-profile-photo.bat
echo ========================================
echo.

pause
