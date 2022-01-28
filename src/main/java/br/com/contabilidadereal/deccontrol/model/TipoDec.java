package br.com.contabilidadereal.deccontrol.model;

public enum TipoDec {

	ORIGINAL("Original"),
	SUBSTITUTO("Substituto");
		
	private String descricao;
	
	TipoDec(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() { 
		return descricao;
	}
}
