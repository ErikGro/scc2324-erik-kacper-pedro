package pt.unl.fct.di.scc;

public class Constants {
    public static String getDBConnectionURL() {
        return System.getenv("DB_CONNECTION_URL");
    }

    public static String getDBName() {
        return System.getenv("DB_NAME");
    }

    public static String getDBKey() {
        return System.getenv("DB_KEY");
    }

    public static String getRedisHostname() {
        return System.getenv("REDIS_HOSTNAME");
    }

    public static String getRedisKey() {
        return System.getenv("REDIS_KEY");
    }
    
    public static String getMongoDBConnectionString() {
        return System.getenv("MONGODB_CONNECTION_STRING");
    }
}
