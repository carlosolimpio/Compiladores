/**
 * @author Carlos Olimpio
 */

package doxa.v0;

public class Token {

	private TokenType tipo;
	private String lexema;
	private int linha;
	private int coluna;
	
	public Token(TokenType tipo) {
		this.tipo = tipo;
	}
	
	public Token(TokenType tipo, int linha, int coluna) {
		this.tipo = tipo;
		this.linha = linha + 1;
		this.coluna = coluna + 1;
	}

	public Token(TokenType tipo, String lexema) {
		this.tipo = tipo;
		this.lexema = lexema;
	}

	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	public void setToken(TokenType tipo) {
		this.tipo = tipo;
	}
	
	public void setLexeme(String lexeme) {
		this.lexema = lexeme;
	}
	
	public TokenType getType() {
		return tipo;
	}

	public String getLexeme() {
		return lexema;
	}

	public String toString() {
		if (lexema == null || lexema.length() == 0) {
			return "[" + tipo + "]";	
		} else
			return "[" + tipo + "," + lexema + "]";
	}	
}
