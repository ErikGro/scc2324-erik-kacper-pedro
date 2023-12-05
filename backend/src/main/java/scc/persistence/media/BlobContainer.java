package scc.persistence.media;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class BlobContainer implements Container {
    private final BlobContainerClient container;

    BlobContainer(BlobContainerClient container) {
        this.container = container;
    }

    public synchronized void upsertImage(String filename, byte[] image) {
        BlobClient blobClient = container.getBlobClient(filename);

        blobClient.upload(new ByteArrayInputStream(image), image.length, true);
    }

    public synchronized Optional<byte[]> getImageBytes(String filename) {
        BlobClient blobClient = container.getBlobClient(filename);
        BinaryData image = blobClient.downloadContent();
        return Optional.ofNullable(image.toBytes());
    }

    public synchronized boolean deleteImage(String filename) {
        BlobClient blobClient = container.getBlobClient(filename);
        blobClient.deleteIfExists();
        return false;
    }
}
