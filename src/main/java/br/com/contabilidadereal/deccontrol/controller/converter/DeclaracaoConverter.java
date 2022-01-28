package br.com.contabilidadereal.deccontrol.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.contabilidadereal.deccontrol.model.Declaracao;

public class DeclaracaoConverter implements Converter<String, Declaracao> {

	@Override
	public Declaracao convert(String idDesc) {
		if (!StringUtils.isEmpty(idDesc)) {
			Declaracao declaracao = new Declaracao();
			declaracao.setIdDec(Integer.valueOf(idDesc));
			return declaracao;
		}
		return null;
	}
}
