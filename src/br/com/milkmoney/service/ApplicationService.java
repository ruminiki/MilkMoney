package br.com.milkmoney.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileUtil;

import br.com.milkmoney.components.CustomAlert;

public class ApplicationService{

	public void update(String version){
		
		try {
			
			File destination = new File(version);
			
			FileUtils.forceMkdir(destination);
			File fileUpdate = new File(destination.getAbsolutePath() + File.separator +  "update.zip");
			FileUtils.copyURLToFile(new URL(getUrlUpdate()), fileUpdate);
					
			FileUtil.unZip(fileUpdate, destination);
		    File fileRun = new File(version + File.separator +  getFileRun());
			
			if ( fileRun != null ){
				
				System.out.println("Executando: " + fileRun.getAbsolutePath());
				
				ProcessBuilder update = new ProcessBuilder(fileRun.getAbsolutePath());
				update.start();
				FileUtils.forceDelete(destination);
				
			}
			
			CustomAlert.mensagemAlerta("Atualização", "Sistema atualizado com sucesso para a versão" + version + ". Por favor, inicie o sistema novamente." );
			
	        //fecha o sistema
			System.exit(0);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			//System.out.println("Não foi possível atualizar o sistema.");
		}
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
				String versaoAtual = prop.getProperty("system.version");

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
