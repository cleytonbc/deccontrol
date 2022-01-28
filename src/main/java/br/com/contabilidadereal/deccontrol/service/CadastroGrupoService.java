package br.com.contabilidadereal.deccontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Grupo;
import br.com.contabilidadereal.deccontrol.repository.Grupos;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroGrupoEmpresaException;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroGrupoException;

@Service
public class CadastroGrupoService {
	@Autowired
	private Grupos grupos;
	
	@Transactional
	public Grupo salvar(Grupo grupo) {
		
		return grupos.saveAndFlush(grupo);
		
	}
	
	public Grupo verificaGrupoId(Grupo grupo) {
		Grupo grupoVerifica = grupos.findById(grupo.getId());
		if (grupoVerifica == null ) {
			throw new CadastroGrupoException("Grupo não encontrada");
		}
		return grupoVerifica;
	}
	
	public Grupo buscarGrupoPorId(Integer idgrupo) {
		
		Grupo grupo = grupos.findById(idgrupo);
		if (grupo==null) {
			throw new CadastroGrupoEmpresaException("Grupo não encontrado");
		}
		return grupo;
	}

}
