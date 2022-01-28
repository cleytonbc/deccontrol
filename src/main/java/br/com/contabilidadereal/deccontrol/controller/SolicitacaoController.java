package br.com.contabilidadereal.deccontrol.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.contabilidadereal.deccontrol.model.Declaracao;
import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.Grupo;
import br.com.contabilidadereal.deccontrol.model.GrupoEmpresa;
import br.com.contabilidadereal.deccontrol.model.Solicitacao;
import br.com.contabilidadereal.deccontrol.model.Status;
import br.com.contabilidadereal.deccontrol.model.TipoDec;
import br.com.contabilidadereal.deccontrol.model.Usuario;
import br.com.contabilidadereal.deccontrol.repository.Declaracoes;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.repository.Grupos;
import br.com.contabilidadereal.deccontrol.repository.Solicitacoes;
import br.com.contabilidadereal.deccontrol.service.CadastroGrupoEmpresaService;
import br.com.contabilidadereal.deccontrol.service.CadastroSolicitacaoService;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroDeclaracaoException;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroSolicitacaoException;
import br.com.contabilidadereal.deccontrol.service.exception.VerificaEmpresaException;
import br.com.contabilidadereal.deccontrol.util.ConverterStringLocalDateTime;

@Controller
public class SolicitacaoController {
	
	private static final Logger logger = LoggerFactory.getLogger(SolicitacaoController.class);
	@Autowired
	private Empresas empresas;

	@Autowired
	private CadastroSolicitacaoService cadastroSolicitacaoService;

	@Autowired
	private CadastroGrupoEmpresaService cadastroGrupoEmpresaService;

	@Autowired
	private Solicitacoes solicitacoes;

	@Autowired
	private Declaracoes declaracoes;
	

	@Autowired
	private Grupos grupos;
	
	@GetMapping("/")
	public String dashboard(Model model, HttpServletRequest request, Authentication authentication, Principal principal) {
		model.addAttribute("dashboardPendentes", solicitacoes.countByStatus(Status.LIBERADA));
		model.addAttribute("dashboardErros", solicitacoes.countByStatus(Status.ERRO));
		model.addAttribute("dashboardTotal", solicitacoes.countByStatus(Status.ERRO)+solicitacoes.countByStatus(Status.LIBERADA));
		model.addAttribute("dashboardTotalPendente", solicitacoes.countByStatus(Status.ERRO)+solicitacoes.countByStatus(Status.LIBERADA));
		model.addAttribute("dashboardRetificadoras", solicitacoes.countByTipoDecAndStatus(TipoDec.SUBSTITUTO, Status.LIBERADA) + solicitacoes.countByTipoDecAndStatus(TipoDec.SUBSTITUTO, Status.ERRO));
		ConverterStringLocalDateTime converterData = new ConverterStringLocalDateTime();
		model.addAttribute("dashboardTotalMesEntregue",
				solicitacoes.countByDataSolicitacaoBetweenAndStatus(
						converterData.convertInicioMes(YearMonth.now().toString()),
						converterData.convertFimMes(YearMonth.now().toString()), Status.ENTREGUE));
		model.addAttribute("dashboardTotalMes",
				solicitacoes.countByDataSolicitacaoBetween(converterData.convertInicioMes(YearMonth.now().toString()),
						converterData.convertFimMes(YearMonth.now().toString())));
		return "dashboard";
	}
	
/** Início Fragmento das notificações de Erro e Entregue **/
	
	@RequestMapping(value="/api/consultaPendente", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> apiPendente() {
		Integer count = 0;
		count = count + solicitacoes.countByStatus(Status.ERRO);
		count =count + solicitacoes.countByStatus(Status.LIBERADA);
		logger.error(String.valueOf(count));
	/*	String url1 = ("https://api.callmebot.com/whatsapp.php?phone=+5524999080232&text="+"Declarações pendentes para entrega "+count+"&apikey=255698");
		url1 = url1.replace(" ", "+");
		logger.error(url1);
		try {
			
			URL url = new URL(url1);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.connect();
			Scanner scanner = new Scanner(con.getInputStream());
			String jsonDeResposta = scanner.toString();
			logger.error(jsonDeResposta);
			}
			catch (Exception e) {
				logger.error("falhou");
			}*/
		
		return ResponseEntity.ok(count);
	}
	
	@RequestMapping("/solicitacao/countErro")
	public String countErro(Model model) {
		if (solicitacoes.countByStatus(Status.ERRO) > 0) {
			model.addAttribute("countErro", solicitacoes.countByStatus(Status.ERRO).toString());
		}
		return "solicitacao/fragmentsCount/countErro :: countErro";
	}

	@RequestMapping("/solicitacao/countLiberada")
	public String countLiberada(Model model) {
		if (solicitacoes.countByStatus(Status.LIBERADA) > 0) {
			model.addAttribute("countLiberada", solicitacoes.countByStatus(Status.LIBERADA));
		}
		return "solicitacao/fragmentsCount/countLiberada :: countLiberada";
	}
	
	
/**FIM Fragmento das notificações de Erro e Entregue **/
	
/**INICIO DAS NOVAS SOLICITAÇÔES**/

	@GetMapping("/solicitacao/novo")
	public ModelAndView novo(Solicitacao solicitacao) {
		ModelAndView mv = new ModelAndView("solicitacao/cadastroSolicitacao");
		mv.addObject("tiposdec", TipoDec.values());
		mv.addObject("empresas", empresas.findByAtiva(true));
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}

	@PostMapping("/solicitacao/novo")
	public ModelAndView cadastrar(@Valid Solicitacao solicitacao, BindingResult result, Model model,
			RedirectAttributes attributes, Authentication authentication) {

		try {
			cadastroSolicitacaoService.verificaEmpresa(solicitacao);
		} catch (VerificaEmpresaException e) {
			result.rejectValue("empresa", e.getMessage(), e.getMessage());
			return novo(solicitacao);
		}

		if (result.hasErrors()) {
			return novo(solicitacao);
		}
		
		try {
			Usuario usuario=(Usuario)authentication.getPrincipal();
			solicitacao.setDeclaracao(declaracoes.findByIdDec(solicitacao.getDeclaracao().getIdDec()));
			solicitacao.setStatus(Status.LIBERADA);
			solicitacao.setUsuarioSolicita(usuario);
			if  (solicitacao.getTipoDec().equals(TipoDec.ORIGINAL)) {
				cadastroSolicitacaoService.verificaJaCadastrada(solicitacao);
			}
			cadastroSolicitacaoService.salvar(solicitacao);
		} catch (VerificaEmpresaException e) {
			result.rejectValue("empresa", e.getMessage(), e.getMessage());
			return novo(solicitacao);
		} catch (CadastroSolicitacaoException e) {
			result.rejectValue("competencia", e.getMessage(), e.getMessage());
			return novo(solicitacao);
		}

		attributes.addFlashAttribute("mensagem", "Solicitação salva com sucesso!");
		return new ModelAndView("redirect:/solicitacao/novo");
	}
	
	@GetMapping("/solicitacao/novolote")
	public ModelAndView emLote(Solicitacao solicitacao) {
		ModelAndView mv = new ModelAndView("solicitacao/emlote");
       mv.addObject("grupoList", grupos.findAll());
			return mv;
	}
	
	@GetMapping("/solicitacao/emlote/{idgrupo}")
	public ModelAndView emLoteGrupo(@PathVariable("idgrupo") Integer idgrupo,
			@Valid GrupoEmpresa grupoEmpresa,
			@Valid Empresa empresa, BindingResult result,
			RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("solicitacao/cadastroEmLote");
		Grupo grupo = grupos.findById(idgrupo);
		if (grupo==null) {
			mv.addObject("mensagem","Grupo não encontrado");
			return mv;
		}
		mv.addObject("tiposdec", TipoDec.values());
		mv.addObject("declaracoes", declaracoes.findAll());
		mv.addObject("grupoEmpresas", cadastroGrupoEmpresaService.somenteAtivasDoGrupo(grupo));
		return mv;
	}
	
	@PostMapping("/solicitacao/emlote/{idgrupo}")
	public ModelAndView emLoteSalvaar(@PathVariable("idgrupo") Integer idgrupo, @Valid GrupoEmpresa grupoEmpresa,
			@Valid Empresa empresa, BindingResult result, RedirectAttributes attributes,
			Authentication authentication,
			@RequestParam(value = "idEmp", required = false) int[] idEmp,
			@RequestParam(value = "declaracao", required = true) int declaracao,
			@RequestParam(value = "tipoDec", required = true) String tipoDec,
			@RequestParam(value = "competencia", required = true) String competencia) {
		Usuario usuario=(Usuario)authentication.getPrincipal();
		if (idEmp==null) {
			attributes.addFlashAttribute("mensagem","Nenhuma empresa selecionada");
			ModelAndView mv = new ModelAndView("redirect:/solicitacao/emlote/{idgrupo}");
			mv.addObject("idgrupo",idgrupo);
			return mv ;
		}
		
		ModelAndView mv = new ModelAndView("solicitacao/cadastroEmLote");
		List<Empresa> empresasGravadas = new ArrayList<Empresa>();
		List<Empresa> empresasErros = new ArrayList<Empresa>();
		Grupo grupo = grupos.findById(idgrupo);
		

		for (int i = 0; i < idEmp.length; i++) {
			Solicitacao solicitacao = new Solicitacao();
			solicitacao.setCompetencia(competencia);
			solicitacao.setUsuarioSolicita(usuario);
			solicitacao.setDeclaracao(declaracoes.findByIdDec(declaracao));
			solicitacao.setStatus(Status.LIBERADA);
			solicitacao.setObservacao("");
			if (tipoDec.equals("ORIGINAL")) {
				solicitacao.setTipoDec(TipoDec.ORIGINAL);
			}
			else if (tipoDec.equals("SUBSTITUTO"))
			{
				solicitacao.setTipoDec(TipoDec.SUBSTITUTO);
			}
			Empresa empresaId = empresas.findById((long) idEmp[i]);
			solicitacao.setEmpresa(empresaId);
			
			try {
				if (cadastroSolicitacaoService.verificaJaCadastradaEmLote(solicitacao)) {
					Solicitacao solicitacaoSalva = cadastroSolicitacaoService.salvar(solicitacao);
					empresasGravadas.add(solicitacaoSalva.getEmpresa());
				}
				else  {
					empresasErros.add(empresaId);
				}
			}catch (CadastroDeclaracaoException e) {
				mv.addObject("mensagem", e.getMessage());
				
			}
		}
		
		mv.addObject("empresasErros", empresasErros);
		mv.addObject("empresasGravadas", empresasGravadas);
		mv.addObject("tiposdec", TipoDec.values());
		mv.addObject("declaracoes", declaracoes.findAll());
		mv.addObject("grupoEmpresas", cadastroGrupoEmpresaService.somenteAtivasDoGrupo(grupo));
			
		return mv;
	}
	
/**FIM DAS NOVAS SOLICITAÇÔES**/

/** INICIO CONSULTAS **/
	
	@GetMapping("/solicitacao/consulta")
	public ModelAndView consultaGeral() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consulta");
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}
	
	
	@GetMapping("/solicitacao/consulta/liberada")
	public ModelAndView consultaStatusLiberada() throws UnsupportedEncodingException, MessagingException {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaLiberada");
		mv.addObject("solicitacaoLiberadas", solicitacoes.findByStatus(Status.LIBERADA));
	//	emailService.enviaEmailNotificacao(solicitacoes.findByStatusOrStatus(Status.LIBERADA, Status.ERRO));
	
		return mv;
	}
	
	@GetMapping("/solicitacao/consulta/erro")
	public ModelAndView consultaStatusErro() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaErro");
		mv.addObject("solicitacaoErros", solicitacoes.findByStatus(Status.ERRO));

		return mv;
	}
	@GetMapping("/solicitacao/consulta/retificadora")
	public ModelAndView consultaRetificadoras() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaRetificadora");
		mv.addObject("solicitacaoRetificadoras", solicitacoes.buscarRetificadorasPendentes());

		return mv;
	}
	
	@GetMapping("/solicitacao/consulta/entregue")
	public ModelAndView consultaStatusEntregue() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaEntregue");
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}
	
	@PostMapping("/solicitacao/consulta/entregue")
	public ModelAndView consultaStatusEntreguePost(
			@RequestParam(value = "mesComp", required = false) String competencia,
			@RequestParam(value = "decComp", required = false) Integer iddec) {
		Declaracao declaracao = declaracoes.findByIdDec(iddec);
		List<Solicitacao> solicitacoesList = solicitacoes.findByDeclaracaoAndStatusAndCompetencia(declaracao,
				Status.ENTREGUE, competencia);
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaEntregue");
		mv.addObject("declaracoes", declaracoes.findAll());
		mv.addObject("solicitacaoEntregues", solicitacoesList);
		return mv;
	}
	
	@GetMapping("/solicitacao/consulta/competencia")
	public ModelAndView consultaPorCompetencia() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaCompetencia");
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}
	
	@PostMapping("/solicitacao/consulta/competencia")
	public ModelAndView consultaPorCompetenciaPost(
			@RequestParam(value = "mesComp", required = false) String competencia,
			@RequestParam(value = "decComp", required = false) Integer iddec) {
		Declaracao declaracao = declaracoes.findByIdDec(iddec);
		List<Solicitacao> solicitacoesList = solicitacoes.findByCompetenciaAndDeclaracao(competencia, declaracao);
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaCompetencia");
		mv.addObject("declaracoes", declaracoes.findAll());
		mv.addObject("solicitacaoEntregues", solicitacoesList);
		return mv;
	}
	
	@GetMapping("/solicitacao/consulta/pendente")
	public ModelAndView consultaStatusPendente() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaPendente");
		mv.addObject("solicitacaoPendentes", solicitacoes.findByStatusOrStatus(Status.LIBERADA, Status.ERRO));

		return mv;
	}
	
	@GetMapping("/solicitacao/consulta/nomes")
	public ModelAndView consultaPorMes() {
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaMesSolicitacao");
		mv.addObject("declaracoes", declaracoes.findAll());
		return mv;
	}
	
	@PostMapping("/solicitacao/consulta/nomes")
	public ModelAndView consultaPorMesPost(
			@RequestParam(value = "mesSolicita", required = false) String mesSolicita,
			@RequestParam(value = "decComp", required = false) Integer iddec) {
		ConverterStringLocalDateTime converterData = new ConverterStringLocalDateTime();
		List<Solicitacao> solicitacoesList = new ArrayList<Solicitacao>();
		if (iddec!=null) {
			Declaracao declaracao = declaracoes.findByIdDec(iddec);
			solicitacoesList = solicitacoes.findByDataSolicitacaoBetweenAndDeclaracao(converterData.convertInicioMes(mesSolicita), converterData.convertFimMes(mesSolicita),declaracao);
		}
		else {
			solicitacoesList = solicitacoes.findByDataSolicitacaoBetween(converterData.convertInicioMes(mesSolicita), converterData.convertFimMes(mesSolicita));
		}
		ModelAndView mv = new ModelAndView("solicitacao/consulta/consultaMesSolicitacao");
		mv.addObject("declaracoes", declaracoes.findAll());
		mv.addObject("solicitacaoEntregues", solicitacoesList);
		return mv;
	}
	
	/** Fragmento para modal de detalhes **/
	@GetMapping("/solicitacao/consulta/detalhes/{idsolicita}")
	public String detalhe(@PathVariable("idsolicita")long id, ModelMap model) {
		Solicitacao solicitacao = solicitacoes.findById(id);
		model.addAttribute(solicitacao);
		return "solicitacao/consulta/detalhesModal :: modalConteudo";
	}
	
/** FIM CONSULTA **/
/** INICIO PROCESSAR DECLARAÇÂO **/
	
	@GetMapping("/solicitacao/processar")
	public ModelAndView processarSolicitacao() {
		ModelAndView mv = new ModelAndView("solicitacao/processar/processarSolicitacoes");
		mv.addObject("solicitacaoProcessar", solicitacoes.findByStatusOrStatus(Status.LIBERADA, Status.ERRO));
		return mv;
	}
	/** Fragmento para atualizar uma linha ao alterar status em processar**/
	@RequestMapping(value = "/solicitacao/processar/atualizarlinha", method = RequestMethod.GET)
	public String atualizarLinha(@RequestParam("id") long id, Model model) {
	    Solicitacao solicitacao= solicitacoes.findById(id);
	    model.addAttribute("solicitacao", solicitacao);
	    return "solicitacao/processar/processarSolicitacoesRow :: solicitacao-row";
	    
	}
	/** Fragmento para atualizar uma linha pendente para cancelada **/
	@RequestMapping(value = "/solicitacao/processar/atualizarlinhacancelada", method = RequestMethod.GET)
	public String atualizarLinhaCancelada(@RequestParam("id") long id, Model model) {
	    Solicitacao solicitacao= solicitacoes.findById(id);
	    model.addAttribute("solicitacao", solicitacao);
	    return "solicitacao/processar/processarSolicitacoesRow :: cancelada-row";
	}
	
	@PostMapping("/solicitacao/processar/entregue/{id}")
	public ResponseEntity<?> processarSolicitacaoParaEntregue(@PathVariable("id") long id, Authentication authentication) {
	//	ModelAndView mv = new ModelAndView("solicitacao/processar/processarSolicitacoes");
		Usuario usuario = (Usuario)authentication.getPrincipal();
		
		try {
		Solicitacao solicitacao= solicitacoes.findById(id);
		solicitacao.setStatus(Status.ENTREGUE);
		solicitacao.setUsuarioRetorno(usuario);
		cadastroSolicitacaoService.salvar(solicitacao);
		return ResponseEntity.ok(solicitacao);
		}
		catch (CadastroSolicitacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/solicitacao/processar/erro/{id}")
	
	public ResponseEntity<?> processarSolicitacaoParaErro(@PathVariable("id") long id, HttpServletRequest request) {
		try {
		Solicitacao solicitacao= solicitacoes.findById(id);
		solicitacao.setStatus(Status.ERRO);
		cadastroSolicitacaoService.salvar(solicitacao);
		return ResponseEntity.ok(solicitacao);
		}
		catch (CadastroSolicitacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/solicitacao/processar/liberada/{id}")
	public ResponseEntity<?> processarSolicitacaoParaLiberada(@PathVariable("id") long id) {
	//	ModelAndView mv = new ModelAndView("solicitacao/processar/processarSolicitacoes");
		try {
		Solicitacao solicitacao= solicitacoes.findById(id);
		solicitacao.setStatus(Status.LIBERADA);
		cadastroSolicitacaoService.salvar(solicitacao);
		return ResponseEntity.ok(solicitacao);
		}
		catch (CadastroSolicitacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	@PostMapping("/solicitacao/processar/cancelada/{id}")
	public ResponseEntity<?> processarSolicitacaoParaCancelada(@PathVariable("id") long id) {
		try {
		Solicitacao solicitacao= solicitacoes.findById(id);
		solicitacao.setStatus(Status.CANCELADA);
		cadastroSolicitacaoService.salvar(solicitacao);
		return ResponseEntity.ok(solicitacao);
		}
		catch (CadastroSolicitacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	/**Fragmento para o modal de confirmação de entregue**/
	@GetMapping("/solicitacao/processar/entregue/confirmacao/{idsolicita}")
	public String processarModal(@PathVariable("idsolicita")long id, ModelMap model) {
		Solicitacao solicitacao = solicitacoes.findById(id);
		model.addAttribute(solicitacao);
		return "solicitacao/processar/confirmaModal :: modalConfirma";
	}
	
/** FIM PROCESSAR DECLARAÇÂO **/

/**INICIO EDITAR SOLICITAÇÃO**/

	@GetMapping("/solicitacao/editar/{id}")
	public ModelAndView editarEmpresa(@PathVariable("id") long id, Solicitacao solicitacao, BindingResult result) {
		try {
			ModelAndView mv = new ModelAndView("solicitacao/editar");
			mv.addObject("tiposdec", TipoDec.values());
			mv.addObject("declaracoes", declaracoes.findAll());
			mv.addObject("solicitacao", cadastroSolicitacaoService.verificaSolicitacaoId(id));
			return mv;
		} catch (CadastroSolicitacaoException e) {
			ModelAndView mv = new ModelAndView("solicitacao/editar");
			result.rejectValue("empresa.id", e.getMessage(), e.getMessage());
			mv.addObject(e.getMessage());
			return mv;
		}

	}
}