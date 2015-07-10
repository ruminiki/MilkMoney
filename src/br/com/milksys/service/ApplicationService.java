package br.com.milksys.service;

import java.io.File;
import java.net.URL;

import br.com.milksys.dao.ApplicationDao;

public class ApplicationService{

	private ApplicationDao dao = new ApplicationDao();

	public void initilizeDatabase(){
		File file = null;
		
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
