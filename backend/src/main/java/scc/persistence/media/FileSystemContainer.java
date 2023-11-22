package scc.persistence.media;

public class FileSystemContainer implements Container {
    public FileSystemContainer(String dirName) {

    }

    @Override
    public synchronized byte[] getImageBytes(String filename) {
        // TODO: Implement
        return new byte[0];
    }

    @Override
    public synchronized void deleteImage(String filename) {
        // TODO: Implement
    }

    @Override
    public synchronized void upsertImage(String filename, byte[] byteArray) {
        // TODO: Implement
    }
}
