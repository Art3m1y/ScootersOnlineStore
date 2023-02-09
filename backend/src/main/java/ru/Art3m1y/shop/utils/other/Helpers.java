package ru.Art3m1y.shop.utils.other;

import org.springframework.stereotype.Component;

@Component
public class Helpers {
    public boolean checkConvertFromStringToLong(String number) {
        try {
            Long number_converted = Long.parseLong(number);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean checkConvertFromStringToInteger(String number) {
        try {
            int number_converted = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
