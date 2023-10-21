package scc.db.blob;

import java.io.ByteArrayInputStream;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;

// Import BLOB equivalent to import com.azure.cosmos.models.CosmosItemResponse;

public class UserContainer extends BlobContainer {

    UserContainer(BlobContainerClient container) {
        super(container);
    }

    // TODO: What to return?
    public String uploadImage(String id, byte[] image) {
        
        BlobClient blobClient = container.getBlobClient(id);
        
        blobClient.upload(new ByteArrayInputStream(image), image.length, true);

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("image/jpeg");
        blobClient.setHttpHeaders(headers);

        return "File uploaded successfully ";
    }


    public byte[] getImage(String id) {
        BlobClient blobClient = container.getBlobClient(id);
        BinaryData image = blobClient.downloadContent();
        return image.toBytes();
    }
}
