package br.com.contabilidadereal.deccontrol.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Código da empresa é obrigatório")
	@Size(max = 10, message = "O tamanho do nome não pode ser maior que {max} caracteres")
	private String codEmp;
	
	@NotBlank(message = "Código da filial é obrigatório")
	
	private String codEmpFilial;
	
	@NotBlank(message = "O CNPJ da empresa é obrigatório")
	@Size(max = 14, message = "O tamanho do nome não pode ser maior que {max} caracteres")
	private String cnpj;
	
	@NotBlank(message = "A Razão social da empresa é obrigatório")
	@Size(max = 100, message = "O tamanho do nome não pode ser maior que {max} caracteres")
	private String nome;
	
	private Boolean ativa;
	
	@OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL)
	private List<Solicitacao> solicitacoes;
	
	@OneToMany(mappedBy = "empresa", cascade=CascadeType.ALL)
	private List<GrupoEmpresa> grupoEmpresas;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodEmp() {
		return codEmp;
	}
	public void setCodEmp(String codEmp) {
		this.codEmp = codEmp;
	}
	public String getCodEmpFilial() {
		return codEmpFilial;
	}
	public void setCodEmpFilial(String codEmpFilial) {
		this.codEmpFilial = codEmpFilial;
	}
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String razao) {
		this.nome = razao;
	}
	
	public Boolean getAtiva() {
		return ativa;
	}
	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codEmp == null) ? 0 : codEmp.hashCode());
		result = prime * result + ((codEmpFilial == null) ? 0 : codEmpFilial.hashCode());
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
		Empresa other = (Empresa) obj;
		if (codEmp == null) {
			if (other.codEmp != null)
				return false;
		} else if (!codEmp.equals(other.codEmp))
			return false;
		if (codEmpFilial == null) {
			if (other.codEmpFilial != null)
				return false;
		} else if (!codEmpFilial.equals(other.codEmpFilial))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Empresa [id=" + id + ", codEmp=" + codEmp + ", codEmpFilial=" + codEmpFilial + ", cnpj=" + cnpj
				+ ", razao=" + nome + "]";
	}


}
