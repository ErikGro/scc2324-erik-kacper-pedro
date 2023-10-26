package scc.db.blob;

import com.azure.storage.blob.BlobContainerClient;

abstract class BlobContainer {
    protected BlobContainerClient container;

    BlobContainer(BlobContainerClient container) {
        this.container = container;
    }
}
