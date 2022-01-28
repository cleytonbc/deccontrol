package br.com.contabilidadereal.deccontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Usuario;
import br.com.contabilidadereal.deccontrol.repository.Usuarios;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroUsuarioException;
import br.com.contabilidadereal.deccontrol.service.exception.UsuarioNaoEncontradoException;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private Usuarios usuarios;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		if (usuario.getId()==null) {
			if (usuarios.findByLogin(usuario.getLogin().toString())!=null) {
				throw new CadastroUsuarioException("Login já existente");
			}
			validaSenha(usuario.getSenha());
		}
		else {
			Usuario usuarioAtual = usuarios.findOne(usuario.getId());
			if (usuarioAtual==null) {
				throw new CadastroUsuarioException("Usuário não localizado");
			}
			if (!usuario.getLogin().equals(usuarioAtual.getLogin())) {
				throw new CadastroUsuarioException("Não é possível alterar o login do usuário");
			}
		}
		return usuarios.saveAndFlush(usuario);
	}
	
	public String senhaCripto(String senha) {
		String senhaCripto = new BCryptPasswordEncoder().encode(senha);
		return senhaCripto;
	}
	
	/*Redundante no momento por poder validar pela anotação, mas para futuras implementações de validação*/
	public boolean validaSenha(String senha) {
		if (senha.length()<6) {
			throw new CadastroUsuarioException("A senha deve ter no mínimo 6 caracteres");
		}
		return true;
	}
	
	public Usuario atualizaUsuario(Usuario usuario, Integer id) {
		Usuario usuarioAtual=usuarios.findOne(id);
		if (usuarioAtual==null) {
			throw new CadastroUsuarioException("Usuário não encontrado");
		}
		usuarioAtual.setNome(usuario.getNome());
		usuarioAtual.setSobrenome(usuario.getSobrenome());
		usuarioAtual.setEmail(usuario.getEmail());
		usuarioAtual.setAtivo(usuario.getAtivo());
		usuarioAtual.setRoles(usuario.getRoles());
		return usuarioAtual;
	}
	
	@Transactional
	public void atualizaTokenResetSenha(String token, String email) throws CadastroUsuarioException {
        Usuario usuario = usuarios.findByEmail(email);
        if (usuario != null) {
            usuario.setTokenResetSenha(token);
            usuarios.saveAndFlush(usuario);
        } else {
            throw new UsuarioNaoEncontradoException("Não foi possível encontrar nenhum usuário com esse e-mail " + email);
        }
    }
	
	public Usuario buscarPorTokenResetSenha(String token) {
		return usuarios.findByTokenResetSenha(token);
	}
	
	@Transactional
	 public void atualizaSenha(Usuario usuario, String novaSenha) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedSenha = passwordEncoder.encode(novaSenha);
	        usuario.setSenha(encodedSenha);
	         
	        usuario.setTokenResetSenha(null);
	        usuarios.saveAndFlush(usuario);
	    }
	
	public boolean comparaSenha(String senha, String senhaHash) {
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    if (passwordEncoder.matches(senha, senhaHash)) {
	    	return true;
	    }
	    return false;
	    
	}
	
}
