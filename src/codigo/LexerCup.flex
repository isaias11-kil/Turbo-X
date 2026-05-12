package codigo;

import java_cup.runtime.Symbol;
%%

%class LexerCup
%public
%type Symbol
%cup
%line
%column
%ignorecase  // <--- ¡NUEVO! Hace que ignore mayúsculas y minúsculas

L=[a-zA-Z]
D=[0-9]
ID={L}({L}|{D})*
ESPACIO=[ \t\r\n]+

NUM={D}+(\.{D}+)?      // Enteros y decimales

Caracter = \'[^\']\'

// Comentarios
COMENTARIO = "//"[^\n]*
COMENTARIO_MULTILINEA = "/*"([^*]|\*+[^*/])*\*+"/"


%{
    private Symbol symbol(int type){
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

%%
/* =====================================================
   COMENTARIOS Y ESPACIOS IGNORADOS
   ===================================================== */

// Espacios, tabs y saltos de línea
[ \t\r\n]+                { /* Ignorar espacios */ }

// Comentarios de una línea
"//".* { /* Ignorar comentario */ }

// Comentarios multilínea
"/*"([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*"*"+"/"
                          { /* Ignorar comentario */ }

//PALABRAS CLAVE
"INICIO"    { return symbol(sym.RESERVADA_INICIO); }
"FIN"       { return symbol(sym.RESERVADA_FIN); }            

"SI"        { return symbol(sym.CONTROL_SI); }
"ENTONCES"  { return symbol(sym.CONTROL_ENTONCES); }
"SINO"      { return symbol(sym.CONTROL_SINO); }

"MIENTRAS"  { return symbol(sym.CONTROL_MIENTRAS); }
"HACER"     { return symbol(sym.CONTROL_HACER); }

"DEFINIR"   { return symbol(sym.DEFINIR); }
"COMO"      { return symbol(sym.COMO); }

"ESCRIBIR"  { return symbol(sym.ESCRIBIR); }
"LEER"      { return symbol(sym.LEER); }
"graficar"  { return symbol(sym.GRAFICAR); }  // <--- ¡AQUÍ ESTÁ TU NUEVA INSTRUCCIÓN!

                    // ========================= TIPOS ===================================
"Logico"    { return symbol(sym.TIPO_LOGICO); }
"Entero"    { return symbol(sym.TIPO_ENTERO); }
"Real"      { return symbol(sym.TIPO_REAL); }
"Caracter"  { return symbol(sym.TIPO_CARACTER); }
"Cadena"    { return symbol(sym.TIPO_CADENA); }

"Verdadero" { return symbol(sym.VALOR_VERDADERO); }
"Falso"     { return symbol(sym.VALOR_FALSO); }
                    //==========================OPERADORES===================================
"<-"        { return symbol(sym.ASIGNACION); }

"+"         { return symbol(sym.OP_SUMA); }
"-"         { return symbol(sym.OP_RESTA); }
"**"        { return symbol(sym.OP_POTENCIA_DOBLE); }
"*"         { return symbol(sym.OP_MULTIPLICACION); }
"/"         { return symbol(sym.OP_DIVISION); }
"^"         { return symbol(sym.OP_POTENCIA); }
">"         { return symbol(sym.OP_MAYOR); }
"<"         { return symbol(sym.OP_MENOR); }

"=="        { return symbol(sym.OP_IGUALDAD); }
"!="        { return symbol(sym.OP_DIFERENTE); }

                    //==========================SIGNOS=============================================
";"         { return symbol(sym.PUNTO_COMA); }
","         { return symbol(sym.COMA); }

"("         { return symbol(sym.PARENTESIS_ABRE); }
")"         { return symbol(sym.PARENTESIS_CIERRA); }

"{"         { return symbol(sym.LLAVE_ABRE); }
"}"         { return symbol(sym.LLAVE_CIERRA); }

                    //==========================LITERALES==================================================
                    //==========================Cadenas de texto===========================
\"(\\.|[^\"\n])*\" {
    return symbol(sym.LITERAL_CADENA, yytext());
}

\`[^`]*\` {
    return symbol(sym.LITERAL_CADENA, yytext());
}
//==========================LITERALES==================================================
                    
// --- NUEVO: REGLA PARA CARACTERES ('a') ---
\'[^\']\' { return symbol(sym.LITERAL_CARACTER, yytext()); }

                    //==========================Cadenas de texto===========================
\"(\\.|[^\"\n])*\" {
    return symbol(sym.LITERAL_CADENA, yytext());
}
                    //==========================Identificadores============================
{ID}  {return symbol(sym.Identificador,yytext()); }

                    //==========================Números (incluyendo flotantes)============================
{NUM} { return symbol(sym.Numero,yytext()); }


                    //==========================Ignorar espacios y comentarios==================
{ESPACIO}  {/*Ingnorar los espacios */}
{COMENTARIO}            { }
{COMENTARIO_MULTILINEA} { }             


                    //==========================Manejo de errores============
          
    
    . {
    System.err.println("Símbolo no reconocido: " + yytext()
        + " en línea " + yyline
        + ", columna " + yycolumn);

    return symbol(sym.ERROR, yytext());
}



<<EOF>> {
    return null;
}