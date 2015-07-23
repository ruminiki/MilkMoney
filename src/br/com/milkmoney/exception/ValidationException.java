package br.com.milkmoney.exception;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	private String tipo;
	
	public ValidationException(String tipo, String message) {
		
		this.tipo = tipo;
		this.message = message;
		
	}
	
	public String getTipo() {
		return tipo;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	
}
