package scc.db.blob;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class BlobContainer {
    private final BlobContainerClient container;

    BlobContainer(BlobContainerClient container) {
        this.container = container;
    }

    public synchronized void upsertImage(String filename, byte[] image) {
        BlobClient blobClient = container.getBlobClient(filename);

        blobClient.upload(new ByteArrayInputStream(image), image.length, true);
    }

    public synchronized byte[] getImageBytes(String filename) {
        BlobClient blobClient = container.getBlobClient(filename);
        BinaryData image = blobClient.downloadContent();
        return image.toBytes();
    }

    public synchronized boolean deleteImage(String filename) {
        BlobClient blobClient = container.getBlobClient(filename);
        return blobClient.deleteIfExists();
    }
}
