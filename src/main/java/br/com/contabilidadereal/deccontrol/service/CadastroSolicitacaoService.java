package br.com.contabilidadereal.deccontrol.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.model.Solicitacao;
import br.com.contabilidadereal.deccontrol.model.Status;
import br.com.contabilidadereal.deccontrol.model.TipoDec;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.repository.Solicitacoes;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroSolicitacaoException;
import br.com.contabilidadereal.deccontrol.service.exception.VerificaEmpresaException;

@Service
public class CadastroSolicitacaoService {
	@Autowired
	private Empresas empresas;
	@Autowired
	private Solicitacoes solicitacoes;
	
	private static final Logger logger = LoggerFactory.getLogger(CadastroSolicitacaoService.class);


	@Transactional
	public Solicitacao salvar(Solicitacao solicitacao) {
		if (solicitacao.getId() != null) {
			Solicitacao solicitacaoAlterar = verificaSolicitacaoId(solicitacao.getId());
			solicitacao = verificaEmpresa(solicitacao);
			if (solicitacao.getStatus().equals(Status.ERRO)) {
				if (solicitacaoAlterar.getStatus().equals(Status.LIBERADA)) {
					return solicitacoes.saveAndFlush(solicitacao);
				} else {
					throw new CadastroSolicitacaoException("Só é possível definir status erro em solicitação liberada");
				}
			} else if (solicitacao.getStatus().equals(Status.ENTREGUE)) {
				if (solicitacaoAlterar.getStatus().equals(Status.LIBERADA)) {
					solicitacao.setDataRetorno(LocalDateTime.now());

					return solicitacoes.saveAndFlush(solicitacao);
				} else {
					throw new CadastroSolicitacaoException(
							"Só é possível definir como entregue em solicitação liberada");
				}
			} else if (solicitacao.getStatus().equals(Status.LIBERADA)) {
				if (solicitacaoAlterar.getStatus().equals(Status.ERRO)) {
					return solicitacoes.saveAndFlush(solicitacao);
				} else {
					throw new CadastroSolicitacaoException(
							"Só é possível definir como liberada em solicitação com erro");
				}
			} else if (solicitacao.getStatus().equals(Status.CANCELADA)) {
				if (solicitacaoAlterar.getStatus().equals(Status.ERRO)) {
					return solicitacoes.saveAndFlush(solicitacao);
				} else if (solicitacaoAlterar.getStatus().equals(Status.LIBERADA)) {
					return solicitacoes.saveAndFlush(solicitacao);
				} else {
					throw new CadastroSolicitacaoException("Só é possível cancelar declaração não entregue");
				}
			}

		}
		solicitacao.setDataSolicitacao(LocalDateTime.now());
		return solicitacoes.saveAndFlush(solicitacao);
	}

	public Solicitacao verificaSolicitacaoId(Long id) {
		Solicitacao solicitacao = solicitacoes.findById(id);
		if (solicitacao == null) {
			throw new CadastroSolicitacaoException("Solicitação não encontrada");
		}
		return solicitacao;

	}

	public Solicitacao verificaEmpresa(Solicitacao solicitacao) {
		
		Empresa empresa = empresas.findById(solicitacao.getEmpresa().getId());
		
		if (empresa == null) {
			throw new VerificaEmpresaException("Empresa não está cadastrada");
		} else if (empresa.getAtiva().equals(false)) {
			throw new VerificaEmpresaException("A empresa não está ativada");
		}
		solicitacao.setEmpresa(empresa);
		return solicitacao;
	}

	public Solicitacao verificaJaCadastrada(Solicitacao solicitacao) {

		List<Solicitacao> solicitacaoCompara = solicitacoes.findByCompetenciaAndDeclaracaoAndTipoDec(
				solicitacao.getCompetencia(), solicitacao.getDeclaracao(), TipoDec.ORIGINAL);
		
		for (Solicitacao solicita : solicitacaoCompara) {
			if (!solicita.getStatus().equals(Status.CANCELADA)) {
				if ((solicitacao.getEmpresa().getId().equals(solicita.getEmpresa().getId()))
						&& solicita.getTipoDec() == solicitacao.getTipoDec()
						&& solicitacao.getDeclaracao().getIdDec() == solicita.getDeclaracao().getIdDec()) {
					throw new CadastroSolicitacaoException(
							"Solicitação já cadastrada para esse empresa/declaração/competência ");
				}
			}
		}

		return solicitacao;
	}

	public Boolean verificaJaCadastradaEmLote(Solicitacao solicitacao) {

		List<Solicitacao> solicitacaoCompara = solicitacoes.findByCompetenciaAndDeclaracaoAndTipoDec(
				solicitacao.getCompetencia(), solicitacao.getDeclaracao(), TipoDec.ORIGINAL);
		for (Solicitacao solicita : solicitacaoCompara) {
			if (!solicita.getStatus().equals(Status.CANCELADA)) {
				if (solicitacao.getEmpresa().getId().equals(solicita.getEmpresa().getId())
						&& solicita.getTipoDec().equals(solicitacao.getTipoDec())
						&& solicitacao.getDeclaracao().getIdDec() == solicita.getDeclaracao().getIdDec()) {
					return false;
				}
			}
		}
		return true;
	}

	public List<Solicitacao> verificaJaCadastradaExcluida(Solicitacao solicitacao) {

		List<Solicitacao> solicitacaoCompara = solicitacoes.findByCompetenciaAndDeclaracaoAndTipoDec(
				solicitacao.getCompetencia(), solicitacao.getDeclaracao(), TipoDec.ORIGINAL);
		List<Solicitacao> solicitaRemovida = new ArrayList<Solicitacao>();
		for (Solicitacao solicita : solicitacaoCompara) {
			if (!solicita.getStatus().equals(Status.CANCELADA)) {
				if ((solicitacao.getEmpresa().getId().equals(solicita.getEmpresa().getId()))
						&& solicita.getTipoDec() == solicitacao.getTipoDec()
						&& solicitacao.getDeclaracao().getIdDec() == solicita.getDeclaracao().getIdDec()) {
					solicitaRemovida.add(solicita);
				}
			}
		}
		return solicitaRemovida;
	}
}
