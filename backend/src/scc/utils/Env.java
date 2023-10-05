package scc.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private static Dotenv dotenv;
    private static Env instance;

    public static synchronized Env getInstance() {
        if (instance == null) {
            dotenv = Dotenv
                    .configure()
                    .load();

            instance = new Env();
        }

        return instance;
    }

    public String getDBConnectionUrl() {
        return dotenv.get("DB_CONNECTION_URL");
    }

    public String getDBKey() {
        return dotenv.get("DB_KEY");
    }

    public String getDBName() {
        return dotenv.get("DB_NAME");
    }
}
