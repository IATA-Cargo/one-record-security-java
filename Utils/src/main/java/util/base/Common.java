package util.base;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Common {

    private final static String STRING_EMPTY = "";

    public static long getLongTimeStamp() {
        //TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        final Instant instant = Instant.now();
        final long timeStampMillis = instant.toEpochMilli();
        return timeStampMillis / 1000;
    }

    public static String getDatetimeString() {
        String result = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(System.currentTimeMillis());
        result = formatter.format(date);

        return result;
    }

    public static boolean isStringEmpty(String strValue) {
        if (strValue == null) return true;
        if (STRING_EMPTY.equals(strValue)) return true;
        if (STRING_EMPTY.equals(strValue.trim())) return true;
        return false;
    }

    public static String toJsonPrettyString(Object data) {
        try {
            if (data == null) {
                return STRING_EMPTY;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            return gson.toJson(data);
        } catch (Exception e) {
            return STRING_EMPTY;
        }
    }

    public static <T> T fromJson(Class<T> xclass, String data) {
        try {
            if (isStringEmpty(data)) {
                return null;
            }

            Gson converter = new Gson();
            T result = converter.fromJson(data, xclass);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
