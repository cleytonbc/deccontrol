package br.com.contabilidadereal.deccontrol.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import br.com.contabilidadereal.deccontrol.model.Role;

public class RoleConverter implements Converter<String, Role> {

	@Override
	public Role convert(String id) {
		if (!StringUtils.isEmpty(id)) {
			Role role = new Role();
			role.setId(Integer.valueOf(id));
			return role;
		}
		return null;
	}

}
