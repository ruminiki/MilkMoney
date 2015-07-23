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
		
		//indicadores
		file = getFile(ClassLoader.getSystemResource("sql/INDICADOR.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//parametros
		file = getFile(ClassLoader.getSystemResource("sql/PARAMETROS.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//view_animais_ativos
		file = getFile(ClassLoader.getSystemResource("sql/ANIMAIS_ATIVOS_VIEW.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//raca
		file = getFile(ClassLoader.getSystemResource("sql/RACA.SQL"));
		if ( file != null )
			dao.executeSqlFile(file);
		
		//view situacao animal
		file = getFile(ClassLoader.getSystemResource("sql/SITUACAO_ANIMAL_VIEW.SQL"));
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
