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
        private Object valor; 
        private int    linea;
        private int    ocurrencias;

        public Simbolo(String nombre, String tipo, Object valor, int linea) {
            this.nombre      = nombre;
            this.tipo        = tipo;
            this.valor       = valor;
            this.linea       = linea;
            this.ocurrencias = 1;
        }

        public String getNombre()     { return nombre; }
        public String getTipo()       { return tipo; }
        public Object getValor()      { return valor; }
        public int    getLinea()      { return linea; }
        public int    getOcurrencias(){ return ocurrencias; }

        public void setTipo(String tipo)   { this.tipo  = tipo; }
        public void setValor(Object valor) { this.valor = valor; }
        public void incrementarOcurrencia(){ this.ocurrencias++; }

        @Override
        public String toString() {
            return String.format("Simbolo[nombre=%s, tipo=%s, valor=%s, linea=%d, ocurrencias=%d]",
                    nombre, tipo, valor, linea, ocurrencias);
        }
    }

    private final Map<String, Simbolo> tabla;
    public List<String> erroresSemanticos;

    public TablaDeSimbolos() {
        this.tabla = new LinkedHashMap<>();
        this.erroresSemanticos = new ArrayList<>();
    }

    // ========================================================================
    // MÉTODOS DEL JUEZ SEMÁNTICO ACTUALIZADOS
    // ========================================================================

    /**
     * REGLA SEMÁNTICA 1 y 3: Declarar sin duplicados y validar tipos.
     */
    public void declararVariable(String nombre, String tipo, Object valor, int linea) {
        // 1. Verificamos si ya existe (Duplicidad)
        if (tabla.containsKey(nombre)) {
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: La variable '" + nombre + "' ya fue declarada previamente.";
            erroresSemanticos.add(error);
            System.err.println(error);
        } 
        // 2. Verificamos compatibilidad de tipos (Type Checking)
        else if (!esTipoCompatible(tipo, valor)) {
            String tipoRecibido = (valor != null) ? valor.getClass().getSimpleName() : "null";
            // Ajuste de nombres de clase Java a nombres del lenguaje Turbo X²
            tipoRecibido = tipoRecibido.replace("Integer", "Entero").replace("String", "Cadena");
            
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: Incompatibilidad de tipos. " +
                           "No se puede asignar un valor de tipo '" + tipoRecibido + "' a la variable '" + nombre + "' de tipo '" + tipo + "'.";
            erroresSemanticos.add(error);
            System.err.println(error);
        } 
        // 3. Si todo es correcto, se agrega a la tabla
        else {
            tabla.put(nombre, new Simbolo(nombre, tipo, valor, linea));
            System.out.println(">> Acción Semántica: Variable registrada -> " + tipo + " " + nombre + " = " + valor);
        }
    }

    public Simbolo obtenerVariableEstricto(String nombre, int linea) {
        if (!tabla.containsKey(nombre)) {
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: Intento de usar la variable '" + nombre + "' sin haberla definido.";
            erroresSemanticos.add(error);
            System.err.println(error);
            return null;
        }
        return tabla.get(nombre);
    }

    /**
     * REGLA SEMÁNTICA 3: Validación Estricta de Tipos
     */
    private boolean esTipoCompatible(String tipoDeclarado, Object valorReal) {
        if (valorReal == null) return false;
        
        switch (tipoDeclarado.toLowerCase()) {
            case "entero":
                return valorReal instanceof Integer;
            case "real":
                return valorReal instanceof Double || valorReal instanceof Integer;
            case "cadena":
                return valorReal instanceof String;
            default:
                return false;
        }
    }

    // ========================================================================
    // MÉTODOS COMPLEMENTARIOS
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