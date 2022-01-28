package br.com.contabilidadereal.deccontrol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "declaracao")
public class Declaracao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDec;
	
	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 50, message = "O tamanho do nome não pode ser maior que {max} caracteres")
	private String nome;
	
	@NotNull(message = "O tipo de período é obrigatório")
	@Enumerated(EnumType.STRING)
	private TipoPeriodo tipoPeriodo;
	
	@OneToMany(mappedBy = "declaracao")
	private List<Solicitacao> solicitacoes;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	public Integer getIdDec() {
		return idDec;
	}
	public void setIdDec(Integer idDec) {
		this.idDec = idDec;
	}
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDec == null) ? 0 : idDec.hashCode());
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
		Declaracao other = (Declaracao) obj;
		if (idDec == null) {
			if (other.idDec != null)
				return false;
		} else if (!idDec.equals(other.idDec))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Declaracao [idDec=" + idDec + ", nome=" + nome + ", tipoPeriodo=" + tipoPeriodo + "]";
	}
	
	

}
