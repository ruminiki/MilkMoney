package br.com.milkmoney.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.milkmoney.dao.ApplicationDao;

@Service
public class ApplicationService{

	@Autowired
	private ApplicationDao dao;

	public void initilizeDatabase(){
		//atualização de versão
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "system.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
				String novaVersao = prop.getProperty("system.version");
				
				String versaoAtual = dao.getVersaoSistema();
				
				if ( novaVersao != null && !novaVersao.equals(versaoAtual) ){
					
					String diretorioVersao = "version/v" + novaVersao.replaceAll("\\.", "_");
				    File directory = getFile(getClass().getClassLoader().getResource(diretorioVersao));
				    // get all the files from a directory
				    File[] sqlFiles = directory.listFiles();
				    
				    //ordena os arquivos de acordo com a ordem de execução
				    Arrays.sort(sqlFiles, new Comparator<File>() {
					    @Override
					    public int compare(File f1, File f2) {
					        return f1.getName().substring(0, 2).compareTo(f2.getName().substring(0, 2));
					    }
				    });
				    
				    for ( File f : sqlFiles ){
						if ( f != null )
							dao.executeSqlFile(f);	
				    }
					
					dao.setSystemVersion(novaVersao);
					
				}
				
			}
			
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
	
	public boolean existeNovaVersao(){
	
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = "system.properties";

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {

			if (inputStream != null) {

				prop.load(inputStream);
				String URL = prop.getProperty("system.url_check_version");

				File f = new File("update.properties");

				try {
					URL urlUpdate = new URL(URL);
					FileUtils.copyURLToFile(urlUpdate, f);
					
					if ( f != null ){
						inputStream = new FileInputStream(f);

						if (inputStream != null) {

							prop.load(inputStream);
							String update_version = prop.getProperty("update.version");

							String currentVersion = dao.getVersaoSistema();
							if (!update_version.equals(currentVersion)) {
								System.out.println("Existe uma nova versão do sistema: " + update_version);
								return true;
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
		
		return false;
	}

}
