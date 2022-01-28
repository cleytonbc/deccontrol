package br.com.contabilidadereal.deccontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Role;

@Repository
public interface Roles extends JpaRepository<Role, Integer>{
	public Role findById(Integer id);
	

}
