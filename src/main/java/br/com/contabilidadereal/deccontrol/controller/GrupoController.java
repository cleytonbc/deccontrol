package br.com.contabilidadereal.deccontrol.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.Grupo;
import br.com.contabilidadereal.deccontrol.model.GrupoEmpresa;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.repository.GrupoEmpresas;
import br.com.contabilidadereal.deccontrol.repository.Grupos;
import br.com.contabilidadereal.deccontrol.service.CadastroGrupoEmpresaService;
import br.com.contabilidadereal.deccontrol.service.CadastroGrupoService;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroGrupoEmpresaException;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroGrupoException;
import br.com.contabilidadereal.deccontrol.service.exception.VerificaEmpresaException;

@RestController
public class GrupoController {

	@Autowired
	private Grupos grupos;

	@Autowired
	private CadastroGrupoService cadastroGrupoService;

	@Autowired
	private GrupoEmpresas grupoEmpresas;

	@Autowired
	private CadastroGrupoEmpresaService cadastroGrupoEmpresaService;

	@Autowired
	private Empresas empresas;

	@GetMapping("/grupos/novo")
	public ModelAndView novo(Grupo grupo) {
		ModelAndView mv = new ModelAndView("grupo/cadastroGrupo");
		mv.addObject("grupoList", grupos.findAll());

		return mv;
	}

	@PostMapping("/grupos/novo")
	public ModelAndView cadastrar(@Valid Grupo grupo, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(grupo);
		}
		cadastroGrupoService.salvar(grupo);
		
		return new ModelAndView("redirect:/grupos/novo");
	}

	@GetMapping("/grupos/editar/{idgrupo}")
	public ModelAndView editargrupo(@PathVariable("idgrupo") Integer idgrupo, Grupo grupo) {
		ModelAndView mv = new ModelAndView("grupo/editarGrupo");
		mv.addObject("grupo", grupos.findById(idgrupo));
		return mv;
	}

	@GetMapping("/grupos/consulta")
	public ModelAndView consulta(Grupo grupo) {
		ModelAndView mv = new ModelAndView("grupo/consultaGrupo");
		mv.addObject("grupoList", grupos.findAll());

		return mv;
	}

	@GetMapping("/grupos/detalhe/{idgrupo}")
	public ModelAndView detalhe(@PathVariable("idgrupo") Integer idgrupo, @Valid GrupoEmpresa grupoEmpresa,
			@Valid Empresa empresa, BindingResult result, RedirectAttributes attributes) {

		ModelAndView mv = new ModelAndView("grupo/detalheGrupo");
		Grupo grupo = grupos.findById(idgrupo);
		List<Empresa> empTodas = cadastroGrupoEmpresaService.filtraEmpresaParaIncluir(grupo);
		List<GrupoEmpresa> empresaGrupoList = grupoEmpresas.findByGrupo(grupo);
		mv.addObject("grupo", grupo);
		mv.addObject("grupoEmpresas", empresaGrupoList);
		mv.addObject("empresas", empTodas);

		return mv;
	}

	@PostMapping("/grupos/detalhe/{idgrupo}")
	public ModelAndView detalheSalvar(@PathVariable("idgrupo") Integer idgrupo, @Valid GrupoEmpresa grupoEmpresa,
			@Valid Empresa empresa, BindingResult result, RedirectAttributes attributes,
			@RequestParam(value = "idEmp", required = false) int[] idEmp) {

		ModelAndView mv = new ModelAndView("grupo/detalheGrupo");
		Grupo grupo = null;
		try {
			grupo = cadastroGrupoService.buscarGrupoPorId(idgrupo);
			if (idEmp!=null) {
			
				List<Empresa> empresaListGravar = new ArrayList<Empresa>();
				if (idEmp.length > 0) {
					for (int i = 0; i < idEmp.length; i++) {
						empresaListGravar.add(empresas.findById((long) idEmp[i]));
					}
				}
				for (Empresa emp : empresaListGravar) {
					cadastroGrupoEmpresaService.salvar(grupo, emp);
					mv.addObject("mensagem", "Empresa(s) incluída(s) com sucesso");
					mv.addObject("alertClass", "alert-success");
				}
			}
			else {
				mv.addObject("mensagem", " Nenhuma empresa selecionada");
				mv.addObject("alertClass", "alert-danger");
			}
		}
		catch (CadastroGrupoException e) {
			mv.addObject("mensagem"," " + e.getMessage());
			mv.addObject("alertClass", "alert-danger");
		}
		catch(VerificaEmpresaException e){
			mv.addObject("mensagem"," " + e.getMessage());
			mv.addObject("alertClass", "alert-danger");
		}
		catch (CadastroGrupoEmpresaException e){
			mv.addObject("mensagem"," " + e.getMessage());
			mv.addObject("alertClass", "alert-danger");		
		}
		finally {
			
		List<Empresa> empTodas = cadastroGrupoEmpresaService.filtraEmpresaParaIncluir(grupo);
		mv.addObject("grupo", grupo);
		mv.addObject("empresas", empTodas);
		mv.addObject("grupoEmpresas", grupoEmpresas.findByGrupo(grupo));
		
		}
		return mv;
	}

	@GetMapping("/grupos/detalhe/{idgrupo}/delete/{idempresa}")
	@Transactional
	public ModelAndView detalheDelete(@PathVariable("idgrupo") Integer idgrupo,
			@PathVariable("idempresa") Long idempresa, @Valid GrupoEmpresa grupoEmpresa, @Valid Empresa empresa,
			BindingResult result, RedirectAttributes attributes) {
		
		Grupo grupoDelete = grupos.findById(idgrupo);
		Empresa empresaDelete = empresas.findById(idempresa);
		cadastroGrupoEmpresaService.deleteEmpresaGrupo(grupoDelete, empresaDelete);
		ModelAndView mv = new ModelAndView("redirect:/grupos/detalhe/{idgrupo}");
		attributes.addFlashAttribute("mensagem","Empresa excluída do grupo com sucesso");
		attributes.addFlashAttribute("alertClass", "alert-success");
		mv.addObject("idgrupo", grupoDelete.getId());
		return mv;
	}

}
