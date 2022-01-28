package br.com.contabilidadereal.deccontrol.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.contabilidadereal.deccontrol.model.Empresa;
import br.com.contabilidadereal.deccontrol.repository.Empresas;
import br.com.contabilidadereal.deccontrol.service.exception.JaCadastradoException;
import br.com.contabilidadereal.deccontrol.service.exception.VerificaEmpresaException;

@Service
public class CadastroEmpresaService {
	@Autowired
	private Empresas empresas;

		@Transactional
		public Empresa salvar(Empresa empresa) {
			Empresa empresaVerifica = empresas.findById(empresa.getId());
			if (empresaVerifica != null) {
				return empresas.saveAndFlush(empresa);
			}
			Optional<Empresa> empresaOptional = empresas.findByCodEmpAndCodEmpFilial(empresa.getCodEmp(), empresa.getCodEmpFilial());
			if (empresaOptional.isPresent()) {
				throw new JaCadastradoException("Empresa já cadastrada com esse Código e Filial");
			}
			empresa.setAtiva(true);
			return empresas.saveAndFlush(empresa);
		}
		
		public Empresa verificaEmpresaAtiva(Empresa empresa) {
			Empresa empresaVerifica = verificaEmpresaId(empresa);
			if (empresaVerifica.getAtiva().equals(false) ) {
				System.out.println("empresa não ativa");
				throw new VerificaEmpresaException("A empresa de código "+ empresaVerifica.getCodEmp() +"-"+empresaVerifica.getCodEmpFilial() +" não está ativa");
			}
			return empresaVerifica;
		}
		
		public Empresa verificaEmpresaId(Empresa empresa) {
			Empresa empresaVerifica = empresas.findById(empresa.getId());
			if (empresaVerifica == null ) {
				throw new VerificaEmpresaException("Empresa não encontrada");
			}
			return empresaVerifica;
		}
		
		public Empresa verificaEmpresaPorCodigo(String codEmp, String codEmpFilial) {
			Optional<Empresa> empresaVerifica=empresas.findByCodEmpAndCodEmpFilial(codEmp, codEmpFilial);
			if (!empresaVerifica.isPresent()) {
				throw new VerificaEmpresaException("Empresa não cadastrada");
			}
			return empresaVerifica.get();
		}
		
}
