/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.io.File;
/**
 *
 * @author braya
 */
public class Principal {
    public static void main(String[] args) {
        // Esto busca la carpeta 'src/codigo/Lexer.flex' dentro de tu proyecto actual
String ruta = System.getProperty("user.dir") + "/src/codigo/Lexer.flex";
        generarLexer(ruta);
    }
    public static void generarLexer(String ruta){
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
}
//Comando a ejecutar en cmd desde la carpeta del proyecto si no se genera Lexer.java
//java -jar C:\jflex\JFlex.jar Lexer.flex