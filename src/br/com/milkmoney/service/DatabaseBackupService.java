package br.com.milkmoney.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class DatabaseBackupService{

	public StringProperty message = new SimpleStringProperty();
	
	@SuppressWarnings({ "resource" })
	public Task<Void> backup(){
		
		Task<Void> task = new Task<Void>() {
			
			@Override
			public Void call() throws InterruptedException {
				try {
					
					String CMD_RUN = "database\\bin\\mysqldump.exe -u root milkMoney --result-file ";
							
					updateMessage("Iniciando backup do banco de dados\n");
					Thread.sleep(1000);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_H_m_s");

					String DIR_BACKUP = "backup";
					String TMP_DIR_BACKUP = DIR_BACKUP + File.separator + sdf.format(new Date());
					String FINAL_FILE_BACKUP = TMP_DIR_BACKUP + ".zip";
					
					File dirBackup = new File(DIR_BACKUP);
					
					if ( !dirBackup.exists() ){
						dirBackup.mkdir();
					}
					
					String FILE_DB_BACKUP = TMP_DIR_BACKUP + File.separator +  sdf.format(new Date()) + ".dump";
					CMD_RUN += FILE_DB_BACKUP;
					
					Thread.sleep(1000);
					
					Process p = Runtime.getRuntime().exec(CMD_RUN);
					
			        //exibe o resultado da execução do script no log de atualização
			        Scanner s = new Scanner(p.getErrorStream()).useDelimiter("\\A");
			        if ( s.hasNext() ){
			        	updateMessage("---ERROS ENCONTRADOS---\n");
			        	Thread.sleep(1000);
			        	updateMessage(s.next());
			        	Thread.sleep(1000);
			        	updateMessage("O backup não pôde ser realizado. Por favor, contate o administrador ou tente novamente. \n");
			        	Thread.sleep(1000);
			        	return null;
			        }else{
			        	
			        	updateMessage("Copiando arquivos de imagens\n");
						Thread.sleep(1000);
						
						//copia a pasta de imagens para dentro da pasta do backup
						Runtime.getRuntime().exec("copy images " + TMP_DIR_BACKUP);
						
						updateMessage("Compactando arquivo de backup\n");
						Thread.sleep(1000);
						//compacta a pasta do backup
						compressZipfile(TMP_DIR_BACKUP, FINAL_FILE_BACKUP);
						
			        	updateMessage("Removendo arquivos temporários\n");
						Thread.sleep(1000);
						FileUtils.forceDelete(new File(TMP_DIR_BACKUP));
						
			        	updateMessage("Backup realizado com sucesso\n\n");
			        	Thread.sleep(1000);
			        	
			        	updateMessage("Dados do arquivo\n");
			        	Thread.sleep(1000);
			        	
			        	File FILE_BACUP = new File(FINAL_FILE_BACKUP);
			        	updateMessage("Tamanho: " + calculaTamanhoArquivo(FILE_BACUP) + "\n");
			        	Thread.sleep(1000);
			        	
			        	updateMessage("Disponível em: " + FILE_BACUP.getAbsolutePath() + "\n\n");
			        	Thread.sleep(1000);
			        	
			        	updateMessage("Por segurança mantenha sempre uma cópia de seu banco de dados em um dispositivo externo\ncomo PEN DRIVE ou E-MAIL.\n\n");
			        }
			        Thread.sleep(1000);
				
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				return null;
			}
		};
		
		return task;
		
	}
	
	
	public static void compressZipfile(String sourceDir, String outputFile) throws IOException, FileNotFoundException {
	    ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
	    compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
	    IOUtils.closeQuietly(zipFile);
	}

	private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
	    for (File file : new File(sourceDir).listFiles()) {
	        if (file.isDirectory()) {
	            compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + File.separator, out);
	        } else {
	            ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
	            out.putNextEntry(entry);

	            FileInputStream in = new FileInputStream(sourceDir + file.getName());
	            IOUtils.copy(in, out);
	            IOUtils.closeQuietly(in);
	        }
	    }
	}
	
	private String calculaTamanhoArquivo(File file){
    	long size = file.length();
    	
    	String sizeFormated = "";
    	if ( size < 1024 ){
    		sizeFormated = size + " bytes";
    	}else{
    		size = size / 1024;
    		//mb
    		if ( size > 1024 ){
    			size = size / 1024;
    			//gb
    			if ( size > 1024 ){
    				size = size / 1024;
    				sizeFormated = size + " Gb";
    			}else{
    				sizeFormated = size + " Mb";
    			}
    		}else{
    			sizeFormated = size + " kb";	
    		}
    	}
    	
    	return sizeFormated;

	}

}
