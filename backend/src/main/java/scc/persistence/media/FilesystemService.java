package scc.persistence.media;

public class FilesystemService implements MediaService {
    private static FilesystemService instance;
    private final FileSystemContainer usersContainer;
    private final FileSystemContainer housesContainer;

    public static synchronized FilesystemService getInstance() {
        if (instance != null)
            return instance;

        instance = new FilesystemService();

        return instance;
    }

    public FileSystemContainer getUsersContainer() {
        return usersContainer;
    }

    public FileSystemContainer getHousesContainer() {
        return housesContainer;
    }

    private FilesystemService() {
        usersContainer = new FileSystemContainer("users");
        housesContainer = new FileSystemContainer("houses");
    }
}
