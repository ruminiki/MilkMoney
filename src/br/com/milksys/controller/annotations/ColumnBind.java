package br.com.milksys.controller.annotations;

@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.FIELD,java.lang.annotation.ElementType.METHOD})
public @interface ColumnBind{

	String name();

}
