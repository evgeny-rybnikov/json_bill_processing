package main;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Evgenii_Rybnikov on 23.08.2017.
 */
public class Main {

    private static JSONParser parser = new JSONParser();

    private static final String ROOT_PATH = "D:/bills/json";
    private final static Set<String> KEY_SET = new HashSet<>(
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
                .map(File::getAbsolutePath)
                .map(Main::toString)
                .map(Main::getItems)
                .flatMap(List::stream)
                .forEach(System.out::println);
    }

    private static List<Item> getItems(String json) {
        List<JSONObject> jsons;
        try {
            jsons = toJsonObject(json);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing json", e);
        }
        jsons.forEach(j -> {
            if (!isCorrect(j))
                throw new RuntimeException("JSON doesn't match the format");
        });

        assert(jsons.size() != 0);
        List<Pair<JSONObject, JSONObject>> pairs = new ArrayList<>(jsons.size() * jsons.get(0).size());
        for (JSONObject j : jsons) {
            JSONArray array = (JSONArray) j.get("items");
            for (Object o : array) {
                JSONObject i = (JSONObject) o;
                pairs.add(new Pair<>(j, i));
            }
        }

        List<Item> itemList = new ArrayList<>(pairs.size());
        for (Pair<JSONObject, JSONObject> p : pairs) {
            Calendar date = getDate(p.getKey());
            String userInn = (String) p.getKey().get("userInn");
            itemList.add(new Item(date, userInn, p.getValue()));
        }
        return itemList;
    }

    private static List<JSONObject> toJsonObject(String json) throws ParseException {
        Object obj = parser.parse(json);
        if (obj instanceof JSONObject) {
            return Arrays.asList((JSONObject) obj);
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            return jsonArray.subList(0, jsonArray.size());
        } else {
            throw new RuntimeException("Unknown JSON object type");
        }
    }

    private static boolean isCorrect(JSONObject jsonObject) {
        return jsonObject.keySet().containsAll(KEY_SET);
    }

    public static String toString(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path, e);
        }
    }

    private static Calendar getDate(JSONObject jsonObject) {
        Calendar date = Calendar.getInstance(Locale.getDefault());
        date.setTimeInMillis((Long) jsonObject.get("dateTime") * 1000);
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        return date;
    }
}
