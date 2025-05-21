-- Script para criar o banco de dados e inserir dados iniciais
--CREATE USER tcc IDENTIFIED BY teste--banco sqldeveloper
--/
--grant dba to tcc
 --/
INSERT INTO USUARIO (ID, NOME_COMPLETO, EMAIL, LOGIN, SENHA, DATA_INCLUSAO)
VALUES (1,'Administrador', 'admin@email.com', 'admin', '1234', SYSDATE);
COMMIT;





