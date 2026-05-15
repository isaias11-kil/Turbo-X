package codigo;

/**
 * Motor de ejecución (Rol 3): acciones finales del lenguaje tras un análisis correcto.
 * Por ahora concentra la invocación de gráficas; las expresiones numéricas ya se evalúan
 * durante el parseo y los valores viven en la {@link TablaDeSimbolos}.
 */
public final class MotorEjecucion {

    private MotorEjecucion() {
    }

    /**
     * Atiende la instrucción {@code graficar(...)} abriendo una ventana con JFreeChart.
     * El modo barras / líneas se deduce del valor cuando es {@link String}: si comienza por
     * {@code LINEAS:} (sin distinguir mayúsculas) se usa gráfica de líneas y el resto es la lista
     * numérica; en caso contrario, barras.
     *
     * @param tabla  tabla de símbolos actual (para registrar errores de ejecución)
     * @param s      variable cuyo valor se representa (número o cadena con números separados por coma)
     * @param linea  línea fuente aproximada (para mensajes)
     */
    public static void solicitarGrafica(TablaDeSimbolos tabla, TablaDeSimbolos.Simbolo s, int linea) {
        ServicioGraficasJFreeChart.mostrarDesdeSimbolo(tabla, s, linea);
    }
}
