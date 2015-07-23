package br.com.milkmoney.controller.reports;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.engine.classic.core.modules.output.table.xls.ExcelReportUtil;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class GenericPentahoReport {

	public static final String PDF_OUTPUT_FORMAT = ".pdf";
	public static final String XLS_OUTPUT_FORMAT = ".xls";
	
	public static MasterReport getReportDefinition(String path){
		
        ClassicEngineBoot.getInstance().start(); 
        
        try {
        	
        	final URL reportDefinitionURL = ClassLoader.getSystemResource(path);
        	final ResourceManager resourceManager = new ResourceManager();
        	resourceManager.registerDefaults();
        	final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
        	return (MasterReport) directly.getResource();
        	
        } catch (Exception e) {
             e.printStackTrace();
             return null;
        }
        
	}
	
	private static String getFileName(String format){
		
		try {
		  Files.newDirectoryStream( new File("reportOutput/").toPath() ).forEach( file -> {
		    try { Files.delete( file ); }
		    catch ( IOException e ) {}
		  } );
		}catch ( IOException e ) {}
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
		String outputFileName = "reportOutput/" + sdf.format(new Date()) + format;
		return outputFileName;
		
	}
	
	public static void runReport(String format, String path){
		try {
			
			File output = new File(getFileName(format));
			
			if ( format.equals(PDF_OUTPUT_FORMAT) ){
				PdfReportUtil.createPDF(getReportDefinition(path), output);	
			}
			
			if ( format.equals(XLS_OUTPUT_FORMAT) ){
				ExcelReportUtil.createXLS(getReportDefinition(path), output.getAbsolutePath());	
			}
			
			Desktop.getDesktop().open(output);
			
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void runReport(String format, MasterReport report){
		try {
			
			File output = new File(getFileName(format));
			
			if ( format.equals(PDF_OUTPUT_FORMAT) ){
				PdfReportUtil.createPDF(report, output);	
			}
			
			if ( format.equals(XLS_OUTPUT_FORMAT) ){
				ExcelReportUtil.createXLS(report, output.getAbsolutePath());	
			}
			
			Desktop.getDesktop().open(output);
			
		} catch (ReportProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
