package br.com.contabilidadereal.deccontrol.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.contabilidadereal.deccontrol.model.Declaracao;
import br.com.contabilidadereal.deccontrol.model.Solicitacao;
import br.com.contabilidadereal.deccontrol.model.Status;
import br.com.contabilidadereal.deccontrol.model.TipoDec;

@Repository
public interface Solicitacoes extends JpaRepository<Solicitacao, Long>{
	
	public Optional<Solicitacao> findByDataSolicitacao(LocalDateTime dataSolicitacao);
	public List<Solicitacao> findByStatus(Status status);
	public List<Solicitacao> findByStatusOrStatus(Status status1, Status status2);
	public List<Solicitacao> findByCompetenciaAndDeclaracao(String competencia, Declaracao declaracao);
	public List<Solicitacao> findByCompetenciaAndDeclaracaoAndTipoDec(String competencia, Declaracao declaracao, TipoDec tipoDec);
	public List<Solicitacao> findByDataSolicitacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
	public List<Solicitacao> findByDataSolicitacaoBetweenAndDeclaracao(LocalDateTime dataInicio, LocalDateTime dataFim, Declaracao declaracao);
	public List<Solicitacao> findByDeclaracaoAndStatusAndCompetencia(Declaracao declaracao, Status status, String competencia);
	public List<Solicitacao> findByDeclaracaoAndStatus(Declaracao declaracao, Status status);
	public Solicitacao findById(Long id);
	public Integer countByStatus(Status status);
	public List<Solicitacao> findByStatusAndTipoDec(Status status, TipoDec tipoDec);
	public Integer countByTipoDec(TipoDec tipoDec);	
	public Integer countByDataSolicitacaoBetweenAndStatus(LocalDateTime dataInicio, LocalDateTime dataFim,Status status);
	public Integer countByDataSolicitacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
	public Integer countByTipoDecAndStatus(TipoDec tipoDec, Status status);	
	
	@Query(value = "select * from solicitacao where tipodec = 'SUBSTITUTO' and (status='LIBERADA' or status='ERRO')", nativeQuery = true)
	List<Solicitacao> buscarRetificadorasPendentes();
	
	@Query(value= "SELECT * FROM deccontrol.solicitacao WHERE MONTH(dataSolicitacao) = ?1 AND YEAR(dataSolicitacao)= ?2 AND id_declaracao= ?3", nativeQuery = true)
	List<Solicitacao> findByDateMonthYear(String mes, String ano, Integer dec);
	
}
