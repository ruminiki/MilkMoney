delete from indicador where sigla = 'PS';

update indicador set descricao = 'PRIMEIRO SERVI�O AP�S O PARTO', 
definicao = 'O n�mero de dias p�s-parto no primeiro servi�o � influenciado pelo per�odo volunt�rio de espera (PVE). � o c�lculo do n�mero de dias do parto at� o primeiro servi�o para as vacas inseminadas. Para um intervalo entre partos de 12 meses esse per�odo deve ser no m�ximo de 85 dias.' where sigla = 'DPPPS';

