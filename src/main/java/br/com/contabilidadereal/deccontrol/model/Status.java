package br.com.contabilidadereal.deccontrol.model;

public enum Status {

	ENTREGUE("Entregue"),
	LIBERADA("Liberada"),
	ERRO("ERRO"),
	CANCELADA("CANCELADA");
		
	private String descricao;
	
	Status(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() { 
		return descricao;
	}
}

