package codigo;

public class GeneradorLexer {

    public static void main(String[] args) {
        // La ruta exacta de tu archivo
        String rutaArchivoFlex = "src/codigo/Lexer.flex";

        try {
            // En JFlex 1.9.1, se le manda un arreglo de Strings con la ruta
            String[] opciones = new String[]{rutaArchivoFlex};
            jflex.Main.generate(opciones);

            System.out.println("¡Éxito! El archivo Lexer.java se ha generado correctamente.");
        } catch (Exception e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}
