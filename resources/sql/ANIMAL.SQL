INSERT INTO animal (numero, nome, dataNascimento, raca, sexo, finalidadeAnimal, peso, valor) 
VALUES (substr(ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000),8), concat('ANIMAL',substr(ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000),8)), current_date(), 1, 'FÊMEA', 'PRODUÇÃO DE LEITE', 0, 0);
