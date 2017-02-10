/**
 * @author Carlos Olimpio
 */
package doxa.v0.tests;

import java.io.FileInputStream;
import java.io.InputStream;

import doxa.v0.Parser;

public class TestParser {

	/**
	 * Este metodo executa um pequeno teste de reconhecimento.  
	 */
	public static void main(String[] args) throws Exception {
		Parser parser = new Parser();
		InputStream entrada;

		System.out.println(" == TESTE DO PARSER ==\n");

		//PARA TESTAR USANDO ARQUIVO:
		entrada = new FileInputStream("exemplo_3.txt");
		
		String msg = parser.parse(entrada);

		
		System.out.println(" >>" + msg + "\n");
		
		System.out.println(" == FIM ==");
	}
	
}
