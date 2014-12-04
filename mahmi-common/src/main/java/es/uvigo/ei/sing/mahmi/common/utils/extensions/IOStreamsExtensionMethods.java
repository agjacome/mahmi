package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import static fj.data.Validation.fail;
import static fj.data.Validation.success;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import fj.F;
import fj.data.Validation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOStreamsExtensionMethods {

    public static <A> Validation<IOException, InputStream> pipeToInput(final F<OutputStream, A> osConsumer) {
        try {
            val in  = new PipedInputStream();
            val out = new PipedOutputStream(in);

            new Thread(() -> osConsumer.f(out)).start();

            return success(in);
        } catch (final IOException ioe) {
            return fail(ioe);
        }
    }

}
