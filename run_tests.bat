@echo off

REM Detectar ubicación de ADB
set ADB=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe
if not exist "%ADB%" (
    echo ERROR: No se encontró ADB en %ADB%
    echo Por favor instala Android SDK o configura ANDROID_SDK_ROOT
    pause
    exit /b 1
)

echo ===== Verificando dispositivos conectados =====
"%ADB%" devices

echo.
echo ===== Preparando directorios en el dispositivo =====
"%ADB%" shell mkdir -p /sdcard/screenshots
"%ADB%" shell mkdir -p /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results

echo.
echo ===== Limpiando versiones anteriores =====
call gradlew clean

echo.
echo ===== Limpiando resultados anteriores del dispositivo =====
"%ADB%" shell rm -rf /sdcard/screenshots
"%ADB%" shell rm -rf /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results
"%ADB%" shell mkdir -p /sdcard/screenshots
"%ADB%" shell mkdir -p /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results

echo.
echo ===== Construyendo la aplicación =====
call gradlew build

echo.
echo ===== Construyendo el APK de tests =====
call gradlew assembleAndroidTest

echo.
echo ===== EJECUTANDO TESTS - Esto puede tomar 15-20 minutos =====
call gradlew connectedAndroidTest

echo.
echo ===== Intentando generar reporte Allure =====
where allure >nul 2>&1
if %errorlevel% equ 0 (
    allure generate app\build\allure-results --clean -o allure-report
    echo Reporte generado en: allure-report\index.html
) else (
    echo Allure no está instalado. Instálalo con: npm install -g allure-commandline
)

echo.
echo ===== Extrayendo resultados =====
"%ADB%" pull /sdcard/screenshots QA\evidencias
"%ADB%" pull /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results app\build\allure-results

echo.
echo ===== TESTS COMPLETADOS =====
pause

