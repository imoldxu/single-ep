rem *******************************Code Start*****************************
@echo off
set "Ymd=%date:~,4%%date:~5,2%%date:~8,2%"
C:\MySQL\bin\mysqldump --opt -u root --password=jxfy0125 ep > D:\db_backup\ep_%Ymd%.sql
@echo on
echo "备份完成"
rem *******************************Code End*****************************