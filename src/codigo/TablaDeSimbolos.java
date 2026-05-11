package codigo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TablaDeSimbolos {

    // ── Clase interna que representa un símbolo ──────────────────────────────
    public static class Simbolo {
        private String nombre;
        private String tipo;
        private Object valor; // <--- CAMBIO 1: Ahora es Object para soportar Integer, Double o String
        private int    linea;
        private int    ocurrencias;

        public Simbolo(String nombre, String tipo, Object valor, int linea) {
            this.nombre      = nombre;
            this.tipo        = tipo;
            this.valor       = valor;
            this.linea       = linea;
            this.ocurrencias = 1;
        }

        // Getters
        public String getNombre()     { return nombre; }
        public String getTipo()       { return tipo; }
        public Object getValor()      { return valor; }
        public int    getLinea()      { return linea; }
        public int    getOcurrencias(){ return ocurrencias; }

        // Setters
        public void setTipo(String tipo)   { this.tipo  = tipo; }
        public void setValor(Object valor) { this.valor = valor; }
        public void incrementarOcurrencia(){ this.ocurrencias++; }

        @Override
        public String toString() {
            return String.format("Simbolo[nombre=%s, tipo=%s, valor=%s, linea=%d, ocurrencias=%d]",
                    nombre, tipo, valor, linea, ocurrencias);
        }
    }

    // ── Almacén principal y Registro de Errores ──────────────────
    private final Map<String, Simbolo> tabla;
    public List<String> erroresSemanticos; // <--- CAMBIO 2: Lista para guardar los errores

    public TablaDeSimbolos() {
        this.tabla = new LinkedHashMap<>();
        this.erroresSemanticos = new ArrayList<>();
    }

    // ========================================================================
    // NUEVOS MÉTODOS DEL JUEZ SEMÁNTICO
    // ========================================================================

    /**
     * REGLA SEMÁNTICA 1: Declarar sin duplicados.
     * Úsalo cuando el usuario haga: Entero x <- 5;
     */
    public void declararVariable(String nombre, String tipo, Object valor, int linea) {
        if (tabla.containsKey(nombre)) {
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: La variable '" + nombre + "' ya fue declarada previamente.";
            erroresSemanticos.add(error);
            System.err.println(error);
        } else {
            tabla.put(nombre, new Simbolo(nombre, tipo, valor, linea));
            System.out.println(">> Acción Semántica: Variable registrada -> " + tipo + " " + nombre + " = " + valor);
        }
    }

    /**
     * REGLA SEMÁNTICA 2: Usar variables que existan.
     * Úsalo en operaciones matemáticas o en graficar(x).
     */
    public Simbolo obtenerVariableEstricto(String nombre, int linea) {
        if (!tabla.containsKey(nombre)) {
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: Intento de usar la variable '" + nombre + "' sin haberla definido.";
            erroresSemanticos.add(error);
            System.err.println(error);
            return null;
        }
        return tabla.get(nombre);
    }

    // ========================================================================
    // MÉTODOS ORIGINALES (Mantenidos para no romper tu Interfaz Gráfica)
    // ========================================================================

    public void agregar(String nombre, String tipo, Object valor, int linea) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).incrementarOcurrencia();
        } else {
            tabla.put(nombre, new Simbolo(nombre, tipo, valor, linea));
        }
    }

    public void actualizarTipo(String nombre, String tipo) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).setTipo(tipo);
        }
    }

    public void actualizarValor(String nombre, Object valor) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).setValor(valor);
        }
    }

    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    public Simbolo obtener(String nombre) {
        return tabla.get(nombre);
    }

    public List<Simbolo> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

    public void limpiar() {
        tabla.clear();
        erroresSemanticos.clear();
    }

    public int tamanio() {
        return tabla.size();
    }
}