package es.uvigo.ei.sing.mahmi.browser.runners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;

@Slf4j
@AllArgsConstructor(staticName = "blastp")
final public class BlastpRunner implements Runnable {

    private final Path         input;
    private final Path         output;
    private final String       db;
    private final BlastOptions blastOptions;

    @Override
    public void run() {
        try {
            val process = buildProcess().start();
            redirectErrorToLogs(process);
            checkExitValue(process.waitFor());
        } catch (final IOException | InterruptedException ie){
            log.error("BlastP error", ie);
            throw new RuntimeException(ie);
        }
    }

    private ProcessBuilder buildProcess() {
    	final List<String> blastWithOptions =  getBlastWithOptions();
        return new ProcessBuilder(
        		blastWithOptions.toArray(new String[blastWithOptions.size()])
        ).redirectOutput(output.toFile());
    }
    
    private List<String> getBlastWithOptions(){
    	final List<String> blast = new ArrayList<String>();
    	blast.add("blastp");
    	blast.add("-query");
    	blast.add(input.toString());
    	blast.add("-db");
    	blast.add(db);
    	blast.add("-evalue");
    	blast.add(String.valueOf(blastOptions.getEvalue()));
    	blast.add("-word_size");
    	blast.add(String.valueOf(blastOptions.getWord_size()));
    	blast.add("-gapopen");
    	blast.add(String.valueOf(blastOptions.getGapopen()));
    	blast.add("-gapextend");
    	blast.add(String.valueOf(blastOptions.getGapextend()));
    	blast.add("-threshold");
    	blast.add(String.valueOf(blastOptions.getBlast_threshold()));
    	blast.add("-num_alignments");
    	blast.add(String.valueOf(blastOptions.getNum_alignments()));
    	blast.add("-window_size");
    	blast.add(String.valueOf(blastOptions.getWindow_size()));

    	if(blastOptions.isUngapped()){
        	blast.add("-comp_based_stats");
    		blast.add("-ungapped");
    	}
    	if(blastOptions.getBest_hit_overhang() > 0){
        	blast.add("-best_hit_overhang");
    		blast.add(String.valueOf(blastOptions.getBest_hit_overhang()));    		
    	}
    	if(blastOptions.getBest_hit_score_edge() > 0){
        	blast.add("-best_hit_score_edge");
    		blast.add(String.valueOf(blastOptions.getBest_hit_score_edge()));    		
    	}
    	
    	return blast;
    }

    private void checkExitValue(final int value) throws IOException {
        if (value != 0) throw new IOException(
            MessageFormat.format("BlastP exited anormally ({})", value)
        );
    }

    private void redirectErrorToLogs(final Process proc) throws IOException {
        try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            stderr.lines().forEach(log::info);
        }
    }

}
