package br.com.contabilidadereal.deccontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.contabilidadereal.deccontrol.model.Usuario;
import br.com.contabilidadereal.deccontrol.repository.Usuarios;

@Service
public class ImplementUserDetailsService implements UserDetailsService{

	@Autowired
	private Usuarios usuarios;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Usuario usuario = usuarios.findByLogin(login);
		
		if (usuario==null) {
			throw new UsernameNotFoundException("Usuario não encontrado");
		}
		return usuario;
	}

}
