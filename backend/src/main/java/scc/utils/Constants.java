package scc.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static String getApplicationURL() {
        String appName = System.getenv("WEBSITE_SITE_NAME");
        return "https://" + appName + ".azurewebsites.net";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean cachingEnabled = true;
    public static boolean azureFunctions = true;
}
