package scc.db.blob;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import scc.utils.Constants;

public class BlobLayer {
    private static BlobLayer instance;
    public UserContainer usersContainer;
    public HouseContainer housesContainer;

    public static synchronized BlobLayer getInstance() {
        if (instance != null)
            return instance;

        instance = new BlobLayer();

        return instance;
    }

    private BlobLayer() {
        BlobServiceClient client = new BlobServiceClientBuilder()
                .connectionString(Constants.getBlobConnectionString())
                .buildClient();

        usersContainer = new UserContainer(client.getBlobContainerClient("images-user"));
        housesContainer = new HouseContainer(client.getBlobContainerClient("images-house"));
    }
}

