package br.com.contabilidadereal.deccontrol.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.contabilidadereal.deccontrol.model.Usuario;
import br.com.contabilidadereal.deccontrol.service.CadastroUsuarioService;
import br.com.contabilidadereal.deccontrol.service.EnviarEmailService;
import br.com.contabilidadereal.deccontrol.service.exception.UsuarioNaoEncontradoException;
import br.com.contabilidadereal.deccontrol.util.buscarUrl;
import net.bytebuddy.utility.RandomString;

@Controller
public class  EsqueceuSenhaController {
	
	private static final Logger logger = LoggerFactory.getLogger(EsqueceuSenhaController.class);	
	
     
	@Autowired
	private EnviarEmailService enviarsendEmail;
	
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;
     
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "senha/formEsqueceuSenha";
    }
 
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
         
        try {
            cadastroUsuarioService.atualizaTokenResetSenha(token, email);
            String resetPasswordLink = buscarUrl.getSiteURL(request) + "/reset_password?token=" + token;
 //           emailService.enviaEmailEsqueceuSenha(email, resetPasswordLink);
            enviarsendEmail.enviaEmail(email, resetPasswordLink);
            model.addAttribute("mensagem", "Enviamos um link de redefinição de senha para o seu e-mail. Por favor, verifique seu e-mail");
            logger.info("Enviado e-mail de redefinição de senha para " + email);
             
        } catch (UsuarioNaoEncontradoException ex) {
            model.addAttribute("erro", ex.getMessage());
            logger.error(ex.getMessage());
       }
             
        return "senha/formEsqueceuSenha";
    }
     
     
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Usuario usuario = cadastroUsuarioService.buscarPorTokenResetSenha(token);
        model.addAttribute("token", token);
        
        if (token==null) {
        	return "redirect:/forgot_password";
        }
        
        if (usuario == null) {
            model.addAttribute("mensagem", "Token inválido");
            return "senha/mensagem";
        }
         
        return "senha/resetSenha";
    }
     
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
   
        Usuario usuario = cadastroUsuarioService.buscarPorTokenResetSenha(token);
    
        model.addAttribute("title", "Redefina sua senha");
         
        if (usuario == null) {
            model.addAttribute("erro", "Token inválido");
            return "senha/mensagem";
        } else {           
            cadastroUsuarioService.atualizaSenha(usuario, password);
            model.addAttribute("mensagem", "Sua senha foi alterada com sucesso");
        }
         
        return "senha/mensagem";
    }
}