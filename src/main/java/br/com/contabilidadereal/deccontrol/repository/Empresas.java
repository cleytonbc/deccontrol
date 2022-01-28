package br.com.contabilidadereal.deccontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Empresa;

@Repository
public interface Empresas extends JpaRepository<Empresa, Long> {
	
	public Optional<Empresa> findByNomeIgnoreCase(String nome);
	public Optional<Empresa> findByCodEmpAndCodEmpFilial(String codEmp, String codEmpFilial);
	public Empresa findById(Long id);
	public List<Empresa> findByAtiva(Boolean ativa);
	


}
