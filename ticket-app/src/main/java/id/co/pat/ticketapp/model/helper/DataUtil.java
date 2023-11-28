package id.co.pat.ticketapp.model.helper;

public class DataUtil {

    private static DataUtil single_instance = null;

    private DataUtil() {

    }

    public static synchronized DataUtil getInstance() {
        if (single_instance == null)
            single_instance = new DataUtil();

        return single_instance;
    }

    public String getVisibleEventId(long lastEventId) {
        return "";
    }

}
