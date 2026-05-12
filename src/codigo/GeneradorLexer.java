package codigo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneradorLexer {

    public static void main(String[] args) throws Exception {
        // Rutas de tus archivos .flex y .cup preparadas para JFlex 1.9+
        String[] ruta1 = {"src/codigo/Lexer.flex"};
        String[] ruta2 = {"src/codigo/LexerCup.flex"};
        String[] rutaS = {"-parser", "Sintax", "src/codigo/Sintax.cup"};

        generar(ruta1, ruta2, rutaS);
    }

    public static void generar(String[] ruta1, String[] ruta2, String[] rutaS) throws IOException, Exception {
        // Ejecuta JFlex 1.9.1 con un arreglo de Strings
        jflex.Main.generate(ruta1);
        jflex.Main.generate(ruta2);

        // Ejecuta Java CUP
        java_cup.Main.main(rutaS);

        // Mueve el archivo sym.java a la carpeta correcta
        Path rutaSym = Paths.get("src/codigo/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        }
        Files.move(
            Paths.get("sym.java"), 
            Paths.get("src/codigo/sym.java")
        );
        
        // Mueve el archivo Sintax.java a la carpeta correcta
        Path rutaSin = Paths.get("src/codigo/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
            Paths.get("Sintax.java"), 
            Paths.get("src/codigo/Sintax.java")
        );
        
        System.out.println("¡Éxito total! Archivos Lexer.java, LexerCup.java, Sintax.java y sym.java generados correctamente.");
    }
}