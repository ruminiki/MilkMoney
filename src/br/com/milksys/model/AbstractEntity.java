package br.com.milksys.model;


public abstract class AbstractEntity {
	
	/*private Hashtable<String, String> erros = new Hashtable<String, String>();
	public static final String KMV_CAMPO_OBRIGATORIO = "Campo obrigatório não informado";
*/
	abstract public int getId();
	
	/*public boolean isValid(){
		
		for ( Method method : this.getClass().getDeclaredMethods() ){
			FieldRequired annotation = method.getAnnotation(FieldRequired.class);
			if ( annotation != null ){
				try {
					
					Object result = method.invoke(this);
					
					if ( result == null || (result instanceof String && ((String)result).isEmpty()) ){
						erros.put(KMV_CAMPO_OBRIGATORIO, annotation.message());
						return false;
					}
					
				} catch (IllegalAccessException | IllegalArgumentException	| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
		
	}

	public Hashtable<String, String> getErros() {
		return erros;
	}*/
	
}
