package br.com.milkmoney.service;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.ApplicationDao;

@Service
public class ApplicationService{

	@Autowired
	private ApplicationDao dao;

	public void initilizeDatabase(){
		File file = null;
		
		//propriedade
		file = getFile(getClass().getClassLoader().getResource("sql/PROPRIEDADE.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//indicadores
		file = getFile(getClass().getClassLoader().getResource("sql/INDICADOR.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//parametros
		file = getFile(getClass().getClassLoader().getResource("sql/PARAMETROS.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//view_animais_ativos
		file = getFile(getClass().getClassLoader().getResource("sql/ANIMAIS_ATIVOS_VIEW.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//raca
		file = getFile(getClass().getClassLoader().getResource("sql/RACA.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//view situacao animal
		file = getFile(getClass().getClassLoader().getResource("sql/SITUACAO_ANIMAL_VIEW.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//tipo procedimento
		file = getFile(getClass().getClassLoader().getResource("sql/TIPO_PROCEDIMENTO.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
	}
	
	
	private File getFile(URL urlFile){
		try{
			File file = new File(urlFile.getPath());
			return file;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	

}
