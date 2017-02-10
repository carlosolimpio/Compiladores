/**
 * @author Carlos Olimpio
 */

package doxa.v0;

public enum TokenType {
	
	//string para dar nomes a variáveis
	IDENTIFICADOR,
	
	//operadores relacionais
	REL_MENOR,				//<
	REL_MAIOR,				//>
	REL_MENOR_IGUAL,		//<=
	REL_MAIOR_IGUAL,		//>=
	REL_IGUAL,				//=
	REL_MENOR_MAIOR,		//<>
	
	//operadores logico-aritmeticos
	ADD,
	SUB,
	MULT,
	DIV,
	MOD,
	//palavras reservadas: and, or, not
	
	//operador de atribuição
	ATRIBUICAO,
	
	//simbolos especiais
	SYM_ABRE_PAR,
	SYM_FECHA_PAR,
	SYM_VIRG,
	SYM_PT_VIRG,
	SYM_ABRE_CHAVE,
	SYM_FECHA_CHAVE,
	
	//Palavras chave reservadas
	IF,
	ELSE,
	WHILE,
	RETURN,
	FLOAT,
	CHAR,
	VOID,
	PRNT,
	INT,
	AND,
	OR,
	NOT,
	PROC,
	VAR,
	
	//valor interiro literal
	INT_LITERAL,
	
	//valor real (ponto flutuante) literal
	FLOAT_LITERAL,
	
	//caractere literal, colocados entre ''
	CHAR_LITERAL,
	
	//comentario
	COMMENT,
	
	//espaço em branco
	SPACE,
	
	//Fim do arquivo
	EOF
}
