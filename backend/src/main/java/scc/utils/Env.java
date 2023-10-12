package scc.utils;

import io.github.cdimascio.dotenv.Dotenv;


public class Env {

    private static Dotenv dotenv;
    private static Env instance;

    public static synchronized Env getInstance() {
        if (instance == null) {
//            URL resource = YourClass.class.getResource("abc");
//            Paths.get(resource.toURI()).toFile();

            dotenv = Dotenv
                    .configure()
                    .load();
            
            instance = new Env();
        }

        return instance;
    }

    public String getDBConnectionUrl() {
        return "https://scc-pedro55921.documents.azure.com:443/";//dotenv.get("DB_CONNECTION_URL");
    }

    public String getDBKey() {
        return "Rk0dqIkq3mg0JYQ0aHeCsHuYpBP4G7A1bKEmjOc8FQMhLNkpteSctoJnvu96traHAdCnKZdRV5aBACDbEfaR9Q"; //dotenv.get("DB_KEY");
    }

    public String getDBName() {
        return "sccbackendusers";// dotenv.get("DB_NAME");
    }
}
