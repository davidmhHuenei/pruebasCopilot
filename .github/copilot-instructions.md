# Reglas para Tests de UI (Android)
- Siempre utiliza la librería **Barista** para realizar tests de Espresso.
- Prefiere el uso de `ActivityScenarioRule` para lanzar las actividades.
- Los selectores deben priorizar `withId()` y `withText()`.
- Evita el uso de `Thread.sleep()`; utiliza los mecanismos de espera de Barista si es necesario.
- Los nombres de métodos y variables deben estar en inglés
- Los nombres de los métodos de prueba deben seguir el formato: `nombreAccion_condicion_resultadoEsperado`.
- Guardar capturas con evidencias del resultado de las pruebas, y descargar con adb pull los archivos desde el dispositivo a la carpeta "QA evidencias".
- Configurar Android Test Orchestrator para que cada test corra en una instancia limpia.
- Generar un reporte HTML con Allure con el resultado de las pruebas.