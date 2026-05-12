package codigo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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

    // 🔥 LA MAGIA DE LOS SCOPES: Una pila de tablas
    private final Deque<Map<String, Simbolo>> pilaScopes;
    
    // Historial global para la Interfaz Gráfica (para que no desaparezcan de la pantalla)
    public final List<Simbolo> todosLosSimbolosGUI; 
    public List<String> erroresSemanticos;

    public TablaDeSimbolos() {
        this.pilaScopes = new ArrayDeque<>();
        this.pilaScopes.push(new LinkedHashMap<>()); // Scope Global (Nivel 0)
        
        this.todosLosSimbolosGUI = new ArrayList<>();
        this.erroresSemanticos = new ArrayList<>();
    }

    // ========================================================================
    // CONTROL DE SCOPES (NUEVO)
    // ========================================================================
    
    public void entrarBloque() {
        pilaScopes.push(new LinkedHashMap<>());
        System.out.println(">> Acción Semántica: Entrando a nuevo Scope Local...");
    }

    public void salirBloque() {
        if (pilaScopes.size() > 1) {
            pilaScopes.pop();
            System.out.println(">> Acción Semántica: Saliendo de Scope Local. Variables destruidas.");
        }
    }

    // ========================================================================
    // MÉTODOS DEL JUEZ SEMÁNTICO ACTUALIZADOS
    // ========================================================================

    public void declararVariable(String nombre, String tipo, Object valor, int linea) {
        Map<String, Simbolo> scopeActual = pilaScopes.peek();

        // 1. Verificamos si ya existe en este SCOPE (Duplicidad local)
        if (scopeActual.containsKey(nombre)) {
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: La variable '" + nombre + "' ya fue declarada previamente en este bloque.";
            erroresSemanticos.add(error);
            System.err.println(error);
        } 
        // 2. Verificamos compatibilidad de tipos
        else if (!esTipoCompatible(tipo, valor)) {
            String tipoRecibido = (valor != null) ? valor.getClass().getSimpleName() : "null";
            tipoRecibido = tipoRecibido.replace("Integer", "Entero").replace("String", "Cadena")
                                       .replace("Boolean", "Logico").replace("Double", "Real");
            
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: Incompatibilidad de tipos. " +
                           "No se puede asignar un valor de tipo '" + tipoRecibido + "' a la variable '" + nombre + "' de tipo '" + tipo + "'.";
            erroresSemanticos.add(error);
            System.err.println(error);
        } 
        // 3. Se agrega al SCOPE ACTUAL
        else {
            Simbolo nuevoSimbolo = new Simbolo(nombre, tipo, valor, linea);
            scopeActual.put(nombre, nuevoSimbolo);
            todosLosSimbolosGUI.add(nuevoSimbolo); // Guardar para que se vea en la tabla de la GUI
            System.out.println(">> Acción Semántica: Variable registrada (" + (pilaScopes.size() > 1 ? "Local" : "Global") + ") -> " + tipo + " " + nombre + " = " + valor);
        }
    }

    public Simbolo obtenerVariableEstricto(String nombre, int linea) {
        // 🔍 Busca desde el scope más interno hacia el más externo
        for (Map<String, Simbolo> scope : pilaScopes) {
            if (scope.containsKey(nombre)) {
                return scope.get(nombre);
            }
        }
        
        // Si no existe en ningún scope activo:
        String error = "ERROR SEMÁNTICO [Línea " + linea + "]: Intento de usar la variable '" + nombre + "' sin haberla definido en este contexto.";
        erroresSemanticos.add(error);
        System.err.println(error);
        return null;
    }

    private boolean esTipoCompatible(String tipoDeclarado, Object valorReal) {
        if (valorReal == null) return false;
        
        switch (tipoDeclarado.toLowerCase()) {
            case "entero": return valorReal instanceof Integer;
            case "real": return valorReal instanceof Double || valorReal instanceof Integer;
            case "cadena": return valorReal instanceof String || valorReal instanceof Integer || valorReal instanceof Double;
            case "logico": return valorReal instanceof Boolean;
            case "caracter": return valorReal instanceof String && ((String)valorReal).length() == 1;
            default: return false;
        }
    }

    public void registrarErrorSemantico(String mensaje) {
        erroresSemanticos.add(mensaje);
        System.err.println(mensaje);
    }

    public boolean validarCondicionBooleana(Object valor, int linea, String bloque) {
        if (!(valor instanceof Boolean)) {
            String tipoRecibido = (valor != null) ? valor.getClass().getSimpleName() : "null";
            tipoRecibido = tipoRecibido.replace("Integer", "Entero").replace("String", "Cadena").replace("Double", "Real");
            
            String error = "ERROR SEMÁNTICO [Línea " + linea + "]: La condición del bloque " + bloque + 
                           " debe ser de tipo Lógico. Se recibió: '" + tipoRecibido + "'.";
            registrarErrorSemantico(error);
            return false;
        }
        return true;
    }

    // ========================================================================
    // MÉTODOS COMPLEMENTARIOS (ADAPTADOS PARA GUI)
    // ========================================================================

    public void agregar(String nombre, String tipo, Object valor, int linea) {
        declararVariable(nombre, tipo, valor, linea);
    }

    public void actualizarTipo(String nombre, String tipo) {
        Simbolo s = obtenerVariableEstricto(nombre, 0);
        if (s != null) s.setTipo(tipo);
    }

    public void actualizarValor(String nombre, Object valor) {
        Simbolo s = obtenerVariableEstricto(nombre, 0);
        if (s != null) s.setValor(valor);
    }

    public boolean existe(String nombre) {
        for (Map<String, Simbolo> scope : pilaScopes) {
            if (scope.containsKey(nombre)) return true;
        }
        return false;
    }

    public Simbolo obtener(String nombre) {
        return obtenerVariableEstricto(nombre, 0);
    }

    public List<Simbolo> obtenerTodos() {
        return todosLosSimbolosGUI; 
    }

    public void limpiar() {
        pilaScopes.clear();
        pilaScopes.push(new LinkedHashMap<>());
        todosLosSimbolosGUI.clear();
        erroresSemanticos.clear();
    }

    public int tamanio() {
        return todosLosSimbolosGUI.size();
    }
}