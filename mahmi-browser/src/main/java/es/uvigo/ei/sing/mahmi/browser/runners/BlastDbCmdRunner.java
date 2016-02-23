package es.uvigo.ei.sing.mahmi.browser.runners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(staticName = "blastdbcmd")
final public class BlastDbCmdRunner implements Runnable {

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
            log.error("BlastDbCmd error", ie);
            throw new RuntimeException(ie);
        }
    }

    private ProcessBuilder buildProcess() {
        return new ProcessBuilder(
            "blastdbcmd",
            "-db",
            db,
            "-dbtype",
            "prot",
            "-entry_batch",
            input.toString()
        ).redirectOutput(output.toFile());
    }

    private void checkExitValue(final int value) throws IOException {
        if (value != 0) throw new IOException(
            MessageFormat.format("BlastDbCmd exited anormally ({})", value)
        );
    }

    private void redirectErrorToLogs(final Process proc) throws IOException {
        try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            stderr.lines().forEach(log::info);
        }
    }

}
