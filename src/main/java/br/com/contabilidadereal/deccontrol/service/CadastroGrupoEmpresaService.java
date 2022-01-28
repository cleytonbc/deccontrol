package br.com.contabilidadereal.deccontrol.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.Grupo;
import br.com.contabilidadereal.deccontrol.model.GrupoEmpresa;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.repository.GrupoEmpresas;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroGrupoEmpresaException;

@Service
public class CadastroGrupoEmpresaService {

	@Autowired
	private GrupoEmpresas grupoEmpresas;

	@Autowired
	private CadastroEmpresaService cadastroEmpresaService;

	@Autowired
	private CadastroGrupoService cadastroGrupoService;

	@Autowired
	private Empresas empresas;


	@Transactional
	public GrupoEmpresa salvar(Grupo grupo, Empresa empresa) {
		empresa = cadastroEmpresaService.verificaEmpresaAtiva(empresa);
		grupo = cadastroGrupoService.verificaGrupoId(grupo);
		verificarEmpresaNoGrupo(grupo, empresa);
		GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
		grupoEmpresa.setGrupo(grupo);
		grupoEmpresa.setEmpresa(empresa);
		return grupoEmpresas.saveAndFlush(grupoEmpresa);
	}
	
	public GrupoEmpresa verificarEmpresaNoGrupo(Grupo grupo, Empresa empresa) {
		System.out.println(grupo);
		System.out.println(empresa);
		GrupoEmpresa grupoEmpresa = grupoEmpresas.findByGrupoAndEmpresa(grupo, empresa);
		System.out.println(grupoEmpresa);
		if (grupoEmpresa != null) {
			throw new CadastroGrupoEmpresaException("Empresa já cadastrada nesse grupo");
		}
		return grupoEmpresa;
	}

	public List<Empresa> filtraEmpresaParaIncluir(Grupo grupo) {

		List<Empresa> empresasFiltradas = empresas.findByAtiva(true);
		List<GrupoEmpresa> empresasDoGrupo = grupoEmpresas.findByGrupo(grupo);
		for (int i = 0; i < empresasFiltradas.size(); i++) {
			for (GrupoEmpresa empGrup : empresasDoGrupo) {
				if (empresasFiltradas.get(i).equals(empGrup.getEmpresa())) {
					empresasFiltradas.remove(i);
				}
			}
		}
		return empresasFiltradas;
	}
	
	@Transactional
	public void deleteEmpresaGrupo(Grupo grupo, Empresa empresa) {
		cadastroGrupoService.verificaGrupoId(grupo);
		cadastroEmpresaService.verificaEmpresaId(empresa);
		GrupoEmpresa grupoEmpresa = grupoEmpresas.findByGrupoAndEmpresa(grupo, empresa);
		grupoEmpresas.delete(grupoEmpresa);
		
	}
	
	public List<GrupoEmpresa> somenteAtivasDoGrupo(Grupo grupo){
		List<GrupoEmpresa> grupoEmpresa = grupoEmpresas.findByGrupo(grupo);
		List<GrupoEmpresa> grupoEmpresaAtivas =new ArrayList<GrupoEmpresa>();
		for (GrupoEmpresa grupEmp : grupoEmpresa) {
			if (grupEmp.getEmpresa().getAtiva()) {
				grupoEmpresaAtivas.add(grupEmp);
			}
		}
	return grupoEmpresaAtivas;
	}
}
