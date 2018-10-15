package main;

import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exercise2 {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private OptimaizeLangDetector langDetector;

    public static void main(String[] args) {
        Exercise2 exercise = new Exercise2();
        exercise.run();
    }

    private void run() {
        try {
            if (!new File("./outputDocuments").exists()) {
                Files.createDirectory(Paths.get("./outputDocuments"));
            }

            initLangDetector();

            File directory = new File("./documents");
            File[] files = directory.listFiles();
            for (File file : files) {
                processFile(file);
            }
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }

    }

    //AutoDetectParser, BodyContentHandler,
    //Metadata, OptimaizeLangDetector, LanguageResult, TikeCoreProperties

    private void initLangDetector() throws IOException {


        // TODO initialize language detector (langDetector)


    }

    private void processFile(File file) throws IOException, SAXException, TikaException {
        // TODO: extract content, metadata and language from given file
        // call saveResult method to save the data
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        FileInputStream content = new FileInputStream(file);
        parser.parse(content, handler, metadata, new ParseContext());
        LanguageIdentifier object = new LanguageIdentifier(handler.toString());

        String creator = "";
        Date date = new Date();
        Date lastModif = new Date();
        try {
            creator = metadata.getValues("creator")[0];
            date = metadata.getDate(Property.get("date"));
            lastModif = metadata.getDate(Property.get("Last-Modified"));
        } catch (ArrayIndexOutOfBoundsException exc) {
            System.err.println(exc.getMessage());
        }

        //TODO: fill with proper values
        saveResult(
                file.getName(),
                object.getLanguage(),
                creator,
                date,
                lastModif,
                null,
                null);
    }

    private void saveResult(String fileName, String language, String creatorName, Date creationDate,
                            Date lastModification, String mimeType, String content) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        int index = fileName.lastIndexOf(".");
        String outName = fileName.substring(0, index) + ".txt";
        try {
            PrintWriter printWriter = new PrintWriter("./outputDocuments/" + outName);
            printWriter.write("Name: " + fileName + "\n");
            printWriter.write("Language: " + (language != null ? language : "") + "\n");
            printWriter.write("Creator: " + (creatorName != null ? creatorName : "") + "\n");
            String creationDateStr = creationDate == null ? "" : dateFormat.format(creationDate);
            printWriter.write("Creation date: " + creationDateStr + "\n");
            String lastModificationStr = lastModification == null ? "" : dateFormat.format(lastModification);
            printWriter.write("Last modification: " + lastModificationStr + "\n");
            printWriter.write("MIME type: " + (mimeType != null ? mimeType : "") + "\n");
            printWriter.write("\n");
            printWriter.write(content + "\n");
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
