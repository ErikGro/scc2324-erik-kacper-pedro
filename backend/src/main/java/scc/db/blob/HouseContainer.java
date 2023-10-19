package scc.db.blob;

import com.azure.storage.blob.BlobContainerClient;

public class HouseContainer extends BlobContainer {

    HouseContainer(BlobContainerClient container) {
        super(container);
    }
    
}
