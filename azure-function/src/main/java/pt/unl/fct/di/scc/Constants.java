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
}
