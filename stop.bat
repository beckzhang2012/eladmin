@echo off
for /f "tokens=5" %%i in ('netstat -ano ^| findstr ":8080"') do taskkill /f /pid %%i
echo 应用程序已停止
pause
