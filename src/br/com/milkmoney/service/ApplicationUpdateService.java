package br.com.milkmoney.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileUtil;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.ApplicationDao;
import br.com.milkmoney.model.Sistema;

@Service
public class ApplicationUpdateService{

	public StringProperty message = new SimpleStringProperty();
	
	@SuppressWarnings({ "resource" })
	public Task<Void> update(String version){
		
		Task<Void> task = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				try {
					
					String[] versoes  = version.split("\\,");
					String URL_UPDATE = getUrlUpdate();
					String FILE_RUN   = getFileRun();
					String DIR_UPDATE = "update";
					
					//caso existam mais de uma vers�o para atualizar, atualiza todas de uma vez
					for ( int i = 0; i < versoes.length; i++ ){
						
						String versao = versoes[i].replace(" ", "");
						
						updateMessage("Iniciando a atualiza��o para vers�o " + versao+"\n");
						Thread.sleep(1000);
						
						//remove arquivos da �ltima atualiza��o
						updateMessage("Preparando ambiente \n");
						Thread.sleep(1000);
						File dirUpdate = new File(DIR_UPDATE);
						FileUtils.forceDelete(dirUpdate);
						
						File destination = new File(DIR_UPDATE + File.separator + versao);
						FileUtils.forceMkdir(destination);
						
						File fileUpdate = new File(destination.getAbsolutePath() + File.separator +  versao + ".zip");
						String URL = URL_UPDATE + versao + ".zip";
						
						updateMessage("Fazendo o download de " + URL+"\n");
						Thread.sleep(1000);
						
						try{
							FileUtils.copyURLToFile(new URL(URL), fileUpdate);
						}catch(FileNotFoundException ex){
							updateMessage("O arquivo " + URL + " n�o foi encontrado. \nA atualiza��o ser� interrompida, por favor, contate o administrador do sistema.\n");
							updateProgress(100, 100);
							return null;
						}
						
						updateMessage("Descompactando arquivos\n");
						Thread.sleep(1000);
						
						FileUtil.unZip(fileUpdate, destination);
					    File fileRun = new File(destination + File.separator +  FILE_RUN);
						
						if ( fileRun != null ){
							//executa script de atualiza��o
							updateMessage("Executando " + fileRun.getAbsolutePath()+"\n");
							Thread.sleep(1000);
							
							Process p = Runtime.getRuntime().exec(fileRun.getAbsolutePath());
							
							//remove arquivos tempor�rios
							Thread.sleep(1000);
							updateMessage("Removendo arquivos tempor�rios\n");
							Thread.sleep(1000);
					        FileUtils.forceDelete(fileUpdate);
					        updateMessage("Arquivos removidos com sucesso\n");
					        Thread.sleep(1000);
					        
					        //exibe o resultado da execu��o do script no log de atualiza��o
					        Scanner s = new Scanner(p.getErrorStream()).useDelimiter("\\A");
					        if ( s.hasNext() ){
					        	updateMessage("---ERROS ENCONTRADOS---\n");
					        	Thread.sleep(1000);
					        	updateMessage(s.next());
					        	Thread.sleep(1000);
					        	updateMessage("Atualiza��o para a vers�o[" + versao + "] N�O CONCLU�DA. Por favor, contate o administrador ou tente novamente. \n");
					        	Thread.sleep(1000);
					        	updateProgress(100, 100);
					        	return null;
					        }else{
					        	updateMessage("Atualiza��o para a vers�o ["+versao+"] conclu�da com SUCESSO! \n\n");
					        }
					        
					        Thread.sleep(1000);
						}
					}
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				return null;
			}
		};
		
		return task;
		
	}
	
	private String getUrlUpdate(){
		
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "system.properties";
		
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		try {
			if (inputStream != null) {
				prop.load(inputStream);
				String OS = System.getProperty("os.name").toLowerCase();
				if ( OS.indexOf("win") >= 0 ){
					return prop.getProperty("system.url_update_version_windows");
				}else{
					return prop.getProperty("system.url_update_version_linux");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getFileRun(){
		
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "system.properties";
		
		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		try {
			if (inputStream != null) {
				prop.load(inputStream);
				String OS = System.getProperty("os.name").toLowerCase();
				if ( OS.indexOf("win") >= 0 ){
					return prop.getProperty("system.update_file_run_windows");
				}else{
					return prop.getProperty("system.update_file_run_linux");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getNumeroNovaVersao(){
	
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "system.properties";

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {

			if (inputStream != null) {

				prop.load(inputStream);
				String URL = prop.getProperty("system.url_check_version");
				Sistema sistema = new ApplicationDao().getVersaoSistema();

				File f = new File("update.properties");

				if ( sistema == null ){
					System.out.println("Verifica��o de atualiza��o n�o p�de ser conclu�da. N�o foi poss�vel recuperar a vers�o atual do sistema.");   
					return null;
				}
				
				try {
					URL urlUpdate = new URL(URL);
					
			        final URLConnection conn = urlUpdate.openConnection();                                                                                                                                                                                  
			        conn.connect();                          
			        if(conn.getContentLength() == -1){
			        	System.out.println("Erro de conex�o. N�o foi poss�vel verificar a atualiza��o do sistema.");   
			        	return null;
			        }
			        
			        FileUtils.copyURLToFile(urlUpdate, f);
					
					if ( f != null ){
						inputStream = new FileInputStream(f);

						if (inputStream != null) {

							@SuppressWarnings("resource")
							Scanner s = new Scanner(inputStream);
							String updateVersion = "";
							
					        while ( s.hasNext() ){
					        	String version = s.next();
					        	//verifica se existe vers�o mais atual que a do sistema instalado
					        	if ( sistema != null && sistema.getVersao() != null ){
					        		if ( Integer.parseInt(version.replace(".", "")) > Integer.parseInt(sistema.getVersao().replace(".", "")) ) {
						        		updateVersion += version + ", ";
						        	}
					        	}
					        	
					        }
					        
					        if ( updateVersion.length() > 0 ){
					        	if ( updateVersion.endsWith(", ") ){
					        		updateVersion = updateVersion.substring(0, updateVersion.length() - 2);
					        		return updateVersion;
					        	}
					        	System.out.println("Existe nova vers�o do sistema: " + updateVersion);	
					        }
							
						}
					}

				} catch (IOException ex) {
					ex.printStackTrace();
					System.out.println("N�o foi poss�vel verificar a atualiza��o do sistema.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getVersaoSistema(){
		Sistema sistema = new ApplicationDao().getVersaoSistema();
		return sistema != null ? sistema.getVersao() : null;
	}

}
