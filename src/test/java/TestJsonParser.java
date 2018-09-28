import main.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class TestJsonParser {

    private static JSONParser parser = new JSONParser();

    @Test
    public void testParsingSingleJsonFile() throws ParseException {
        String text = Main.fileToString("src/test/resources/23_08_2017_02_20_46-1379275764.json");
        JSONObject jsonObject = (JSONObject) parser.parse(text);
        assertTrue(!jsonObject.isEmpty());
    }

    @Test
    public void testParseArray() throws IOException, ParseException {
        String text = Main.fileToString("src/test/resources/F1FE052F-BD8B-48FC-AF2D-66D567835DA2.json");
        JSONArray jsonArray = (JSONArray) parser.parse(text);
        List list = jsonArray.subList(0, jsonArray.size());
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testPropertiesreading() throws IOException {
        Properties props = new Properties();
        props.load(new FileReader("src/main/resources/inn.properties"));
        assertTrue(props.containsKey("7825706086"));
    }
}
