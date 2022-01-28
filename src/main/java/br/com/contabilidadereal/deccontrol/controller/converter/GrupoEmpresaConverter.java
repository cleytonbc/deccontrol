package br.com.contabilidadereal.deccontrol.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.contabilidadereal.deccontrol.model.GrupoEmpresa;

public class GrupoEmpresaConverter implements Converter<String, GrupoEmpresa> {
	
		@Override
		public GrupoEmpresa convert(String id) {
			if (!StringUtils.isEmpty(id)) {
				GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
				grupoEmpresa.setId(Long.valueOf(id));
				return grupoEmpresa;
			}
			return null;
		}

}
