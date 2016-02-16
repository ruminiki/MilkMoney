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
					
					//caso existam mais de uma versão para atualizar, atualiza todas de uma vez
					for ( int i = 0; i < versoes.length; i++ ){
						
						String versao = versoes[i].replace(" ", "");
						
						updateMessage("Iniciando a atualização para versão " + versao+"\n");
						Thread.sleep(1000);
						
						//remove arquivos da última atualização
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
							updateMessage("O arquivo " + URL + " não foi encontrado. \nA atualização será interrompida, por favor, contate o administrador do sistema.\n");
							updateProgress(100, 100);
							return null;
						}
						
						updateMessage("Descompactando arquivos\n");
						Thread.sleep(1000);
						
						FileUtil.unZip(fileUpdate, destination);
					    File fileRun = new File(destination + File.separator +  FILE_RUN);
						
						if ( fileRun != null ){
							//executa script de atualização
							updateMessage("Executando " + fileRun.getAbsolutePath()+"\n");
							Thread.sleep(1000);
							
							Process p = Runtime.getRuntime().exec(fileRun.getAbsolutePath());
							
							//remove arquivos temporários
							Thread.sleep(1000);
							updateMessage("Removendo arquivos temporários\n");
							Thread.sleep(1000);
					        FileUtils.forceDelete(fileUpdate);
					        updateMessage("Arquivos removidos com sucesso\n");
					        Thread.sleep(1000);
					        
					        //exibe o resultado da execução do script no log de atualização
					        Scanner s = new Scanner(p.getErrorStream()).useDelimiter("\\A");
					        if ( s.hasNext() ){
					        	updateMessage("---ERROS ENCONTRADOS---\n");
					        	Thread.sleep(1000);
					        	updateMessage(s.next());
					        	Thread.sleep(1000);
					        	updateMessage("Atualização para a versão[" + versao + "] NÃO CONCLUÍDA. Por favor, contate o administrador ou tente novamente. \n");
					        	Thread.sleep(1000);
					        	updateProgress(100, 100);
					        	return null;
					        }else{
					        	updateMessage("Atualização para a versão ["+versao+"] concluída com SUCESSO! \n\n");
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
					System.out.println("Verificação de atualização não pôde ser concluída. Não foi possível recuperar a versão atual do sistema.");   
					return null;
				}
				
				try {
					URL urlUpdate = new URL(URL);
					
			        final URLConnection conn = urlUpdate.openConnection();                                                                                                                                                                                  
			        conn.connect();                          
			        if(conn.getContentLength() == -1){
			        	System.out.println("Erro de conexão. Não foi possível verificar a atualização do sistema.");   
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
					        	//verifica se existe versão mais atual que a do sistema instalado
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
					        	System.out.println("Existe nova versão do sistema: " + updateVersion);	
					        }
							
						}
					}

				} catch (IOException ex) {
					ex.printStackTrace();
					System.out.println("Não foi possível verificar a atualização do sistema.");
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
