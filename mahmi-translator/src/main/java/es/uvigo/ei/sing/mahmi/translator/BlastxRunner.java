package es.uvigo.ei.sing.mahmi.translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Deprecated
@Slf4j
@AllArgsConstructor(staticName = "blastx")
final class BlastxRunner implements Runnable {

    private final Path input;
    private final Path output;
    private final String db;

    @Override
    public void run() {
        try {
            val process = buildProcess().start();
            redirectErrorToLogs(process);
            checkExitValue(process.waitFor());
        } catch (final IOException | InterruptedException ie){
            log.error("BlastX error", ie);
            throw new RuntimeException(ie);
        }
    }

    private ProcessBuilder buildProcess() {
        return new ProcessBuilder(
            "blastx",
            "-db",
            db,
            "-query",
            input.toString()
        ).redirectOutput(output.toFile());
    }

    private void checkExitValue(final int value) throws IOException {
        if (value != 0) throw new IOException(
            MessageFormat.format("BlastX exited anormally ({})", value)
        );
    }

    private void redirectErrorToLogs(final Process proc) throws IOException {
        try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            stderr.lines().forEach(log::info);
        }
    }

}
