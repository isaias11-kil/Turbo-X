package codigo;
import static codigo.Tokens.*;
//import java_cup.runtime.Symbol;
%%

%class Lexer
%type Tokens
%unicode
%public
%final
%char
%line
%column
%ignorecase

L=[a-zA-Z]
D=[0-9]
ID={L}({L}|{D})*
ESPACIO=[ \t\r\n]+

NUM={D}+(\.{D}+)?

// Comentarios
COMENTARIO = "//"[^\n]*
COMENTARIO_MULTILINEA = "/*"([^*]|\*+[^*/])*\*+"/"

%{
    public String lexeme;

    public int getLinea() {
        return yyline + 1;
    }

    public int getColumna() {
        return yycolumn + 1;
    }
%}

%%
//PALABRAS CLAVE
"INICIO"    {lexeme = yytext(); return RESERVADA_INICIO; }
"FIN"       {lexeme = yytext(); return RESERVADA_FIN; }            

"SI"        {lexeme = yytext(); return CONTROL_SI; }
"ENTONCES"  {lexeme = yytext(); return CONTROL_ENTONCES; }
"SINO"      {lexeme = yytext(); return CONTROL_SINO; }

"MIENTRAS"  {lexeme = yytext(); return CONTROL_MIENTRAS; }
"HACER"     {lexeme = yytext(); return CONTROL_HACER; }

"DEFINIR"   {lexeme = yytext(); return DEFINIR; }
"COMO"      {lexeme = yytext(); return COMO; }

"ESCRIBIR"  {lexeme = yytext(); return ESCRIBIR; }
"LEER"      {lexeme = yytext(); return LEER; }
"graficar"  {lexeme = yytext(); return GRAFICAR; }

// ========================= TIPOS ===================================
"Logico"    {lexeme = yytext(); return TIPO_LOGICO; }
"Entero"    {lexeme = yytext(); return TIPO_ENTERO; }
"Real"      {lexeme = yytext(); return TIPO_REAL; }
"Caracter"  {lexeme = yytext(); return TIPO_CARACTER; }
"Cadena"    {lexeme = yytext(); return TIPO_CADENA; }

"Verdadero" {lexeme = yytext(); return VALOR_VERDADERO; }
"Falso"     {lexeme = yytext(); return VALOR_FALSO; }

//==========================OPERADORES===================================
"<-"        {lexeme = yytext(); return ASIGNACION; }
"+"         {lexeme = yytext(); return OP_SUMA; }
"-"         {lexeme = yytext(); return OP_RESTA; }
"**"        {lexeme = yytext(); return OP_POTENCIA_DOBLE; }
"*"         {lexeme = yytext(); return OP_MULTIPLICACION; }
"/"         {lexeme = yytext(); return OP_DIVISION; }
"^"         {lexeme = yytext(); return OP_POTENCIA; }
">"         {lexeme = yytext(); return OP_MAYOR; }
"<"         {lexeme = yytext(); return OP_MENOR; }
"=="        {lexeme = yytext(); return OP_IGUALDAD; }
"="         {lexeme = yytext(); return SIGNO_IGUAL; }
"!="        {lexeme = yytext(); return OP_DIFERENTE; }

//==========================SIGNOS=============================================
";"         {lexeme = yytext(); return PUNTO_COMA; }
","         {lexeme = yytext(); return COMA; }
"("         {lexeme = yytext(); return PARENTESIS_ABRE; }
")"         {lexeme = yytext(); return PARENTESIS_CIERRA; }
"{"         {lexeme = yytext(); return LLAVE_ABRE; }
"}"         {lexeme = yytext(); return LLAVE_CIERRA; }

//==========================LITERALES==================================================

// REGLA CORREGIDA: Se devuelve 'Cadena' para evitar conflictos con el Enum Tokens
\'[^\']\' {lexeme = yytext(); return Cadena; }

//==========================Cadenas de texto===========================
\"(\\.|[^\"\n])*\" {lexeme = yytext(); return Cadena; }
\`[^`]*\` {lexeme = yytext(); return Cadena; }

//==========================Identificadores============================
{ID} {lexeme = yytext(); System.out.println("TOKEN: ID -> " + lexeme); return Identificador; }

//==========================Números============================
{NUM} {lexeme = yytext();  System.out.println("TOKEN: NUM-> " + lexeme); return Numero; }

//==========================Ignorar espacios y comentarios==================
{ESPACIO}  {/*Ignorar*/}
{COMENTARIO}            { lexeme = yytext(); return COMENTARIO; }
{COMENTARIO_MULTILINEA} { lexeme = yytext(); return COMENTARIO; }             

//==========================Manejo de errores============
. {
    lexeme = yytext();
    System.err.println("Símbolo no reconocido: " + yytext()
        + " en línea " + getLinea()
        + ", columna " + getColumna());
    return ERROR;     
}

<<EOF>> {
    return null;
}

