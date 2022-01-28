package br.com.contabilidadereal.deccontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Grupo;

@Repository
public interface Grupos extends JpaRepository<Grupo, Integer>{
	
	public Grupo findById(Integer id);
		
}
