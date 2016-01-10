copy .\1.6.0\MilkMoney.jar .\app\MilkMoney.jar
cd database/bin/

mysql.exe -u root --database=milkMoney -e "update sistema set versao = '1.6.0'"