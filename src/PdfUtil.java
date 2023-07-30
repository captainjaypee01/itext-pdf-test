import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class PdfUtil {

    private static JSONArray questionList;

    private static PdfContentByte cb;
    private static ColumnText ct;
    private static float y;
    static Document document = null;
    static PdfWriter writer = null;
    static PdfContentByte canvas = null;
    public void pdf() {
        try{
            JSONArray questionList = Main.questionListFromJson();
            System.out.println(createQuestionnairePdf(questionList));
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public String createQuestionnairePdf(JSONArray questionList) throws FileNotFoundException, DocumentException {

        this.questionList = questionList;

        String randomFilename = Main.generateRandomString();
        String filename = Main.folderPath + (randomFilename + ".pdf").toString();

        String headerName = "Questionnaire (" + randomFilename + ")\n";

        document = new Document();
        // step 2
        writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

        // step 3
        document.open();

        cb = writer.getDirectContent();
        ct = new ColumnText(cb);
        y = 10;

        System.out.println("height " + document.getPageSize().getHeight());
        System.out.println("width " + document.getPageSize().getWidth());
        canvas = writer.getDirectContent();

        Font font = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
        Chunk chunk = new Chunk(headerName, font);
        Phrase heading = new Phrase(chunk);
        ct.setSimpleColumn(heading, 5, document.getPageSize().getHeight() - 30, (document.getPageSize().getWidth() / 2) - 40, document.getPageSize().getHeight() - 13, 10,
                Element.ALIGN_CENTER);
        ct.go();
//        document.add(heading);

        writeQuestions();

        document.close();

        System.out.println("Questionnaire created successfully...");

        return randomFilename;
    }

    static void writeQuestions() throws DocumentException {

        questionList.forEach( question -> {
            try {
                parseQuestionObject((JSONObject) question);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void parseQuestionObject(JSONObject question) throws DocumentException {
        System.out.println("==============");

        String questionDescription = (String) question.get("question");
        System.out.println(questionDescription);
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
        Chunk chunk = new Chunk(questionDescription, font);
        Phrase questionText = new Phrase(chunk);
        document.add(questionText);

        String questionType = (String) question.get("type");
        JSONArray answerList = null;
        JSONObject answer = null;
        List list = new List(List.UNORDERED);
        switch (questionType) {
            case "checkbox":
                answerList = (JSONArray) question.get("answers");
                break;
            default:
                answer = (JSONObject) question.get("answers");
                break;
        }
        if (questionType.equals("checkbox")) {
            for (Object ansObj : answerList) {
                JSONObject ansJsonObj = (JSONObject) ansObj;
                String description = (String) ansJsonObj.get("description");
                String value = (String) ansJsonObj.get("value");
                long score = Long.parseLong(String.valueOf(0));
                if (ansJsonObj.containsKey("score"))
                    score = (long) ansJsonObj.get("score");

                if (description != null) {
                    description += " " + "(" + score + ")";
                    list.add(new ListItem(description));
                }
                if (value != null) {
                    list.add(new ListItem(value));
                }

            }

            // System.out.println(answerList);
        } else {
            System.out.println(answer);
            String description = (String) answer.get("description");
            String value = (String) answer.get("value");
            long score = Long.parseLong(String.valueOf(0));
            if (answer.containsKey("score"))
                score = (long) answer.get("score");

            if (description != null) {
                description += " " + "(" + score + ")";
                list.add(new ListItem(description));
            }
            if (value != null) {
                list.add(new ListItem(value));
            }
        }
        document.add(list);
    }
}
