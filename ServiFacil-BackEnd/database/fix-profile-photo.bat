@echo off
echo ========================================
echo ServiFacil - Correcao Profile Photo
echo ========================================
echo.

echo Aplicando correcao na coluna Profile_Photo...
mysql -u servifacil_user -pxpto1661WIN < 03-fix-profile-photo.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Correcao aplicada com sucesso!
    echo Agora voce pode fazer upload de fotos maiores
    echo ========================================
) else (
    echo.
    echo ========================================
    echo ERRO ao aplicar correcao!
    echo Verifique se o backend foi iniciado e criou as tabelas
    echo ========================================
)

echo.
pause
