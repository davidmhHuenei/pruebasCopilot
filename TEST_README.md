# Guía de Ejecución de Tests UI - Copilot

## Requisitos Previos

1. **Dispositivo Android conectado** (físico o emulador)
2. **Android SDK Tools** instalado
3. **ADB (Android Debug Bridge)** accesible desde el PATH
4. **Allure CLI** (opcional, para generar reportes): `npm install -g allure-commandline`

## Cambios Realizados (Solución del Error de Allure)

### Problema Original
Error: `io.qameta.allure.AllureResultsWriteException: Could not create Allure results directory`

### Solución Implementada

#### 1. **Configuración de Allure (allure.properties)**
```
allure.results.directory=/sdcard/Android/data/com.example.pruebascopilot/cache/allure-results
```
Cambio de ruta relativa a ruta en almacenamiento externo con permisos adecuados.

#### 2. **TestListener (Gestión de Permisos en Tiempo de Ejecución)**
- Archivo: `app/src/androidTest/java/com/example/pruebascopilot/TestListener.kt`
- Solicita permisos en tiempo de ejecución
- Crea directorios necesarios antes de ejecutar tests
- Se ejecuta automáticamente al iniciar el suite de tests

#### 3. **AllureTestRunner (Runner Personalizado)**
- Archivo: `app/src/androidTest/java/com/example/pruebascopilot/AllureTestRunner.kt`
- Configura propiedades dinámicas de Allure
- Usa el directorio de caché de la aplicación para los resultados

#### 4. **BaseAndroidTest (Clase Base para Tests)**
- Archivo: `app/src/androidTest/java/com/example/pruebascopilot/BaseAndroidTest.kt`
- Centraliza la lógica de permisos y screenshots
- Proporciona métodos reutilizables para todos los tests:
  - `ensurePermissions()`: Verifica y solicita permisos necesarios
  - `createDirectoriesIfNeeded()`: Crea directorios en el dispositivo
  - `saveScreenshot(name)`: Guarda screenshots con manejo de permisos

#### 5. **Actualización de Tests**
Todos los tests heredan de `BaseAndroidTest`:
- `LoginScreenTest.kt`
- `RegisterScreenTest.kt`
- `AuthenticationFlowTest.kt`

#### 6. **Script de Ejecución Mejorado (run_tests.bat)**
- Otorga permisos a través de `pm grant` antes de ejecutar tests
- Crea directorios necesarios en el dispositivo
- Limpia directorios anteriore antes de ejecutar
- Descarga resultados de Allure desde la ubicación correcta

#### 7. **Configuración de build.gradle.kts**
```kotlin
testOptions {
    execution = "ANDROIDX_TEST_ORCHESTRATOR"
}
```
- Android Test Orchestrator activado
- Asegura que cada test corra en una instancia limpia
- Mejor manejo de permisos y recursos

## Posibles Problemas y Soluciones

### Error: "Instrumentation did not complete"
Este error indica que los tests no se ejecutaron correctamente en el dispositivo. Causas comunes:

1. **Dispositivo no conectado**
   ```bash
   adb devices
   ```
   Debería mostrar tu dispositivo con estado "device"

2. **APK de test no instalado**
   ```bash
   adb shell pm list packages | grep pruebascopilot
   ```
   Debería mostrar el paquete de pruebas

3. **Permisos faltantes** ⚠️ RESUELTO
   - El TestListener ahora gestiona permisos automáticamente
   - Se ejecuta antes de cada suite de tests

4. **Versión de Android incompatible**
   - Los tests requieren Android 8.0 (Nivel 26) o superior
   - El proyecto está configurado con minSdk = 26

### Error: "Could not create Allure results directory" ⚠️ RESUELTO
- La configuración de directorios se realiza automáticamente
- TestListener crea directorios con permisos 777
- El AllureTestRunner configura la ruta de resultados dinámicamente

### Screenshots no se guardan ⚠️ RESUELTO
- BaseAndroidTest gestiona permisos y creación de directorios
- Los métodos `ensurePermissions()` y `createDirectoriesIfNeeded()` son llamados automáticamente
- Verifica que el dispositivo tenga espacio disponible

## Procedimiento para Ejecutar Tests

### Opción 1: Script Automático (Recomendado)
```bash
cd D:\Documentos\proyectos\copilot
run_tests.bat
```

### Opción 2: Comandos Individuales

#### 1. Limpiar y compilar
```bash
gradlew clean build
```

#### 2. Compilar APK de tests
```bash
gradlew assembleAndroidTest
```

#### 3. Ejecutar tests en dispositivo conectado
```bash
gradlew connectedAndroidTest
```

#### 4. Extraer screenshots
```bash
adb pull /sdcard/screenshots "QA evidencias"
```

#### 5. Extraer resultados de Allure (actualizado)
```bash
adb pull /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results app/build/allure-results
```

#### 6. Generar reporte Allure (si está instalado)
```bash
allure generate app/build/allure-results --clean -o allure-report
```

## Archivos de Test

Los siguientes archivos contienen los tests:
- `app/src/androidTest/java/com/example/pruebascopilot/ui/screens/LoginScreenTest.kt` (4 tests)
- `app/src/androidTest/java/com/example/pruebascopilot/ui/screens/RegisterScreenTest.kt` (4 tests)
- `app/src/androidTest/java/com/example/pruebascopilot/ui/screens/AuthenticationFlowTest.kt` (4 tests)

Total: 12 tests

## Salida Esperada

Después de ejecutar los tests, deberías ver:
- ✓ Resultados de tests en la consola
- ✓ Screenshots en `QA evidencias/screenshots/`
- ✓ Resultados de Allure en `app/build/allure-results/`
- ✓ Reporte HTML en `allure-report/index.html`

## Estructura del Proyecto

```
copilot/
├── app/
│   ├── src/
│   │   ├── androidTest/
│   │   │   ├── java/
│   │   │   │   └── com/example/pruebascopilot/
│   │   │   │       ├── BaseAndroidTest.kt        [NUEVO]
│   │   │   │       ├── AllureTestRunner.kt       [NUEVO]
│   │   │   │       ├── TestListener.kt           [NUEVO]
│   │   │   │       └── ui/screens/
│   │   │   │           ├── LoginScreenTest.kt    [ACTUALIZADO]
│   │   │   │           ├── RegisterScreenTest.kt [ACTUALIZADO]
│   │   │   │           └── AuthenticationFlowTest.kt [ACTUALIZADO]
│   │   │   └── resources/
│   │   │       └── allure.properties             [ACTUALIZADO]
│   │   ├── main/
│   │   └── test/
│   ├── build.gradle.kts                         [ACTUALIZADO]
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── gradlew.bat
├── run_tests.bat                                [ACTUALIZADO]
└── TEST_README.md                               [ESTE ARCHIVO]
```

## Configuration

### build.gradle.kts - Configuración de Tests
- `execution = "ANDROIDX_TEST_ORCHESTRATOR"` - Cada test se ejecuta en instancia limpia
- `testInstrumentationRunner = "com.example.pruebascopilot.AllureTestRunner"` - Runner personalizado
- `testInstrumentationRunnerArguments["listener"] = "com.example.pruebascopilot.TestListener"` - Listener de permisos
- Dependencias de Espresso, Allure, UiAutomator, Barista

### allure-report/
Carpeta que contiene el reporte HTML generado después de ejecutar `allure generate`

## Troubleshooting

### "Could not load test results"
- Verifica que el dispositivo tenga espacio suficiente
- Intenta resetear el dispositivo
- Verifica los logs: `adb logcat`

### Build falla
- Limpia el caché: `gradlew clean`
- Detén el daemon: `gradlew --stop`

### Los permisos no se otorgan
- Verifica que ADB tenga acceso root o al menos privilegi</s>
```bash
adb shell pm grant com.example.pruebascopilot android.permission.WRITE_EXTERNAL_STORAGE
adb shell pm grant com.example.pruebascopilot android.permission.READ_EXTERNAL_STORAGE
```

## Cambios Arquitectónicos

### Antes
- Cada test manejaba sus propios permisos y screenshots
- Configuración de Allure con ruta relativa problemática
- Inconsistencias en el manejo de directorios

### Después  
- Clase base centraliza lógica de permisos y screenshots
- TestListener gestiona permisos al inicio del suite
- Rutas de Allure dinámicas y configuradas correctamente
- AllureTestRunner prepara el entorno
- Mejor separación de responsabilidades

## Contacto

Si encuentras problemas, verifica:
1. La salida de compilación
2. Los logs del dispositivo con `adb logcat`
3. La carpeta `app/build/outputs/androidTest-results/` para resultados XML
4. Que el archivo `run_tests.bat` haya otorgado los permisos correctamente
