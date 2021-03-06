package br.com.milkmoney.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.springframework.stereotype.Repository;

import br.com.milkmoney.model.Sistema;

@Repository
public class ApplicationDao extends AbstractGenericDao<Integer, Object> {
	
	public Sistema getVersaoSistema() {
		
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "project.properties";
		Sistema sistema = new Sistema();
		
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {

			if (inputStream != null) {

				prop.load(inputStream);
				
				String URL    = prop.getProperty("jdbc.url");
				String user   = prop.getProperty("jdbc.username");
				String passwd = prop.getProperty("jdbc.password");
		
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection conn = DriverManager.getConnection(URL, user, passwd);
		
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT versao FROM SISTEMA");
				
				if ( rs.next() ){
					sistema.setVersao(rs.getString("versao"));
				}
				
				stmt.close();
				conn.close();
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sistema;
	}
	
}