package pl.put.fc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MetaFileFixer {
    
    private static final String DESCRIPTION_PART = "'description': ";
    private static final String TITLE_PART = "'title': ";
    
    public void fix(String fileName) throws IOException {
        URL fileURL = getClass().getClassLoader().getResource(fileName);
        File fileIn = new File(fileURL.getPath());
        String fileOut = fileURL.getPath().replaceAll(".json", "_fixed.json");
        
        FileInputStream fis = new FileInputStream(fileIn);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        
        FileWriter fstream = new FileWriter(fileOut, false);
        BufferedWriter out = new BufferedWriter(fstream);
        
        String aLine = null;
        while ((aLine = in.readLine()) != null) {
            aLine = aLine.replaceAll("\\\\'", " ");
            aLine = fixLine(aLine, DESCRIPTION_PART);
            aLine = fixLine(aLine, TITLE_PART);
            out.write(aLine);
            out.newLine();
        }
        
        in.close();
        
        out.close();
    }
    
    public boolean isAlreadyFixed(String fileName) {
        URL fileURL = getClass().getClassLoader().getResource(fileName);
        if (fileURL != null) {
            File file = new File(fileURL.getPath());
            return file.exists();
        }
        return false;
    }
    
    private String fixLine(String line, String partToFix) {
        if (!line.contains(partToFix)) {
            return line;
        }
        
        String[] lineSplitted = line.split(partToFix);
        String partAfterDescription = lineSplitted[1];
        String openingQuote = partAfterDescription.substring(0, 1);
        String partAfterOpeningQuote = partAfterDescription.substring(1);
        
        if ("'".equals(openingQuote)) {
            return fixContent(lineSplitted[0], partToFix, "'", "\"", partAfterOpeningQuote);
        }
        if ("\"".equals(openingQuote)) {
            return fixContent(lineSplitted[0], partToFix, "\"", "'", partAfterOpeningQuote);
        }
        
        return line;
    }
    
    private String fixContent(String partBeforeOpeningQuote, String key, String quote, String quoteInContent,
            String partAfterOpeningQuote) {
        int endingQuoteIndex = partAfterOpeningQuote.indexOf(quote);
        String descriptionContent = partAfterOpeningQuote.substring(0, endingQuoteIndex);
        descriptionContent = descriptionContent.replaceAll(quoteInContent, " ");
        return partBeforeOpeningQuote + key + quote + descriptionContent + partAfterOpeningQuote.substring(endingQuoteIndex);
    }
}
