package codigo;

import java_cup.runtime.Symbol;

%{
// Método helper para devolver tokens a CUP
private Symbol token(int tipo) {
    return new Symbol(tipo, yyline, yycolumn, yytext());
}
%}

%%
// SECCIÓN 2: Opciones y Macros
%class Lexer
%public
%unicode
%line
%column
%standalone

Letra = [a-zA-Z]
Digito = [0-9]
Espacio = [ \t\r\n]+

Identificador = {Letra}({Letra}|{Digito}|_)*
Entero = {Digito}+
Real = {Digito}+\.{Digito}+
Cadena = \"[^\"]*\"
Caracter = \'[^\']\'

// Comentarios
ComentarioLinea = "//".*
ComentarioBloque = "/*"[^*]*"*"+([^/*][^*]*"*"+)*"/"

%%
// SECCIÓN 3: Reglas Léxicas
<YYINITIAL> {

  // ===== PALABRAS CLAVE =====
  "INICIO"        { return token(sym.RESERVADA_INICIO); }
  "FIN"           { return token(sym.RESERVADA_FIN); }

  "SI"            { return token(sym.CONTROL_SI); }
  "ENTONCES"      { return token(sym.CONTROL_ENTONCES); }
  "SINO"          { return token(sym.CONTROL_SINO); }

  // ⚠️ CAMBIO IMPORTANTE: separar FIN SI
  "MIENTRAS"      { return token(sym.CONTROL_MIENTRAS); }
  "HACER"         { return token(sym.CONTROL_HACER); }

  "DEFINIR"       { return token(sym.DEFINIR); }
  "COMO"          { return token(sym.COMO); }

  "ESCRIBIR"      { return token(sym.ESCRIBIR); }
  "LEER"          { return token(sym.LEER); }

  // ===== TIPOS =====
  "Logico"        { return token(sym.TIPO_LOGICO); }
  "Entero"        { return token(sym.TIPO_ENTERO); }
  "Real"          { return token(sym.TIPO_REAL); }
  "Caracter"      { return token(sym.TIPO_CARACTER); }
  "Cadena"        { return token(sym.TIPO_CADENA); }

  "Verdadero"     { return token(sym.VALOR_VERDADERO); }
  "Falso"         { return token(sym.VALOR_FALSO); }

  // ===== OPERADORES =====
  "<-"            { return token(sym.ASIGNACION); }

  "+"             { return token(sym.OP_SUMA); }
  "-"             { return token(sym.OP_RESTA); }
  "*"             { return token(sym.OP_MULTIPLICACION); }
  "/"             { return token(sym.OP_DIVISION); }
  "^"             { return token(sym.OP_POTENCIA); }
  "**"            { return token(sym.OP_POTENCIA_DOBLE); }

  ">"             { return token(sym.OP_MAYOR); }
  "<"             { return token(sym.OP_MENOR); }
  "=="            { return token(sym.OP_IGUALDAD); }
  "!="            { return token(sym.OP_DIFERENTE); }

  // ===== SIGNOS =====
  ";"             { return token(sym.PUNTO_COMA); }
  ","             { return token(sym.COMA); }

  "("             { return token(sym.PARENTESIS_ABRE); }
  ")"             { return token(sym.PARENTESIS_CIERRA); }

  "{"             { return token(sym.LLAVE_ABRE); }
  "}"             { return token(sym.LLAVE_CIERRA); }

  // ===== LITERALES =====
  {Real}          { return token(sym.LITERAL_REAL); }
  {Entero}        { return token(sym.LITERAL_ENTERO); }
  {Cadena}        { return token(sym.LITERAL_CADENA); }
  {Caracter}      { return token(sym.LITERAL_CARACTER); }

  // ===== IDENTIFICADOR =====
  {Identificador} { return token(sym.IDENTIFICADOR); }

  // ===== COMENTARIOS =====
  {ComentarioLinea}   { /* ignorar */ }
  {ComentarioBloque}  { /* ignorar */ }

  // ===== ESPACIOS =====
  {Espacio}       { /* ignorar */ }

  // ===== ERROR =====
  [^] {
    System.err.println("ERROR LÉXICO: '" + yytext() +
    "' en línea " + (yyline+1) + ", columna " + (yycolumn+1));
  }
}