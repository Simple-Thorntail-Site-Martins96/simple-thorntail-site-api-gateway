@echo off
@echo *** Start login module ***
@echo ** Start Backend server **
cd api-gtw
start "BACKEND Console" mvn clean thorntail:run -DskipTests
cd ..
@echo *** Starting env triggered, end batch ***