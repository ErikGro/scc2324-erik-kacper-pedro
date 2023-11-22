package scc.persistence.media;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;

public class FileSystemContainer implements Container {
    private final File dir;
    public FileSystemContainer(String dirName) {
        this.dir = new File(dirName);;
    }

    @Override
    public synchronized Optional<byte[]> getImageBytes(String filename) {
        try {
            return Optional.of(Files.readAllBytes(getFile(filename).toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized boolean deleteImage(String filename) {
        return getFile(filename).delete();
    }

    @Override
    public synchronized void upsertImage(String filename, byte[] byteArray) {
        File file = getFile(filename);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            fos.write(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    File getFile(String filename) {
        return new File(dir, filename);
    }
}
