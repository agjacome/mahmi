package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOStreamsExtensionMethods {

    public static InputStream pipeToInput(final Consumer<OutputStream> osConsumer) throws IOException {
        val in  = new PipedInputStream();
        val out = new PipedOutputStream(in);

        new Thread(() -> osConsumer.accept(out)).start();

        return in;
    }

}
