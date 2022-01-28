package br.com.contabilidadereal.deccontrol.model;

import java.util.List;

public class EmpresaSelecao {
	
	private List<Empresa> empresasSelect;

	public List<Empresa> getEmpresasSelect() {
		return empresasSelect;
	}

	public void setEmpresasSelect(List<Empresa> empresasSelect) {
		this.empresasSelect = empresasSelect;
	}

	@Override
	public String toString() {
		return "EmpresaSelecao [empresasSelect=" + empresasSelect + "]";
	}
	
	
	
}
