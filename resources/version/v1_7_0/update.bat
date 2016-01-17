copy .\1.7.0\MilkMoney.jar .\app\MilkMoney.jar

mkdir backup

.\database\bin\mysql.exe -u root --database=milkMoney -e "update sistema set versao = '1.7.0'"