package br.com.milksys.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
//@ContextConfiguration("classpath:/applicationContext.xml")
public class ApplicationDao extends AbstractGenericDao<Integer, Object> {
	
	public void executeSqlFile(File file){
		
		try {
			
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(";");

			while(scanner.hasNext()) {
			    String sql = scanner.next();
		    	Query query = entityManager.createNativeQuery(sql);
		    	
		    	entityManager.getTransaction().begin();
		    	try{
		    		query.executeUpdate();
		    		entityManager.getTransaction().commit();
		    	}catch(Exception e){
		    		if ( entityManager.getTransaction().isActive() ){
		    			entityManager.getTransaction().rollback();
					}
		    	}
			}
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		    
	}
	
}