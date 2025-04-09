@echo off
echo Starting Online Delivery Project...

:: 기본 경로 변수 설정
set BASE_PATH=C:\Users\BIT-N39\Desktop\BitEducationCentre\MiniProject2\onlineDelivery

:: 1. Flask API 실행
cd %BASE_PATH%\flaskApi
call venv\Scripts\activate
start cmd /k "pip install -r requirements.txt && python main.py"

:: 2. Docker Compose로 Elasticsearch와 Kibana 실행
cd %BASE_PATH%
start cmd /k docker compose up

:: 3. Spring Boot 실행
cd %BASE_PATH%\backend\OnlineDeliveryService
start cmd /k gradlew bootRun

:: 4. Frontend (React) 실행
cd %BASE_PATH%\frontend\food-delivery-app
start cmd /k npm run dev

echo All services started!
pause