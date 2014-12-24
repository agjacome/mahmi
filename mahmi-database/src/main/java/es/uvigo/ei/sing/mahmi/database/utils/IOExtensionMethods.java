package es.uvigo.ei.sing.mahmi.database.utils;

import static java.lang.System.lineSeparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOExtensionMethods {

    public static InputStream pipeToInput(
        final Consumer<OutputStream> outputConsumer
    ) throws IOException {
        val in  = new PipedInputStream();
        val out = new PipedOutputStream(in);

        new Thread(() -> outputConsumer.accept(out)).start();

        return in;
    }

    public static String inputToString(
        final InputStream input
    ) throws IOException {
        val sb = new StringBuilder();

        try (val reader = new BufferedReader(new InputStreamReader(input))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(lineSeparator());
            }
        }

        return sb.toString();
    }

}
