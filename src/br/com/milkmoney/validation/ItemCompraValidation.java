package br.com.milkmoney.validation;

import br.com.milkmoney.model.ItemCompra;


public class ItemCompraValidation extends Validator {
	
	public static void validate(ItemCompra itemCompra) {
	
		Validator.validate(itemCompra);
		
	}
}
