package br.com.contabilidadereal.deccontrol.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.contabilidadereal.deccontrol.model.Declaracao;
import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.TipoPeriodo;
import br.com.contabilidadereal.deccontrol.repository.Declaracoes;
import br.com.contabilidadereal.deccontrol.service.CadastroDeclaracaoService;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroDeclaracaoException;

@Controller
public class DeclaracaoController {
	
	@Autowired
	private CadastroDeclaracaoService cadastroDeclaracaoService;
	
	@Autowired
	private Declaracoes declaracoes;
	
	@RequestMapping("/declaracoes/novo")
	public ModelAndView novo(Declaracao declaracao) {
		ModelAndView mv = new ModelAndView("declaracao/CadastroDeclaracao");
		mv.addObject("tiposPeriodo", TipoPeriodo.values());
		return mv;
	}
	@RequestMapping(value = "/declaracoes/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Declaracao declaracao, BindingResult result, RedirectAttributes attributes) {
		System.out.println(declaracao);
		if (result.hasErrors()) {
			return novo(declaracao);
		}
		
		try {
			cadastroDeclaracaoService.salvar(declaracao);
		}
		catch (CadastroDeclaracaoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(declaracao);
			}
		
		attributes.addFlashAttribute("mensagem", "Declaração salva com sucesso!");
		return new ModelAndView("redirect:/declaracoes/novo");
	}
	
	@RequestMapping("/declaracoes/consulta")
	public ModelAndView consulta(Declaracao declaracao) {
		ModelAndView mv = new ModelAndView("declaracao/consultaDeclaracao");
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}
	
	@GetMapping("/declaracoes/editar/{iddeclaracao}")
	public ModelAndView editarDeclaracao(@PathVariable("iddeclaracao") Integer id, Empresa empresa) {
		ModelAndView mv = new ModelAndView("declaracao/editarDeclaracao");
		mv.addObject("tiposPeriodo", TipoPeriodo.values());
		mv.addObject("declaracao", declaracoes.findByIdDec(id));
		return mv;
	}
	
	@PostMapping("/declaracoes/editar/{iddeclaracao}")
	public ModelAndView editarDeclaracaoPost(@PathVariable("iddeclaracao") Integer id, Empresa empresa,
			@Valid Declaracao declaracao, BindingResult result, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("declaracao/editarDeclaracao");
		mv.addObject("tiposPeriodo", TipoPeriodo.values());
		
		if (result.hasErrors()) {
			return novo(declaracao);
		}
		
		try {
			cadastroDeclaracaoService.salvar(declaracao);
			mv.addObject("declaracao", declaracoes.findByIdDec(id));
		}
		catch (CadastroDeclaracaoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(declaracao);
			}
		
		mv.addObject("mensagem", "Declaração salva com sucesso!");
		return mv;
	}
	
}
