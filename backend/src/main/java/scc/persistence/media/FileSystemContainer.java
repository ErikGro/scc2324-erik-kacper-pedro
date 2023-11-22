package scc.persistence.media;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

public class FileSystemContainer implements Container {
    private final String dirName;
    public FileSystemContainer(String dirName) {
        this.dirName = dirName;
    }

    @Override
    public synchronized Optional<byte[]> getImageBytes(String filename) {
        try {
            Files.readAllBytes(new File(dirName + filename).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public synchronized boolean deleteImage(String filename) {
        return new File(dirName + filename).delete();
    }

    @Override
    public synchronized void upsertImage(String filename, byte[] byteArray) {
        try (FileOutputStream fos = new FileOutputStream(dirName + filename)) {
            fos.write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
