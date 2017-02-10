/**
 * @author Carlos Olimpio
 */
package doxa.v0.tests;

import doxa.v0.Lexer;
import doxa.v0.Token;
import doxa.v0.TokenType;

public class TestLexer {
	public static void main(String[] args) throws Exception {
		Lexer lexer;
		Token token;
		
		System.out.println("\n\n\n");
		System.out.println(" == TESTE DO LEXER ==\n");
		System.out.println(" Digite alguma string terminada em \";\" e tecle ENTER:\n\n");
		System.out.print(" ");

		// passa a entrada padrão (teclado) para o lexer
		lexer = new Lexer(System.in);
		
		// descomente aqui para usar entrada por meio de arquivo
		//lexer = new Lexer(new FileInputStream("exemplo1"));
		
		do {
			token = lexer.nextToken();
			System.out.println("\t" + token);
		
		} while (token.getType() != TokenType.SYM_PT_VIRG);
		
		System.out.println("\n == FIM ==");
	}
}
