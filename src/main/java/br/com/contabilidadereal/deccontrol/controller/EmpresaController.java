package br.com.contabilidadereal.deccontrol.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.service.CadastroEmpresaService;
import br.com.contabilidadereal.deccontrol.service.exception.JaCadastradoException;

@RestController
public class EmpresaController {

	@Autowired
	private Empresas empresas;

	@Autowired
	private CadastroEmpresaService cadastroEmpresaService;

	@GetMapping("/empresas/novo")
	public ModelAndView novo(Empresa empresa) {
		return new ModelAndView("empresa/CadastroEmpresa");
	}

	@PostMapping("/empresas/novo")
	public ModelAndView cadastrar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			return novo(empresa);
		}
		try {
			cadastroEmpresaService.salvar(empresa);
		} catch (JaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(empresa);
		}
		attributes.addFlashAttribute("mensagem", "Empresa salva com sucesso!");
		return new ModelAndView("redirect:/empresas/consulta");
	}

	@GetMapping("/empresas/editar/{idempresa}")
	public ModelAndView editarEmpresa(@PathVariable("idempresa") long idempresa, Empresa empresa) {
		ModelAndView mv = new ModelAndView("empresa/editarEmpresa");
		mv.addObject("empresa", empresas.findById(idempresa));
		return mv;
	}

	@PostMapping("/empresas/editar/{idempresa}")
	public ModelAndView editarSalvarEmpresa(@PathVariable("idempresa") long idempresa, @Valid Empresa empresa,
			BindingResult result, RedirectAttributes attributes) {
		empresa.setId(idempresa);
		if (result.hasErrors()) {
			return novo(empresa);
		}
		try {
			cadastroEmpresaService.salvar(empresa);
		} catch (JaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(empresa);
		}

		attributes.addFlashAttribute("mensagem", "Empresa alterada com sucesso!");
		return new ModelAndView("redirect:/empresas/editar/{idempresa}");
	}

	@GetMapping(value = "/empresas/listarAtivas", produces = "application/json")
	@ResponseBody
	public List<Empresa> empresaTodas() {
		List<Empresa> empresaTodas = empresas.findAll();
		List<Empresa> empresaAtivas = new ArrayList<Empresa>();
		for (Empresa empresa : empresaTodas) {
			if (empresa.getAtiva().booleanValue() == true) {
				empresaAtivas.add(empresa);
			}
		}
		return empresaAtivas;
	}

	@RequestMapping(value = "/empresas/lista")
	public ModelAndView listarEmpresas() {

		return new ModelAndView("empresa/listarEmpresas");
	}

	@GetMapping("/empresas/editar")
	public ModelAndView editar(Empresa empresa) {
		System.out.println(empresas.findAll());
		return new ModelAndView("empresa/editarListaEmpresa");
	}

	@GetMapping("/empresas/consulta")
	public ModelAndView consultaEmpresa() {
		ModelAndView mv = new ModelAndView("empresa/consultaEmpresa");
		mv.addObject("listaEmpresas", empresas.findAll());
		return mv;
	}

}
