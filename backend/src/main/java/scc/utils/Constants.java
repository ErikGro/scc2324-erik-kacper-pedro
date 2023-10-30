package scc.utils;

import com.azure.storage.blob.BlobServiceClientBuilder;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static String getApplicationURL() {
        String appName = System.getenv("WEBSITE_SITE_NAME");
        return "https://" + appName + ".azurewebsites.net";
    }

    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean cachingEnabled = true;

    public static boolean azureFunctionsEnabled = true;

    ///////////////////////////////// DB Env /////////////////////////////////

    public static String getDBConnectionURL() {
        return System.getenv("DB_CONNECTION_URL");
    }

    public static String getDBName() {
        return System.getenv("DB_NAME");
    }

    public static String getDBKey() {
        return System.getenv("DB_KEY");
    }

    ///////////////////////////////// Redis Env /////////////////////////////////

    public static String getRedisHostname() {
        return System.getenv("REDIS_HOSTNAME");
    }

    public static String getRedisKey() {
        return System.getenv("REDIS_KEY");
    }

    ///////////////////////////////// Blob Env /////////////////////////////////

    public static String getBlobConnectionString() {
        return System.getenv("DB_BLOB_CONNECTION_STRING");
    }
}
