package main;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.Calendar;

/**
 * Created by Evgenii_Rybnikov on 24.08.2017.
 */
@Getter
public class Item {

    private Calendar date;
    private String userInn;
    private double sum;
    private String name;
    private double price;
    private double quantity;

    public Item(Calendar date, String userInn, JSONObject j) {
        this.date = date;
        this.userInn = userInn;
        this.sum = (double) (Long) j.get("sum") / 100;
        this.name = (String) j.get("name");
        this.price = (double) (Long) j.get("price") / 100;
        this.quantity = (double) j.get("quantity");
    }

    @Override
    public String toString() {
        String magazine;
        switch (userInn) {
            case "7826087713": magazine = "Окей"; break;
            case "7825706086": magazine = "Пятерочка"; break;
            default:
                throw new RuntimeException("Unknown userInn: " + userInn);
        }
        return String.format("%td.%1$tm.%1$ty %1$tH:%1$tM\t\"%s\"\t%.3f\t%.4f\t%.3f\t%s", date, name, price, quantity, sum, magazine);
    }
}