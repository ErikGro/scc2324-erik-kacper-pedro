package scc.persistence.media;

public class FileSystemService implements MediaService {
    private static FileSystemService instance;
    private final FileSystemContainer usersContainer;
    private final FileSystemContainer housesContainer;

    public static synchronized FileSystemService getInstance() {
        if (instance != null)
            return instance;

        instance = new FileSystemService();

        return instance;
    }

    public FileSystemContainer getUsersContainer() {
        return usersContainer;
    }

    public FileSystemContainer getHousesContainer() {
        return housesContainer;
    }

    private FileSystemService() {
        // TODO: Replace
        String root = "/usr/local/tomcat/webapps/pedro-kacper-erik-backend-1.0/";

        usersContainer = new FileSystemContainer(root + "users/");
        housesContainer = new FileSystemContainer(root + "houses/");
    }
}
