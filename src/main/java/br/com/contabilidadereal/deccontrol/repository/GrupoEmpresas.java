package br.com.contabilidadereal.deccontrol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.Grupo;
import br.com.contabilidadereal.deccontrol.model.GrupoEmpresa;

@Repository
public interface GrupoEmpresas extends JpaRepository<GrupoEmpresa, Long>{
	
	public List<GrupoEmpresa> findByGrupo(Grupo grupo);
	public GrupoEmpresa findById(Long id);
	public GrupoEmpresa findByGrupoAndEmpresa(Grupo grupo, Empresa empresa);
	public List<GrupoEmpresa> findByEmpresa(Empresa empresa);

}
