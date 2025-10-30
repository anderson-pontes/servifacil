-- ===================================
-- ServiFácil - Correção Final
-- ===================================

USE dbServi_Facil;

-- Remover TODAS as foreign keys relacionadas
ALTER TABLE tb_services DROP FOREIGN KEY FKnsui1eqbovebv32igl32gvy4c;
ALTER TABLE tb_services DROP FOREIGN KEY FKs8gaenjx1srncd8vhxk68v2ie;

-- Remover constraint UNIQUE
ALTER TABLE tb_services DROP INDEX UK5uorylykh5ey64e9ardca76bw;

-- Recriar foreign keys SEM UNIQUE
ALTER TABLE tb_services 
ADD CONSTRAINT FKnsui1eqbovebv32igl32gvy4c 
FOREIGN KEY (Professional_ID) 
REFERENCES tb_users(User_ID);

ALTER TABLE tb_services 
ADD CONSTRAINT FKs8gaenjx1srncd8vhxk68v2ie 
FOREIGN KEY (Category_ID) 
REFERENCES tb_categories(Category_ID);

SELECT 'Correcao aplicada!' AS Status;
