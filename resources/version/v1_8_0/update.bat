copy .\1.8.0\MilkMoney.jar .\app\MilkMoney.jar
.\database\bin\mysql.exe -u root --database=milkMoney -e "update sistema set versao = '1.8.0'"