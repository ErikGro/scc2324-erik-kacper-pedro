package scc.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final boolean cachingEnabled = true;

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

    ///////////////////////////////// MongoDB Env /////////////////////////////////

    public static String getMongoDBConnectionString() {
        return System.getenv("MONGODB_CONNECTION_STRING");
    }

    ///////////////////////////////// Fileservice Env /////////////////////////////////

    public static String getMediaRootDir() {
        return System.getenv("MEDIA_ROOT_DIR");
    }
}
