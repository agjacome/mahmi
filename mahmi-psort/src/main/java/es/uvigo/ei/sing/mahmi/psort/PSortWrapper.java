package es.uvigo.ei.sing.mahmi.psort;

import java.io.IOException;
import java.nio.file.Path;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class PSortWrapper {

    public static boolean run(
        final PSortGramMode mode, final Path input, final Path output
    ) {
        try {
            val process = Runtime.getRuntime().exec(psort(mode, input, output));

            new Thread(() -> {
                logOutput(process);
                logErrors(process);
            }).start();

            return process.waitFor() == 0;
        } catch (final Exception e){
            log.error("PSORT error", e);
            return false;
        }
    }

    private static String[ ] psort(
        final PSortGramMode mode, final Path input, final Path output
    ) {
        return new String[ ] {
            "psort",
            mode.getModeFlag(),
            "-o=terse",
            "--verbose",
            input.toString(),
            " > " + output.toString()
        };
    }

    private static void logOutput(final Process process) {
        try (val inStream = process.getInputStream()) {
            log.info(IOUtils.toString(inStream, "UTF-8"));
        } catch(final IOException ioe){
            throw new RuntimeException(ioe);
        }
    }

    private static void logErrors(final Process process){
        try (val errStream = process.getErrorStream()) {
            log.error(IOUtils.toString(errStream, "UTF-8"));
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
