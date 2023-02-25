package dev.thrako.receptaria.utilities;

public class DoubleParser {

    public static Double parse (String input) {
        Double quantity = null;
        try {
            quantity = Double.parseDouble(input);
        } catch (NumberFormatException ignored) {

        }

        return quantity;
    }

}
