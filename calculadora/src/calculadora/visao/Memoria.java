package calculadora.visao;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	
	private enum TipoComando {
			ZERAR,SOMA, NUMERO, DIV, MULT,SUB,IGUAL,VIRGULA,INVSINAL;
	};

	private static final Memoria instancia = new Memoria();
	private final List<MemoriaObservador> observadores= new ArrayList<>();
	
	private TipoComando ultimaOperacao= null;
	private boolean substituir = false;
	private String textoAtual = "";
	private String textoBuffer = "";
	
	private Memoria() {
	}
	public static Memoria getInstancia() {
		return instancia;
	}
	
	public void adicionarObservador(MemoriaObservador observador) {
		observadores.add(observador);
	}
	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0": textoAtual;
	}

	public void processarComando(String texto) {
		
		TipoComando tipoComando = detectarTipoComando(texto);
				
			if(tipoComando == null) {
				return;
			}else if (tipoComando == TipoComando.ZERAR) {
				textoAtual ="";
				textoBuffer ="";
				substituir = false;
				ultimaOperacao = null;
			}else if(tipoComando == TipoComando.NUMERO || tipoComando == TipoComando.VIRGULA) {
				textoAtual = substituir ? texto:textoAtual + texto;
				substituir = false;
			}else if(tipoComando == TipoComando.INVSINAL && textoAtual.contains("-")) {
				textoAtual = textoAtual.substring(1);
			}else if(tipoComando == TipoComando.INVSINAL && !textoAtual.contains("-")) {
				textoAtual = "-" + textoAtual;
			}else {
				substituir = true;
				textoAtual = obterResultadoOperacao();
				textoBuffer = textoAtual;
				ultimaOperacao = tipoComando;
			}
		
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}
	
	private String obterResultadoOperacao() {
		if(ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		double numeroBuffer = Double.parseDouble(textoBuffer);
		double numeroAtual = Double.parseDouble(textoAtual);
		
		double resultado = 0;
		
		if(ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		}else if(ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}else if(ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		}else if(ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		}
		String resultadoString = Double.toString(resultado);
		boolean inteiro = resultadoString.endsWith(".0");
		return inteiro ? resultadoString.replace(".0", "") : resultadoString;
	}
	private TipoComando detectarTipoComando(String texto) {
		
		if(textoAtual.isEmpty()&& texto == "0") {
			return null;
		}else if (textoAtual.isEmpty()&& texto == "."){
			return null;
		}
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			//Quando n for numero
			if ("AC".equals(texto)) {
				return TipoComando.ZERAR;
			}else if ("/".equals(texto)) {
				return TipoComando.DIV;
			}else if ("*".equals(texto)) {
				return TipoComando.MULT;
			}else if ("+".equals(texto)) {
				return TipoComando.SOMA;
			}else if ("-".equals(texto)) {
				return TipoComando.SUB;
			}else if ("=".equals(texto)) {
				return TipoComando.IGUAL;
			}else if (".".equals(texto) && !textoAtual.contains(".")) {
				return TipoComando.VIRGULA;
			}else if ("+/-".equals(texto)){
				return TipoComando.INVSINAL;
		}}
		return null;
	}
}
	

