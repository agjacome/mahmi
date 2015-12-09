package es.uvigo.ei.sing.mahmi.psort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(staticName = "psort")
final class PSortRunner implements Runnable {

    private final PSortGramMode mode;
    private final Path input;
    private final Path output;

    @Override
    public void run() {
        try {
            val process = buildProcess().start();
            redirectErrorToLogs(process);
            checkExitValue(process.waitFor());
        } catch (final IOException | InterruptedException ie){
            log.error("PSORT error", ie);
            throw new RuntimeException(ie);
        }
    }

    private ProcessBuilder buildProcess() {
        return new ProcessBuilder(
            "psort",
            mode.getModeFlag(),
            "-o=terse",
            "--verbose",
            input.toString()
        ).redirectOutput(output.toFile());
    }

    private void checkExitValue(final int value) throws IOException {
        if (value != 0) throw new IOException(
            String.format("PSORT exited anormally (%d)", value)
        );
    }

    private void redirectErrorToLogs(final Process proc) throws IOException {
        try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            stderr.lines().forEach(log::info);
        }
    }

}
