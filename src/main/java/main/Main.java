package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Evgenii_Rybnikov on 23.08.2017.
 */
public class Main {

    private static final String ROOT_PATH = "D:/bills/json";
    final static Set<String> KEY_SET = new HashSet<>(
            Arrays.asList(
                    "dateTime",
                    "ndsCalculated18",
                    "fiscalDriveNumber",
                    "shiftNumber",
                    "discount",
                    "discountSum",
                    "modifiers",
                    "operator",
                    "requestNumber",
                    "ecashTotalSum",
                    "fiscalDocumentNumber",
                    "nds18",
                    "taxationType",
                    "nds0",
                    "nds10",
                    "ndsNo",
                    "markup",
                    "userInn",
                    "kktRegId",
                    "cashTotalSum",
                    "totalSum",
                    "stornoItems",
                    "markupSum",
                    "kktNumber",
                    "retailPlaceAddress",
                    "fiscalSign",
                    "operationType",
                    "ndsCalculated10",
                    "items",
                    "user"
            )
    );


    public static void main(String[] args) {
        File[] files = new File(ROOT_PATH).listFiles();
        if (files.length == 1) throw new RuntimeException("No files found in " + ROOT_PATH);
        Arrays.stream(files)
                .filter(File::isFile)
                .filter(x -> x.getName().matches(".+\\.json"))
                .map(x -> getItems(x.getAbsolutePath()))
                .flatMap(List::stream)
                .forEach(System.out::println);
    }

    private static List<Item> getItems(String fileName) {

        JSONParser parser = new JSONParser();
        List<Item> list = null;
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(fileName));

            if (!isCorrect(jsonObject))
                throw new RuntimeException(String.format("File (%s) doesn't match the format", fileName));

            Calendar date = getDate(jsonObject);
            String userInn = (String) jsonObject.get("userInn");

            JSONArray items = (JSONArray) jsonObject.get("items");
            list = (List<Item>) StreamSupport.stream(items.spliterator(), false)
//            list = (List<Item>) items.stream()
                    .map(o -> new Item(date, userInn, (JSONObject) o))
                    .collect(Collectors.toList());

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Calendar getDate(JSONObject jsonObject) {
        Calendar date = Calendar.getInstance(Locale.getDefault());
        date.setTimeInMillis((Long) jsonObject.get("dateTime") * 1000);
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        return date;
    }

    private static boolean isCorrect(JSONObject jsonObject) {
        return jsonObject.keySet().containsAll(KEY_SET);
    }
}
