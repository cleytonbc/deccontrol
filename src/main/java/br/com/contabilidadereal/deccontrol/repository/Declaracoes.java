package br.com.contabilidadereal.deccontrol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Declaracao;

@Repository
public interface Declaracoes extends JpaRepository<Declaracao, Long> {
	
	public Optional<Declaracao> findByNomeIgnoreCase(String nome);
	public Declaracao findByIdDec(Integer idDec);
	

}
