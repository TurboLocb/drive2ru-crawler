import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoUtils {

    private static final Logger logger = LogManager.getLogger(AutoUtils.class);

    /**
     * @param input приходит в виде 'Продажа ГАЗ 31 - Нижний Новгород'
     * @return строку в виде 'ГАЗ 31'
     */
    public static String getAutoBrandByString(String input) {
        //тире в строке на drive2ru
        char drive2ruDashChar = Character.toChars(8212)[0];

        //'обычное' тире
        char dashChar = Character.toChars(45)[0];

        logger.info("");

        return input.replace(drive2ruDashChar, dashChar)
                .split("Продажа ")[1]
                .split("\\s-\\s")[0].trim();
    }

    public static String getAutoModelByInputBrand(String input, String brandString) {
        return input.replace(brandString, "").trim();
    }

    public static long getAutoPriceByInputSting(String input){
        //пробел в строке на drive2ruChar
        char drive2ruChar = Character.toChars(8201)[0];

        //'обычный' пробел
        char spaceChar = Character.toChars(32)[0];

        return Long.parseLong(input
                .replace("₽", "")
                .replace(drive2ruChar, spaceChar)
                .replaceAll("\\s", ""));
    }

    public static float getAutoEngineCapacityByInputString(String input){
        Pattern pattern = Pattern.compile("(\\d\\.\\d)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()){
            return Float.parseFloat(matcher.group());
        }

        return 0.0f;
    }

    public static int getAutoYearByInputString(String input){
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()){
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }

}
