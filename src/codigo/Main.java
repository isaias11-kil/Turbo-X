package codigo;

import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {
        // Esta es la ruta donde creamos tu archivo de texto
        String rutaArchivoPrueba = "src/codigo/prueba.txt";
        
        try {
            // Leemos el archivo
            Reader lector = new FileReader(rutaArchivoPrueba);
            
            // Se lo pasamos al Lexer que acabas de generar
            Lexer lexer = new Lexer(lector);
            
            System.out.println("--- INICIANDO ANÁLISIS LÉXICO ---");
            
            // yylex() arranca el motor y lee todo el archivo
            lexer.yylex();
            
            System.out.println("--- ANÁLISIS FINALIZADO ---");
            
        } catch (Exception e) {
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }
    }
}