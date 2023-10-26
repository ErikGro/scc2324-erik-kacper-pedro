package scc.db.blob;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class BlobLayer {

    private static BlobLayer instance;
    private final BlobServiceClient client;
    public UserContainer usersContainer;
    public HouseContainer housesContainer;

    public static synchronized BlobLayer getInstance() {
		if(instance != null)
			return instance;

		instance = new BlobLayer();

		return instance;
	}

    private BlobLayer() {
        client = new BlobServiceClientBuilder()
            .connectionString(System.getenv("DB_BLOB_CONNECTION_STRING"))
            .buildClient();

        usersContainer = new UserContainer(client.getBlobContainerClient("images-user"));
        housesContainer = new HouseContainer(client.getBlobContainerClient("images-house"));
    }
}

