package br.com.contabilidadereal.deccontrol.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "solicitacao")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "A empresa é obrigatório")
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@ManyToOne
	@JoinColumn(name = "id_declaracao")
	@NotNull(message = "A declaração é obrigatória")
	private Declaracao declaracao;
	
	@NotBlank(message = "A competencia é obrigatória")
	private String competencia;
	
	@NotNull(message = "O tipo de declaração é obrigatória")
	@Enumerated(EnumType.STRING)
	private TipoDec tipoDec;
	
	private String observacao;
	
	@Enumerated(EnumType.STRING)
	@Column( name = "status", nullable = false )
	private Status status;
	
	private LocalDateTime dataSolicitacao;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario_solicita")
	private Usuario usuarioSolicita;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario_retorno")
	private Usuario usuarioRetorno;
	
	
	private LocalDateTime dataRetorno;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Declaracao getDeclaracao() {
		return declaracao;
	}
	public void setDeclaracao(Declaracao declaracao) {
		this.declaracao = declaracao;
	}
	public String getCompetencia() {
		return competencia;
	}
	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}
	public TipoDec getTipoDec() {
		return tipoDec;
	}
	public void setTipoDec(TipoDec tipoDec) {
		this.tipoDec = tipoDec;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public LocalDateTime getDataSolicitacao() {
		return dataSolicitacao;
	}
	public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	public LocalDateTime getDataRetorno() {
		return dataRetorno;
	}
	public void setDataRetorno(LocalDateTime dataRetorno) {
		this.dataRetorno = dataRetorno;
	}
	
	public Usuario getUsuarioSolicita() {
		return usuarioSolicita;
	}
	public void setUsuarioSolicita(Usuario usuarioSolicita) {
		this.usuarioSolicita = usuarioSolicita;
	}
	
	public Usuario getUsuarioRetorno() {
		return usuarioRetorno;
	}
	public void setUsuarioRetorno(Usuario usuarioRetorno) {
		this.usuarioRetorno = usuarioRetorno;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solicitacao other = (Solicitacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Solicitacao [id=" + id + ", empresa=" + empresa + ", declaracao=" + declaracao + ", competencia="
				+ competencia + ", tipoDec=" + tipoDec + ", observacao=" + observacao + ", status=" + status
				+ ", dataSolicitacao=" + dataSolicitacao +"]";
	}
	

	
	

}
