package br.com.contabilidadereal.deccontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	
	@RequestMapping("/login")
	  public String login() {
		return "login";
	    }
	
	// Login form with error
	  @RequestMapping("/login-error")
	  public String loginError(Model model) {
	    model.addAttribute("loginError", true);
	    return "login";
	  }
	  
	   @GetMapping("/acesonegado")
	    public String getAccessDenied() {
	        return "/erro/acessoNegado";
	    }
}
