package br.com.contabilidadereal.deccontrol.model;

public enum TipoPeriodo {

	MENSAL("Mensal"),
	TRIMESTRAL("Trimestral"),
	SEMESTRAL("Semestral"),
	ANUAL("Anual");
	
	private String descricao;
	
	TipoPeriodo (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() { 
		return descricao;
	}		

}
