package scc.db.blob;

import java.io.ByteArrayInputStream;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;

public class HouseContainer extends BlobContainer {

    HouseContainer(BlobContainerClient container) {
        super(container);
    }

    public void uploadImage(String id, byte[] image) {
        BlobClient blobClient = container.getBlobClient(id);
        
        blobClient.upload(new ByteArrayInputStream(image), image.length, true);
    }

    public byte[] getImage(String id) {
        BlobClient blobClient = container.getBlobClient(id);
        BinaryData image = blobClient.downloadContent();
        return image.toBytes();
    }
}
