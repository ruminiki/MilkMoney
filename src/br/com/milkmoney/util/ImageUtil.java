package br.com.milkmoney.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import br.com.milkmoney.exception.ValidationException;

public class ImageUtil {

	public final static int UM_MB = 1000000;
	
	public static String reduceImageQualityAndSave(int maxSize, File src) throws Exception {
		
	    //se for maior que 10MB 
	    if ( src.length() > 10000000 ){
	    	throw new ValidationException("Validação", "O arquivo selecionado é muito grande. Por favor, selecione outro arquivo.");
	    }
	    
    	String fileExtension = FilenameUtils.getExtension(src.getName());
    	String destination = "imageOutput/" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + "." + fileExtension;
    	File fileOutput = new File(destination);
    	File dirOutput = new File("imageOutput");
		
		if ( !dirOutput.exists() ){
			dirOutput.mkdir();
		}
		
        long fileSize = src.length();  
        
        if (fileSize <= maxSize) {  
            FileUtils.copyFile(src, fileOutput);
            return destination;  
        }  
		
		float quality = 0.3f;
		
		@SuppressWarnings("rawtypes")
		Iterator iter = ImageIO.getImageWritersByFormatName(fileExtension);
		
		ImageWriter writer = (ImageWriter) iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		FileInputStream inputStream = new FileInputStream(src);
		BufferedImage originalImage = ImageIO.read(inputStream);
		
		IIOImage image = new IIOImage(originalImage, null, null);
		float percent = 0.1f; // 10% of 1
		
		while (fileSize > maxSize) {
			if (percent >= quality) {
				percent = percent * 0.1f;
			}

			quality -= percent;

			if (fileOutput.exists()) {
				fileOutput.delete();
			}
			
			FileImageOutputStream output = new FileImageOutputStream(fileOutput);
			writer.setOutput(output);
			iwp.setCompressionQuality(quality);
			writer.write(null, image, iwp);
			
			File fileOut2 = new File(fileOutput.getAbsolutePath());
			long newFileSize = fileOut2.length();
			
			if (newFileSize == fileSize) {
				// cannot reduce more, return
				break;
			} else {
				fileSize = newFileSize;
			}
			System.out.println("quality = " + quality + ", new file size = " + fileSize);
			output.close();
		}

		writer.dispose();
		return destination;
		
	}
}
