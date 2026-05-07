
package codigo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
/**
 *
 * @author braya
 */
public class Principal {
    public static void main(String[] args) {
        // Esto busca la carpeta 'src/codigo/Lexer.flex' dentro de tu proyecto actual
String ruta = System.getProperty("user.dir") + "/src/codigo/Lexer.flex";
String ruta2 = System.getProperty("user.dir") + "/src/codigo/LexerCup.flex";
        //Ruta del archivo sintax.cup
String rutaCup= System.getProperty("user.dir") + "/src/codigo/Sintax.cup";        
String rutaDestino =System.getProperty("user.dir") + "/src/codigo/";

        generarLexer(ruta);
        generarLexer(ruta2);
        generarCup(rutaCup,rutaDestino);
    }
    public static void generarLexer(String ruta){
        try {
            jflex.Main.generate(new String[]{ruta});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Metodo para generar CUP
    public static void generarCup(String rutaCup,String rutaDestino){
        try{
            String[] opciones = {
            "-parser","Sintax",
            "-symbols","sym",
            rutaCup
            };
            java_cup.Main.main(opciones);
            
            //Mover los archivos generados a la src/codigo/
            moverArchivo("Sintax.java", rutaDestino + "Sintax.java");
            moverArchivo("sym.java", rutaDestino + "sym.java");
        }catch (Exception ex){
        System.out.println("Error al ejecutar Java CUP: " + ex.getMessage());
    }
  }
    
    //metodo para mover los archivos a src/codigo/
  private static void moverArchivo (String origen,String destino) throws IOException{
  Path rutaOrigen = new File(origen).toPath();
  Path rutaDestino = new File(destino).toPath();
  Files.move(rutaOrigen, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
  
  }
    
    
    
}
//Comando a ejecutar en cmd desde la carpeta del proyecto si no se genera Lexer.java
//java -jar C:\jflex\JFlex.jar Lexer.flex