package main;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by Evgenii_Rybnikov on 24.08.2017.
 */
@Getter
public class Item {

    private static Properties inns = new Properties();

    static {
        try {
            inns.load(new FileReader("src/main/resources/inn.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Calendar date;
    private String userInn;
    private double sum;
    private String name;
    private double price;
    private double quantity;

    Item(Calendar date, String userInn, JSONObject j) {
        this.date = date;
        this.userInn = userInn;
        this.sum = (double) (Long) j.get("sum") / 100;
        this.name = (String) j.get("name");
        this.price = (double) (Long) j.get("price") / 100;
        this.quantity = (double) j.get("quantity");
    }

    @Override
    public String toString() {
        if (!inns.containsKey(userInn)) throw new RuntimeException("Unknown userInn: " + userInn);
        String magazine = inns.getProperty(userInn);
        return String.format("%td.%1$tm.%1$ty %1$tH:%1$tM\t\"%s\"\t%.3f\t%.4f\t%.3f\t%s",
                date, name, price, quantity, sum, magazine);
    }

}