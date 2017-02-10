/**
 * @author Carlos Olimpio
 */

package doxa.v0;

import java.io.InputStream;

public class Parser {
	private Lexer lexer;
	private Token currentToken;
	
	public Parser() {
		
	}
	
	public String parse(InputStream input) throws Exception {
		String r;
		
		try {
			lexer = new Lexer(input);
			currentToken = lexer.nextToken();
			
			parseProgram();
			acceptToken(TokenType.EOF);
			
			r = "OK";
		} catch (Exception e) {
			e.printStackTrace();
			r = e.getMessage();
		}
		return r;
	}
	
	private void acceptToken() throws Exception {
		currentToken = lexer.nextToken();
	}
	
	private void acceptToken(TokenType tp) throws Exception {
		if(currentToken.getType() == tp)
			currentToken = lexer.nextToken();
		else
			throw new Exception("Token inesperado: " 
							+ "foi lido um \"" + currentToken.getType()
							+ "\", quando o esperado era \"" + tp + "\". Linha: "
							+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
	}
	
	/**
	 * <programa> = <decl_global>*
	 * @throws Exception
	 */
	private void parseProgram() throws Exception {
		while(currentToken.getType() == TokenType.VAR
				|| currentToken.getType() == TokenType.PROC)
			parseDeclGlobal();
	}
	
	/**
	 * <decl_global> = <decl_variavel> | <decl_funcao>
	 * @throws Exception
	 */
	private void parseDeclGlobal() throws Exception {
		if(currentToken.getType() == TokenType.VAR)
			parseDeclVariavel();
		else if(currentToken.getType() == TokenType.PROC)
			parseDeclFuncao();
		else
			throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
					+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
	}
	
	/**
	 * <decl_variavel> = "var" <lista_idents> "-" <tipo> ";"
	 * @throws Exception
	 */
	private void parseDeclVariavel() throws Exception {
		acceptToken(TokenType.VAR);
		parseListaIdents();
		acceptToken(TokenType.SUB);
		parseTipo();
		acceptToken(TokenType.SYM_PT_VIRG);
	}
	
	/**
	 * <lista_idents> = IDENTIFICADOR ("," IDENTIFICADOR)*
	 * @throws Exception
	 */
	private void parseListaIdents() throws Exception {
		if(currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			while(currentToken.getType() == TokenType.SYM_VIRG) {
				acceptToken(TokenType.SYM_VIRG);
				acceptToken(TokenType.IDENTIFICADOR);
			}
		} else
			throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
					+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
	}
	
	/**
	 * <tipo> = "int" | "char" | "float"
	 * @throws Exception
	 */
	private void parseTipo() throws Exception {
		if(currentToken.getType() == TokenType.INT)
			acceptToken(TokenType.INT);
		else if(currentToken.getType() == TokenType.CHAR)
			acceptToken(TokenType.CHAR);
		else if(currentToken.getType() == TokenType.FLOAT)
			acceptToken(TokenType.FLOAT);
		else
			throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
					+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
	}
	
	/**
	 * <decl_funcao> = "proc" <nome_args> "-" <tipo> <bloco>
 	 *				| "proc" <nome_args> <bloco>
	 * @throws Exception
	 */
	private void parseDeclFuncao() throws Exception {
		acceptToken(TokenType.PROC);
		parseNomeArgs();
		if(currentToken.getType() == TokenType.SUB) {
			acceptToken(TokenType.SUB);
			parseTipo();
			parseBloco();
		} else 
			parseBloco();
	}
	
	/**
	 * <nome_args> = ( IDENTIFICADOR "(" <param_formais> ")" )+
	 * @throws Exception
	 */
	private void parseNomeArgs() throws Exception {
		do {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.SYM_ABRE_PAR);
			parseParamFormais();
			acceptToken(TokenType.SYM_FECHA_PAR);
		} while(currentToken.getType() == TokenType.IDENTIFICADOR);
	}
	
	/**
	 * <param_formais> = IDENTIFICADOR "-" <tipo> ( "," IDENTIFICADOR "-" <tipo> )*
 						| <vazio>
	 * @throws Exception
	 */
	private void parseParamFormais() throws Exception {
		if(currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.SUB);
			parseTipo();
			while(currentToken.getType() == TokenType.SYM_VIRG) {
				acceptToken(TokenType.SYM_VIRG);
				acceptToken(TokenType.IDENTIFICADOR);
				acceptToken(TokenType.SUB);
				parseTipo();
			}
		} else {
			/* produção vazia */
		}
	}
	
	/**
	 * <bloco> = "{" <lista_comandos> "}"
	 * @throws Exception
	 */
	private void parseBloco() throws Exception {
		acceptToken(TokenType.SYM_ABRE_CHAVE);
		parseListaComandos();
		acceptToken(TokenType.SYM_FECHA_CHAVE);
	}
	
	/**
	 * <lista_comandos> = (<comando>)*
	 * @throws Exception
	 */
	private void parseListaComandos() throws Exception {
		while(currentToken.getType() == TokenType.VAR
				|| currentToken.getType() == TokenType.IDENTIFICADOR
				|| currentToken.getType() == TokenType.WHILE
				|| currentToken.getType() == TokenType.IF
				|| currentToken.getType() == TokenType.PRNT
				|| currentToken.getType() == TokenType.RETURN
				|| currentToken.getType() == TokenType.SYM_ABRE_CHAVE)
			parseComando();
	}
	
	/**
	 * <comando> = <decl_variavel>
	 *			 | <atribuicao>
	 *			 | <iteracao>
	 *			 | <decisao>
	 *			 | <escrita>
	 *			 | <retorno>
	 *			 | <bloco>
	 *			 | <chamada_func_cmd>
	 * @throws Exception
	 */
	private void parseComando() throws Exception {
		if(currentToken.getType() == TokenType.VAR)
			parseDeclVariavel();
		else if(currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			if(currentToken.getType() == TokenType.ATRIBUICAO)
				parseAtribuicao();
			else if(currentToken.getType() == TokenType.SYM_ABRE_PAR)
				parseCmdChamadaFunc();
		}
		else if(currentToken.getType() == TokenType.WHILE)
			parseIteracao();
		else if(currentToken.getType() == TokenType.IF)
			parseDecisao();
		else if(currentToken.getType() == TokenType.PRNT)
			parseEscrita();
		else if(currentToken.getType() == TokenType.RETURN)
			parseRetorno();
		else if(currentToken.getType() == TokenType.SYM_ABRE_CHAVE)
			parseBloco();
			
	}
	
	/**
	 * <atribuicao> = IDENTIFICADOR ":=" <expressao> ";"
	 * @throws Exception
	 */
	private void parseAtribuicao() throws Exception {
		acceptToken(TokenType.ATRIBUICAO);
		parseExpressao();
		acceptToken(TokenType.SYM_PT_VIRG);
	}
	
	/**
	 * <chamada_func_cmd> = <chamada_func> ";"
	 * @throws Exception
	 */
	private void parseCmdChamadaFunc() throws Exception {
		parseChamadaFunc();
		acceptToken(TokenType.SYM_PT_VIRG);
	}
	
	/**
	 * <chamada_func> = (IDENTIFICADOR "(" <lista_exprs> ")")+
	 */
	private void parseChamadaFunc() throws Exception {
		acceptToken(TokenType.SYM_ABRE_PAR);
		parseListaExpressoes();
		acceptToken(TokenType.SYM_FECHA_PAR);
		
		while(currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			acceptToken(TokenType.SYM_ABRE_PAR);
			parseListaExpressoes();
			acceptToken(TokenType.SYM_FECHA_PAR);
		}
	}
	
	/**
	 * <lista_exprs> = <vazio> | <expressao> ("," <expressao>)*
	 * @throws Exception
	 */
	private void parseListaExpressoes() throws Exception {
		
		if(currentToken.getType() == TokenType.SYM_ABRE_PAR
				|| currentToken.getType() == TokenType.NOT
				|| currentToken.getType() == TokenType.SUB
				|| currentToken.getType() == TokenType.INT_LITERAL
				|| currentToken.getType() == TokenType.CHAR_LITERAL
				|| currentToken.getType() == TokenType.FLOAT_LITERAL
				|| currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken();
			parseExpressao();
			while(currentToken.getType() == TokenType.SYM_VIRG) {
				acceptToken();
				parseExpressao();
			}
		} else {
			/* produção vazia */
		}
	}
	
	/**
	 * <iteracao> = "while" "(" <expressao> ")" <comando>
	 * @throws Exception
	 */
	private void parseIteracao() throws Exception {
		acceptToken(TokenType.WHILE);
		acceptToken(TokenType.SYM_ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.SYM_FECHA_PAR);
		parseComando();
	}
	
	/**
	 * <decisao> = "if" "(" <expressao> ")" <comando> <fim_decisao>
	 * @throws Exception
	 */
	private void parseDecisao() throws Exception {
		acceptToken(TokenType.IF);
		acceptToken(TokenType.SYM_ABRE_PAR);
		parseExpressao();
		acceptToken(TokenType.SYM_FECHA_PAR);
		parseComando();
		parseFimDecisao();
	}
	
	/**
	 * <fim_decisao> = "else" <comando> | <vazio>
	 * @throws Exception
	 */
	private void parseFimDecisao() throws Exception {
		if(currentToken.getType() == TokenType.ELSE) {
			acceptToken(TokenType.ELSE);
			parseComando();
		} else {
			/* produção vazia */
		}
	}
	
	/**
	 * <escrita> = "prnt" "(" <lista_exprs> ")" ";"
	 * @throws Exception
	 */
	private void parseEscrita() throws Exception {
		acceptToken(TokenType.PRNT);
		acceptToken(TokenType.SYM_ABRE_PAR);
		parseListaExpressoes();
		acceptToken(TokenType.SYM_FECHA_PAR);
		acceptToken(TokenType.SYM_PT_VIRG);
	}
	
	/**
	 * <retorno> = "return" <expressao> ";"
	 * @throws Exception
	 */
	private void parseRetorno() throws Exception {
		acceptToken(TokenType.RETURN);
		parseExpressao();
		acceptToken(TokenType.SYM_PT_VIRG);
	}
	
	/**
	 * <expressao> = <expressao> "+" <expressao>
	 *			 | <expressao> "-" <expressao>
	 *			 | <expressao> "*" <expressao>
	 *			 | <expressao> "/" <expressao>
	 *			 | <expressao> "%" <expressao>
	 *			 | <expressao> "and" <expressao>
	 *			 | <expressao> "or" <expressao>
	 *			 | <expressao> "=" <expressao>
	 *			 | <expressao> "<>" <expressao>
	 *			 | <expressao> "<=" <expressao>
	 *			 | <expressao> "<" <expressao>
	 *			 | <expressao> ">=" <expressao>
	 *			 | <expressao> ">" <expressao>
	 *			 | <expr_basica>
	 * @throws Exception
	 */
	private void parseExpressao() throws Exception {
		parseExpr5();
		
		while(currentToken.getType() == TokenType.AND
				|| currentToken.getType() == TokenType.OR
				|| currentToken.getType() == TokenType.REL_IGUAL
				|| currentToken.getType() == TokenType.REL_MENOR_MAIOR
				|| currentToken.getType() == TokenType.REL_MENOR_IGUAL
				|| currentToken.getType() == TokenType.REL_MENOR
				|| currentToken.getType() == TokenType.REL_MAIOR_IGUAL
				|| currentToken.getType() == TokenType.REL_MAIOR
				|| currentToken.getType() == TokenType.ADD
				|| currentToken.getType() == TokenType.SUB
				|| currentToken.getType() == TokenType.MULT
				|| currentToken.getType() == TokenType.DIV
				|| currentToken.getType() == TokenType.MOD
				|| currentToken.getType() == TokenType.NOT
				|| currentToken.getType() == TokenType.SYM_ABRE_PAR
				|| currentToken.getType() == TokenType.INT_LITERAL
				|| currentToken.getType() == TokenType.CHAR_LITERAL
				|| currentToken.getType() == TokenType.FLOAT_LITERAL
				|| currentToken.getType() == TokenType.IDENTIFICADOR)
			parseExpr5();
	}
	
	/**
	 * <expr5> = <expr5> "and" <expr4>
	 *			| <expr5> "or" <expr4>
	 *			| <expr4>
	 * @throws Exception
	 */
	private void parseExpr5() throws Exception {
		parseExpr4();
		
		while(currentToken.getType() == TokenType.AND
				|| currentToken.getType() == TokenType.OR) {
			
			if(currentToken.getType() == TokenType.AND) {
				acceptToken(TokenType.AND);
			}
			else if(currentToken.getType() == TokenType.OR) {
				acceptToken(TokenType.OR);
			} else 
				throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
						+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
			
			parseExpr4();
		}
	}
	
	/**
	 * <expr4> = <expr4> "=" <expr3>
	 *			| <expr4> "<>" <expr3>
	 *			| <expr4> "<=" <expr3>
	 *			| <expr4> "<" <expr3>
	 *			| <expr4> ">=" <expr3>
	 *			| <expr4> ">" <expr3>
	 *			| <expr3>
	 * @throws Exception
	 */
	private void parseExpr4() throws Exception {
		parseExpr3();

		while(currentToken.getType() == TokenType.REL_IGUAL
				|| currentToken.getType() == TokenType.REL_MENOR_MAIOR
				|| currentToken.getType() == TokenType.REL_MENOR_IGUAL
				|| currentToken.getType() == TokenType.REL_MENOR
				|| currentToken.getType() == TokenType.REL_MAIOR_IGUAL
				|| currentToken.getType() == TokenType.REL_MAIOR) {

			if(currentToken.getType() == TokenType.REL_IGUAL) {
				acceptToken(TokenType.REL_IGUAL);
			}
			else if(currentToken.getType() == TokenType.REL_MENOR_MAIOR) {
				acceptToken(TokenType.REL_MENOR_MAIOR);
			}
			else if(currentToken.getType() == TokenType.REL_MENOR_IGUAL) {
				acceptToken(TokenType.REL_MENOR_IGUAL);
			}
			else if(currentToken.getType() == TokenType.REL_MENOR) {
				acceptToken(TokenType.REL_MENOR);
			}
			else if(currentToken.getType() == TokenType.REL_MAIOR_IGUAL) {
				acceptToken(TokenType.REL_MAIOR_IGUAL);
			}
			else if(currentToken.getType() == TokenType.REL_MAIOR) {
				acceptToken(TokenType.REL_MAIOR);
			} else
				throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
						+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());

			parseExpr3();
		}
	}
	
	/**
	 * <expr3> = <expr3> "+" <expr2>
	 *			| <expr3> "-" <expr2>
	 *			| <expr2>
	 * @throws Exception
	 */
	private void parseExpr3() throws Exception {
		parseExpr2();
		
		while(currentToken.getType() == TokenType.ADD
				|| currentToken.getType() == TokenType.SUB) {
			
			if(currentToken.getType() == TokenType.ADD) {
				acceptToken(TokenType.ADD);
			}
			else if(currentToken.getType() == TokenType.SUB) {
				acceptToken(TokenType.SUB);
			} else
				throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
						+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
			
			parseExpr2();
		}
	}
	
	/**
	 * <expr2> = <expr2> "*" <expr1>
	 *			| <expr2> "/" <expr1>
	 *			| <expr2> "%" <expr1>
	 *			| <expr1>
	 * @throws Exception
	 */
	private void parseExpr2() throws Exception {
		parseExpr1();
		
		while(currentToken.getType() == TokenType.MULT
				|| currentToken.getType() == TokenType.DIV
				|| currentToken.getType() == TokenType.MOD) {
			
			if(currentToken.getType() == TokenType.MULT) {
				acceptToken(TokenType.MULT);
			}
			else if(currentToken.getType() == TokenType.DIV) {
				acceptToken(TokenType.DIV);
			}
			else if(currentToken.getType() == TokenType.MOD) {
				acceptToken(TokenType.MOD);
			} else
				throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
						+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
			
			parseExpr1();
		}
	}
	
	/**
	 * <expr1> = <expr1> "not" <expr_basica>
	 * 			| <expr1> "-" <expr_basica>
	 * 			| <expr_basica>
	 * @throws Exception
	 */
	private void parseExpr1() throws Exception {
		parseExpressaoBasica();
		
		while(currentToken.getType() == TokenType.NOT
				|| currentToken.getType() == TokenType.SUB) {
			
			if(currentToken.getType() == TokenType.NOT) {
				acceptToken(TokenType.NOT);
			}
			else if(currentToken.getType() == TokenType.SUB) {
				acceptToken(TokenType.SUB);
			} else 
				throw new Exception("Token inesperado: " + currentToken.getType() + " Linha: "
						+ currentToken.getLinha() + " coluna: " + currentToken.getColuna());
			
			parseExpressaoBasica();
		}
	}
	
	/**
	 * <expr_basica> = "(" <expressao> ")"
	 *				 | "not" <expr_basica>
	 *				 | "-" <expr_basica>
	 *				 | INT_LITERAL
	 *				 | CHAR_LITERAL
	 *				 | FLOAT_LITERAL
	 *				 | IDENTIFICADOR
	 *				 | <chamada_func>
	 * @throws Exception
	 */
	private void parseExpressaoBasica() throws Exception {
		if(currentToken.getType() == TokenType.SYM_ABRE_PAR) {
			acceptToken(TokenType.SYM_ABRE_PAR);
			parseExpressao();
			acceptToken(TokenType.SYM_FECHA_PAR);
		}
		else if(currentToken.getType() == TokenType.NOT) {
			acceptToken(TokenType.NOT);
			parseExpressaoBasica();
		}
		else if(currentToken.getType() == TokenType.SUB) {
			acceptToken(TokenType.SUB);
			parseExpressaoBasica();
		}
		else if(currentToken.getType() == TokenType.INT_LITERAL)
			acceptToken(TokenType.INT_LITERAL);
		else if(currentToken.getType() == TokenType.CHAR_LITERAL)
			acceptToken(TokenType.CHAR_LITERAL);
		else if(currentToken.getType() == TokenType.FLOAT_LITERAL)
			acceptToken(TokenType.FLOAT_LITERAL);
		else if(currentToken.getType() == TokenType.IDENTIFICADOR) {
			acceptToken(TokenType.IDENTIFICADOR);
			if(currentToken.getType() == TokenType.SYM_ABRE_PAR) {
				parseChamadaFunc();
			}
			else {
				/* faz nada */
			}
		}
	}
}