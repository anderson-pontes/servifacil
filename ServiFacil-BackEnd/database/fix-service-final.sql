USE dbServi_Facil;

-- Remover constraint UNIQUE (o problema principal)
ALTER TABLE tb_services DROP INDEX UK5uorylykh5ey64e9ardca76bw;

SELECT 'Constraint UNIQUE removida com sucesso!' AS Status;
SELECT 'Agora reinicie o backend' AS Acao;
