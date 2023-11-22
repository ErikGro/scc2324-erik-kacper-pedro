package scc.persistence.media;

import java.util.Optional;

public interface Container {
    Optional<byte[]> getImageBytes(String filename);
    boolean deleteImage(String filename);
    void upsertImage(String filename, byte[] byteArray);
}
