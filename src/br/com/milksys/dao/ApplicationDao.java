package br.com.milksys.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
public class ApplicationDao extends AbstractGenericDao<Integer, Object> {
	
	public void executeSqlFile(File file){
		
		try {
			
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(";");

			while(scanner.hasNext()) {
			    String sql = scanner.next();
			    System.out.println("SQL Statement: " + sql);
		    	Query query = entityManager.createNativeQuery(sql);
		    	
		    	EntityTransaction entityTransaction = entityManager.getTransaction();
				if ( !entityTransaction.isActive() )
					entityTransaction.begin();
		    	try{
		    		query.executeUpdate();
		    		entityTransaction.commit();
		    	}catch(Exception e){
		    		if ( entityTransaction.isActive() ){
		        		entityTransaction.rollback();
					}
		    	}
			}
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		    
	}
	
}