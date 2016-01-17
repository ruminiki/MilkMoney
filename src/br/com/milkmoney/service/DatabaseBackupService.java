package br.com.milkmoney.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

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
					
					String CMD_RUN = "database\\bin\\mysqldump.exe -u root -d milkMoney --result-file ";
					String PATH_BACKUP = "backup\\";
							
					updateMessage("Iniciando backup do banco de dados\n");
					Thread.sleep(1000);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_H_m_s");
					PATH_BACKUP += sdf.format(new Date()) + ".dump";
					CMD_RUN += PATH_BACKUP;
						
					File dirBackup = new File("backup");
					
					if ( !dirBackup.exists() ){
						dirBackup.mkdir();
					}
					
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
			        	
			        	File FILE_BACKUP = new File(PATH_BACKUP);
			        	
			        	long size = FILE_BACKUP.length();
			        	
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
			        	
			        	updateMessage("Backup realizado com sucesso\n\n");
			        	Thread.sleep(1000);
			        	updateMessage("Dados do arquivo\n");
			        	Thread.sleep(1000);
			        	updateMessage("Tamanho: " + sizeFormated + "\n");
			        	Thread.sleep(1000);
			        	updateMessage("Disponível em: " + FILE_BACKUP.getAbsolutePath() + "\n\n");
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

}
