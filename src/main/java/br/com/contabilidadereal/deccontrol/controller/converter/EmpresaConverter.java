package br.com.contabilidadereal.deccontrol.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.contabilidadereal.deccontrol.model.Empresa;

public class EmpresaConverter implements Converter<String, Empresa> {

	@Override
	public Empresa convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			Empresa empresa = new Empresa();
			empresa.setId(Long.valueOf(id));
			return empresa;
		}
		return null;
	}
}
