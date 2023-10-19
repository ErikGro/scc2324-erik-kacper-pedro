package scc.db.blob;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

public class HouseContainer extends BlobContainer {

    HouseContainer(BlobContainerClient container) {
        super(container);
    }

    // TODO: What to return?
    public String uploadImage(String id, byte[] image) {
        
        BlobClient blobClient = container.getBlobClient(id);
        BinaryData binaryImage = BinaryData.fromBytes(image);
        blobClient.upload(binaryImage);

        return "File uploaded successfully ";
    }


    public byte[] getImage(String id) {
        BlobClient blobClient = container.getBlobClient(id);

        BinaryData image = blobClient.downloadContent();

        return image.toBytes();
    }
}
