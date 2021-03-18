package utils;

import io.micronaut.context.annotation.Primary;

/**
 * Class that checks if a value is correct using its methods
 *
 * @Author Ana Garcia
 */
@Primary
public class CheckValue implements CheckValueInterface{
    @Override
    public boolean isEmpty(String string) {
        if (string != null && !("").equals(string)) {
            return false;
        }
        return true;
    }
}
