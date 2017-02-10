/**
 * @author Carlos Olimpio - UFRPE
 */

package doxa.v0;

%%

%class Lexer
%public
%function nextToken
%type Token
%line
%column

Comment = {LineComment} | {MultiLineComment}
LineComment = "**" [^\n]*
MultiLineComment = ">>" [^*] ~"<<"

WhiteSpace = ([ \t\n\r]+)

Identifier = [a-z] ([a-zA-Z_] | [0-9])*

IntegerLiteral = [0-9]+
FloatPointLiteral = [0-9]* "." [0-9]+ | [0-9]+ "." [0-9]*
CharLiteral = "\'"([0-9] | [a-zA-Z] | "\\n" | "\\t" | [ ] | [:] | [(] | [)] | [,] )"\'"

%%


{WhiteSpace} { }

{IntegerLiteral} { return new Token(TokenType.INT_LITERAL, yyline, yycolumn); }
{FloatPointLiteral} { return new Token(TokenType.FLOAT_LITERAL, yyline, yycolumn); }
{CharLiteral} { return new Token(TokenType.CHAR_LITERAL, yyline, yycolumn); }

";" { return new Token(TokenType.SYM_PT_VIRG, yyline, yycolumn); }

"<" { return new Token(TokenType.REL_MENOR, yyline, yycolumn); }
">" { return new Token(TokenType.REL_MAIOR, yyline, yycolumn); }
"<=" { return new Token(TokenType.REL_MENOR_IGUAL, yyline, yycolumn); }
">=" { return new Token(TokenType.REL_MAIOR_IGUAL, yyline, yycolumn); }
"=" { return new Token(TokenType.REL_IGUAL, yyline, yycolumn); }
"<>" { return new Token(TokenType.REL_MENOR_MAIOR, yyline, yycolumn); }

"+" { return new Token(TokenType.ADD, yyline, yycolumn); }
"-" { return new Token(TokenType.SUB, yyline, yycolumn); }
"*" { return new Token(TokenType.MULT, yyline, yycolumn); }
"/" { return new Token(TokenType.DIV, yyline, yycolumn); }
"%" { return new Token(TokenType.MOD, yyline, yycolumn); }

"and" { return new Token(TokenType.AND, yyline, yycolumn); }
"or" { return new Token(TokenType.OR, yyline, yycolumn); }
"not" { return new Token(TokenType.NOT, yyline, yycolumn); }
"if" { return new Token(TokenType.IF, yyline, yycolumn); }
"else" { return new Token(TokenType.ELSE, yyline, yycolumn); }
"while" { return new Token(TokenType.WHILE, yyline, yycolumn); }
"return" { return new Token(TokenType.RETURN, yyline, yycolumn); }
"float" { return new Token(TokenType.FLOAT, yyline, yycolumn); }
"char" { return new Token(TokenType.CHAR, yyline, yycolumn); }
"void" { return new Token(TokenType.VOID, yyline, yycolumn); }
"prnt" { return new Token(TokenType.PRNT, yyline, yycolumn); }
"int" { return new Token(TokenType.INT, yyline, yycolumn); }
"proc" { return new Token(TokenType.PROC, yyline, yycolumn); }
"var" { return new Token(TokenType.VAR, yyline, yycolumn); }

":=" { return new Token(TokenType.ATRIBUICAO, yyline, yycolumn); }

")" { return new Token(TokenType.SYM_FECHA_PAR, yyline, yycolumn); }
"(" { return new Token(TokenType.SYM_ABRE_PAR, yyline, yycolumn); }
"," { return new Token(TokenType.SYM_VIRG, yyline, yycolumn); }
"{" { return new Token(TokenType.SYM_ABRE_CHAVE, yyline, yycolumn); }
"}" { return new Token(TokenType.SYM_FECHA_CHAVE, yyline, yycolumn); }

{Identifier} { return new Token(TokenType.IDENTIFICADOR, yyline, yycolumn); }

{Comment} { }

. { throw new Error("Caractere ilegal <" + yytext() + "> [line: " + yyline + " column: " + (yycolumn + 1) + "]"); }

<<EOF>> {
	// Casa com o fim do arquivo apenas.
	return new Token(TokenType.EOF);
}
