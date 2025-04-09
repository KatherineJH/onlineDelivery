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
start cmd /k "docker compose up"

:: 3. Frontend (React) 실행
cd %BASE_PATH%\frontend\food-delivery-app
start cmd /k "npm run dev"

:: 4. Elasticsearch가 준비될 때까지 대기
echo Waiting for Elasticsearch to be ready...
:CHECK_ES
timeout /t 2 >nul
curl -s -o nul http://localhost:9200
if %ERRORLEVEL% neq 0 (
    echo Elasticsearch is not ready yet, waiting...
    goto CHECK_ES
)
echo Elasticsearch is ready!

:: 5. Spring Boot 실행
cd %BASE_PATH%\backend\OnlineDeliveryService
start cmd /k "gradlew bootRun"

echo All services started!
pause