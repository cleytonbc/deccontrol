package br.com.contabilidadereal.deccontrol.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.contabilidadereal.deccontrol.model.Role;
import br.com.contabilidadereal.deccontrol.model.Usuario;
import br.com.contabilidadereal.deccontrol.repository.Roles;
import br.com.contabilidadereal.deccontrol.repository.Usuarios;
import br.com.contabilidadereal.deccontrol.service.CadastroUsuarioService;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroUsuarioException;

@Controller
public class UsuariosController {
	
	private static final Logger logger = LoggerFactory.getLogger(UsuariosController.class);
	
	@Autowired
	private Roles roles;
	@Autowired
	private Usuarios usuarios;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@GetMapping("/usuarios/novo")
	  public ModelAndView novoUsuario(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/cadastroUsuario");
		mv.addObject("allRoles",roles.findAll());
	    return mv;
	    }
	
	@PostMapping("/usuarios/novo")
	public ModelAndView cadastrarUsuario(@Valid Usuario usuario, BindingResult result, Model model,
			RedirectAttributes attributes, @RequestParam(value = "idroles", required = false) int[] idRoles) {
		ModelAndView mv = new ModelAndView("usuario/cadastroUsuario");
		mv.addObject("allRoles",roles.findAll());
		if (idRoles.length<0) {
			return mv;
		}
		
		if (result.hasErrors()) {
			return novoUsuario(usuario);
		}
		
		usuario.setAtivo(true);
		List<Role> rolesDefinidas = new ArrayList<Role>();
		for (int i: idRoles) {
			rolesDefinidas.add(roles.findById(i));
		}
		usuario.setSenha(cadastroUsuarioService.senhaCripto(usuario.getSenha()));
		usuario.setRoles(rolesDefinidas);
		try {
		cadastroUsuarioService.salvar(usuario);
		}
		catch (CadastroUsuarioException e) {
			mv.addObject("menssagemErro",e.getMessage());
			logger.error("Erro ao salvar usuário, motio: "+e.getMessage());
		}
				
		return mv;
	}
	
	@GetMapping("/usuarios/consulta")
	  public ModelAndView consulta() {
		ModelAndView mv = new ModelAndView("usuario/consultaUsuario");
		mv.addObject("usuarios",usuarios.findAll());
	    return mv;
	    }
	
	
	@GetMapping("/usuarios/editar/{idusuario}")
	  public ModelAndView editar(@PathVariable("idusuario") Integer id) {
		ModelAndView mv = new ModelAndView("usuario/editarUsuario");
		mv.addObject("usuario",usuarios.findOne(id));
		mv.addObject("allRoles",roles.findAll());
		mv.addObject("roleChecked",usuarios.findOne(id).getRoles());
	    return mv;
	    }

	@PostMapping("/usuarios/editar/{idusuario}")
	public ModelAndView editar(@PathVariable("idusuario") Integer id, @Valid Usuario usuario, BindingResult result, Model model,
			RedirectAttributes attributes, @RequestParam(value = "roles", required = false) int[] idRoles) {
		ModelAndView mv = new ModelAndView("usuario/editarUsuario");
		mv.addObject("allRoles",roles.findAll());
		mv.addObject("roleChecked",usuarios.findOne(id).getRoles());
		List<Role> rolesDefinidas = new ArrayList<Role>();
		for (int i: idRoles) {
			rolesDefinidas.add(roles.findOne(i));
		}
		usuario.setRoles(rolesDefinidas);
		usuario = cadastroUsuarioService.atualizaUsuario(usuario, id);
		try {
		cadastroUsuarioService.salvar(usuario);
		mv.addObject("roleChecked",usuarios.findOne(id).getRoles());
		mv.addObject("mensagem","Usuário atualizado com sucesso");
		}
		catch (CadastroUsuarioException e) {
			mv.addObject("menssagemErro",e.getMessage());
		}
		mv.addObject("usuario",usuarios.findOne(id));
		
		return mv;
	}
	
	@GetMapping("/minhaconta")
		public ModelAndView minhaConta(HttpServletRequest request, Authentication authentication, Principal principal) {
			ModelAndView mv = new ModelAndView("conta/minhaconta");
			Usuario usuario = (Usuario)authentication.getPrincipal();
			mv.addObject("usuario",usuario);
			
			return mv;
	}
	@PostMapping("/minhaconta")
	public ModelAndView minhaContaSalvar(HttpServletRequest request, Usuario usuario, Authentication authentication) {
		ModelAndView mv = new ModelAndView("conta/minhaconta");
		String senhaAtual = request.getParameter("senhaAtual");
		String novaSenha = request.getParameter("novaSenha");
		String confirmaSenha = request.getParameter("confirmaSenha");
				
		Usuario usuarioLogado = (Usuario)authentication.getPrincipal();
	//	senhaAtual=cadastroUsuarioService.senhaCripto(senhaAtual);
		if (!cadastroUsuarioService.comparaSenha(senhaAtual, usuarioLogado.getSenha())) {
			mv.addObject("erro", "A senha digitada está incorreta");
			return mv;
		}
		usuarioLogado.setEmail(usuario.getEmail());
		usuarioLogado.setNome(usuario.getNome());
		usuarioLogado.setSobrenome(usuario.getSobrenome());
		if (!novaSenha.isEmpty()) {
			cadastroUsuarioService.validaSenha(novaSenha);
			usuarioLogado.setSenha(cadastroUsuarioService.senhaCripto(novaSenha));
		}
		usuario = cadastroUsuarioService.salvar(usuarioLogado);
		mv.addObject("usuario",usuario);
		mv.addObject("mensagem","Dados atualizado com sucesso!");
		return mv;
}
	
}
