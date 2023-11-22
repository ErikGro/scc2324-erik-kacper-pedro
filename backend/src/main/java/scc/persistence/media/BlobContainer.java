package scc.persistence.media;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import java.io.ByteArrayInputStream;

public class BlobContainer implements Container {
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

    public synchronized void deleteImage(String filename) {
        BlobClient blobClient = container.getBlobClient(filename);
        blobClient.deleteIfExists();
    }
}
