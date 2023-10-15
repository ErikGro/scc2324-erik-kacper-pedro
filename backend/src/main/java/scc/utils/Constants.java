package scc.utils;

public class Constants {
    public static String getApplicationURL() {
        String appName = System.getenv("WEBSITE_SITE_NAME");
        return "https://" + appName + ".azurewebsites.net";
    }
}
