import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {

    public static String folderPath = "C:\\Users\\ACER\\Documents\\project files\\text-pdf\\";
    public static String jsonFolderPath = "src/json/";
    public static void main(String[] args) throws DocumentException, FileNotFoundException {

        JSONArray questionList = questionListFromJson();
        PdfUtil questionnairePdf = new PdfUtil();
        questionnairePdf.createQuestionnairePdf(questionList);

    }

    public static String generateRandomString() {
        String generatedString = RandomStringUtils.randomAlphanumeric(10);

        System.out.println(generatedString);

        return generatedString;
    }

    static JSONArray questionListFromJson(){
        JSONArray questionList = new JSONArray();
        try {
            //Assertion assertion = JsonUtil.getReadMapper().readValue(new File("/assertion.json"), Assertion.class);

            JSONParser jsonParser = new JSONParser();
            String answersFilename = jsonFolderPath + "answers.json";
            try (FileReader reader = new FileReader(answersFilename))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                questionList = (JSONArray) obj;
                // System.out.println(questionList);

                return questionList;
                //Iterate over question array
                // questionList.forEach( question -> parseQuestionObject( (JSONObject) question ) );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return questionList;
    }
}