delete from indicador where sigla = 'PS';

update indicador set descricao = 'PRIMEIRO SERVIÇO APÓS O PARTO', 
definicao = 'O número de dias pós-parto no primeiro serviço é influenciado pelo período voluntário de espera (PVE). É o cálculo do número de dias do parto até o primeiro serviço para as vacas inseminadas. Para um intervalo entre partos de 12 meses esse período deve ser no máximo de 85 dias.' where sigla = 'DPPPS';

