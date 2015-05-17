package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class IOUtils {

    @DisallowConstruction
    private IOUtils() { }

    public static String read(final Path path) throws IOException {
        try (final BufferedReader br = Files.newBufferedReader(path, UTF_8)) {
            return read(br);
        }
    }

    public static String read(final URL url) throws IOException {
        try (final BufferedReader br = new BufferedReader(
            new InputStreamReader(url.openStream(), UTF_8)
        )) {
            return read(br);
        }
    }

    public static String read(final Readable readable) throws IOException {
        final StringBuilder sb = new StringBuilder();
        copy(readable, sb);
        return sb.toString();
    }

    public static void copy(final Readable from, final Appendable to) throws IOException {
        final CharBuffer buffer = CharBuffer.allocate(2048);

        while (true) {
            final int read = from.read(buffer);
            if (read == -1) break;

            buffer.flip();
            to.append(buffer, 0, read);
        }
    }

}
