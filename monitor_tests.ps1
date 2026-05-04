$resultsDir = "D:\Documentos\proyectos\copilot\app\build\allure-results"
$screenshotsDir = "D:\Documentos\proyectos\copilot\QA evidencias"

Write-Host "=== MONITOREO DE EJECUCION DE TESTS ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "Esperando resultados de tests..."
Write-Host "Tiempo: $(Get-Date)"
Write-Host ""

$maxAttempts = 120
$attempts = 0

while ($attempts -lt $maxAttempts) {
    $attempts++

    if (Test-Path $resultsDir) {
        $fileCount = @(Get-ChildItem $resultsDir -ErrorAction SilentlyContinue -Recurse).Count
        Write-Host "[$attempts] Resultados encontrados: $fileCount archivos"
    }

    if (Test-Path $screenshotsDir) {
        $screenshotCount = @(Get-ChildItem "$screenshotsDir\*" -Filter "*.png" -ErrorAction SilentlyContinue).Count
        Write-Host "[$attempts] Screenshots: $screenshotCount imagenes"
    }

    Start-Sleep -Seconds 10

    if ($attempts % 6 -eq 0) {
        Write-Host "Esperando... ($attempts/120)"
    }
}

Write-Host ""
Write-Host "=== TIEMPO MAXIMO DE ESPERA ALCANZADO ===" -ForegroundColor Yellow


