package scc.persistence.media;

public interface Container {
    byte[] getImageBytes(String filename);
    void deleteImage(String filename);
    void upsertImage(String filename, byte[] byteArray);
}
