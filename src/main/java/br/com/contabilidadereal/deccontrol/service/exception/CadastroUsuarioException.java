package br.com.contabilidadereal.deccontrol.service.exception;

public class CadastroUsuarioException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CadastroUsuarioException (String message) {
		super(message);
	}

}