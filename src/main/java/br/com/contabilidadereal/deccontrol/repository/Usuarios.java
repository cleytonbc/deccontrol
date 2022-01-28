package br.com.contabilidadereal.deccontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Usuario;

@Repository
public interface Usuarios extends JpaRepository<Usuario, Integer> {

	public Usuario findByLogin(String login);
	public Usuario findByEmail(String email);
	public Usuario findByTokenResetSenha(String token);
	
}
