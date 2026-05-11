package codigo;

public class Simbolo {
    private String nombre;
    private String tipo; // "entero", "real", "cadena", etc.
    private Object valor;
    private int linea;

    public Simbolo(String nombre, String tipo, Object valor, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    // Getters para acceder a la información
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public Object getValor() { return valor; }
    public int getLinea() { return linea; }
    
    // Setter por si la variable cambia de valor después
    public void setValor(Object valor) { this.valor = valor; }
}