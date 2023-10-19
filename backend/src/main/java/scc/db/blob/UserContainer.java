package scc.db.blob;

import com.azure.storage.blob.BlobContainerClient;

public class UserContainer extends BlobContainer {

    UserContainer(BlobContainerClient container) {
        super(container);
    }
    
}
