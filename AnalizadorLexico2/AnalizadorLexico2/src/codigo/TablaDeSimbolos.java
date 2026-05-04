package codigo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tabla de Símbolos para el Compilador Turbo X²
 * 
 * Almacena y gestiona todos los identificadores encontrados
 * durante el análisis léxico. Usa un LinkedHashMap para
 * mantener el orden de inserción y evitar duplicados de nombre.
 * 
 * Cada símbolo guarda:
 *   - nombre    : el lexema del identificador
 *   - tipo      : tipo de dato (Entero, Real, Cadena, Logico, Caracter, Desconocido)
 *   - valor     : valor asignado (si se detecta en el análisis)
 *   - linea     : primera línea donde aparece
 *   - ocurrencias: cuántas veces se referencia en el código
 */
public class TablaDeSimbolos {

    // ── Clase interna que representa un símbolo ──────────────────────────────
    public static class Simbolo {
        private String nombre;
        private String tipo;
        private String valor;
        private int    linea;
        private int    ocurrencias;

        public Simbolo(String nombre, String tipo, String valor, int linea) {
            this.nombre      = nombre;
            this.tipo        = tipo;
            this.valor       = valor;
            this.linea       = linea;
            this.ocurrencias = 1;
        }

        // Getters
        public String getNombre()     { return nombre; }
        public String getTipo()       { return tipo; }
        public String getValor()      { return valor; }
        public int    getLinea()      { return linea; }
        public int    getOcurrencias(){ return ocurrencias; }

        // Setters
        public void setTipo(String tipo)   { this.tipo  = tipo; }
        public void setValor(String valor) { this.valor = valor; }
        public void incrementarOcurrencia(){ this.ocurrencias++; }

        @Override
        public String toString() {
            return String.format("Simbolo[nombre=%s, tipo=%s, valor=%s, linea=%d, ocurrencias=%d]",
                    nombre, tipo, valor, linea, ocurrencias);
        }
    }

    // ── Almacén principal: clave = nombre del identificador ──────────────────
    private final Map<String, Simbolo> tabla;

    public TablaDeSimbolos() {
        this.tabla = new LinkedHashMap<>();
    }

    /**
     * Agrega un nuevo símbolo a la tabla.
     * Si ya existe, incrementa su contador de ocurrencias.
     *
     * @param nombre nombre del identificador
     * @param tipo   tipo de dato (puede ser "Desconocido" si aún no se declaró)
     * @param valor  valor actual ("" si no aplica)
     * @param linea  línea del código fuente
     */
    public void agregar(String nombre, String tipo, String valor, int linea) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).incrementarOcurrencia();
        } else {
            tabla.put(nombre, new Simbolo(nombre, tipo, valor, linea));
        }
    }

    /**
     * Actualiza el tipo de un símbolo ya existente.
     * Útil cuando se encuentra la declaración DEFINIR x COMO Entero DESPUÉS
     * de haber insertado "x" como Desconocido.
     */
    public void actualizarTipo(String nombre, String tipo) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).setTipo(tipo);
        }
    }

    /**
     * Actualiza el valor de un símbolo (cuando se detecta una asignación).
     */
    public void actualizarValor(String nombre, String valor) {
        if (tabla.containsKey(nombre)) {
            tabla.get(nombre).setValor(valor);
        }
    }

    /** Verifica si un símbolo ya está registrado. */
    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    /** Retorna un símbolo por su nombre, o null si no existe. */
    public Simbolo obtener(String nombre) {
        return tabla.get(nombre);
    }

    /** Retorna todos los símbolos como lista (para poblar la JTable). */
    public List<Simbolo> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

    /** Limpia completamente la tabla. */
    public void limpiar() {
        tabla.clear();
    }

    /** Cantidad de símbolos registrados. */
    public int tamanio() {
        return tabla.size();
    }
}
