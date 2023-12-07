package fabio.app.calculo;

import fabio.app.calculo.interno.OperacoesAritmeticas;
import fabio.app.logging.Logger;

public class Calculadora {

	private OperacoesAritmeticas opAritmeticas = new OperacoesAritmeticas();

	public double soma(double... nums) {
		Logger.info("Somando...");
		
		return opAritmeticas.soma(nums);
	}
	

}
