package scc.db.blob;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import scc.utils.Constants;

public class BlobService {
    private static BlobService instance;
    private final BlobContainer usersContainer;
    private final BlobContainer housesContainer;

    public static synchronized BlobService getInstance() {
        if (instance != null)
            return instance;

        instance = new BlobService();

        return instance;
    }

    public BlobContainer getUsersContainer() {
        return usersContainer;
    }

    public BlobContainer getHousesContainer() {
        return housesContainer;
    }

    private BlobService() {
        BlobServiceClient client = new BlobServiceClientBuilder()
                .connectionString(Constants.getBlobConnectionString())
                .buildClient();

        usersContainer = new BlobContainer(client.getBlobContainerClient("images-user"));
        housesContainer = new BlobContainer(client.getBlobContainerClient("images-house"));
    }
}

