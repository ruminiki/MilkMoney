package br.com.milkmoney.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDao extends AbstractGenericDao<Integer, Object> {
	
	@Transactional
	public void executeSqlFile(File file){
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(";");
			while(scanner.hasNext()) {
			    String sql = scanner.next();
			    if ( sql != null && !sql.trim().isEmpty() ){
			    	Query query = entityManager.createNativeQuery(sql);
			    	try{
			    		query.executeUpdate();
			    	}catch(Exception e){
			    		System.out.println(e.getMessage());
			    	}
			    }
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		    
	}
	
	public String getVersaoSistema() {
		Query query = entityManager.createQuery("select versao from Sistema");
		try{
			return (String) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
		
	}

	@Transactional
	public void setSystemVersion(String novaVersao) {
		Query query = entityManager.createQuery("update Sistema set versao = '" + novaVersao + "'");
		query.executeUpdate();
	}
	
}