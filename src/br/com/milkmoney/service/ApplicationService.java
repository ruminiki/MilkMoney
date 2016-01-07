package br.com.milkmoney.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileUtil;

import br.com.milkmoney.dao.ApplicationDao;

public class ApplicationService{

	public StringProperty message = new SimpleStringProperty();
	
	@SuppressWarnings({ "resource" })
	public Task<Void> update(String version){
		
		Task<Void> task = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				try {
					
					updateMessage("Iniciando a atualização");
					Thread.sleep(1000);
					
					File destination = new File(version);
					
					FileUtils.forceMkdir(destination);
					File fileUpdate = new File(destination.getAbsolutePath() + File.separator +  "update.zip");
					
					String URL = getUrlUpdate();
					
					updateMessage("Fazendo o download de " + URL);
					Thread.sleep(1000);
					//CHECAR CONEXAO COM A INTERNET
					//VERIFICAR QUANDO SE TENTA ATUALIZAR VARIAS VERSOES DE UMA UNICA VEZ...
					FileUtils.copyURLToFile(new URL(URL), fileUpdate);
					
					updateMessage("Descompactando arquivos");
					Thread.sleep(1000);
					FileUtil.unZip(fileUpdate, destination);
				    File fileRun = new File(version + File.separator +  getFileRun());
					
					if ( fileRun != null ){
						
						updateMessage("Executando " + fileRun.getAbsolutePath());
						Thread.sleep(1000);
						
						Process p = Runtime.getRuntime().exec(fileRun.getAbsolutePath());
						
						Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\A");
						updateMessage(s.hasNext() ? s.next() : "");
						Thread.sleep(1000);
						updateMessage("Removendo arquivos temporários");
						Thread.sleep(1000);
				        FileUtils.forceDelete(destination);
				        updateMessage("Arquivos removidos com sucesso.\n\n");
				        Thread.sleep(1000);

				        s = new Scanner(p.getErrorStream()).useDelimiter("\\A");
				        if ( s.hasNext() ){
				        	updateMessage("---ERROS ENCONTRADOS---\n");
				        	Thread.sleep(1000);
				        	updateMessage(s.next());
				        	Thread.sleep(1000);
				        	updateMessage("Atualização NÃO CONCLUÍDA. Por favor, contate o administrador ou tente novamente.");
				        	Thread.sleep(1000);
				        }else{
				        	updateMessage("Atualização concluída com SUCESSO! Por favor, reinicie a aplicação!");
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
				String versaoAtual = new ApplicationDao().getVersaoSistema();

				File f = new File("update.properties");

				try {
					URL urlUpdate = new URL(URL);
					FileUtils.copyURLToFile(urlUpdate, f);
					
					if ( f != null ){
						inputStream = new FileInputStream(f);

						if (inputStream != null) {

							prop.load(inputStream);
							String update_version = prop.getProperty("update.version");

							if ( !update_version.equals(versaoAtual) ) {
								System.out.println("Existe uma nova versão do sistema: " + update_version);
								return update_version;
							}
						}
					}

				} catch (IOException ex) {
					System.out.println("Não foi possível verificar a atualização do sistema.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
