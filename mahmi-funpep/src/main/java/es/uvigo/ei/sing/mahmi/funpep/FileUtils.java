package es.uvigo.ei.sing.mahmi.funpep;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.util.Objects.nonNull;

public final class FileUtils {

    // Disallow construction
    private FileUtils() { }

    public static void deleteDirectory(final Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(
                final Path file, final BasicFileAttributes attrs
            ) throws IOException {
                Files.deleteIfExists(file);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(
                final Path file, final IOException err
            ) throws IOException {
                if (nonNull(err)) Files.deleteIfExists(file);
                return CONTINUE;
            }

        });
    }
}