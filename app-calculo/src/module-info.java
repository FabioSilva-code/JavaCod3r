module appcalculo {
	exports fabio.app.calculo;
	requires transitive app.logging;
	
	exports fabio.app.calculo.interno
		to appfinanceiro;
}