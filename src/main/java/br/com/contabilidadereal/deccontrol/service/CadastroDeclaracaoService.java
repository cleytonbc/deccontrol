package br.com.contabilidadereal.deccontrol.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Declaracao;
import br.com.contabilidadereal.deccontrol.repository.Declaracoes;
import br.com.contabilidadereal.deccontrol.service.exception.CadastroDeclaracaoException;

@Service
public class CadastroDeclaracaoService {

	@Autowired
	private Declaracoes declaracoes;
	
	@Transactional
	public Declaracao salvar(Declaracao declaracao) {
		Optional<Declaracao> declaracaoOptional = declaracoes.findByNomeIgnoreCase(declaracao.getNome());
		if (declaracao.getIdDec()==null) {
			if (declaracaoOptional.isPresent()) {
			// Caso o nome existe, lançar uma exceção
				throw new CadastroDeclaracaoException("Nome da declaração já cadastrado");
			}
		}
		return declaracoes.saveAndFlush(declaracao);
	}
	public Declaracao buscarDecId(Integer iddec) {
		Declaracao declaracao = declaracoes.findByIdDec(iddec);
		if (declaracao == null) {
			// Caso o nome existe, lançar uma exceção
			throw new CadastroDeclaracaoException("Declaração não encontrada");
		}
		return declaracao;
	}
}


