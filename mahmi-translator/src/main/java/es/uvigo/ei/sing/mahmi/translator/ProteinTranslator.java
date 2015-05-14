package es.uvigo.ei.sing.mahmi.translator;

import static es.uvigo.ei.sing.mahmi.translator.BlastDbCmdRunner.blastdbcmd;
import static es.uvigo.ei.sing.mahmi.translator.BlastxRunner.blastx;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
public class ProteinTranslator {
	private final String db;
	
	public	 Path runBlastX(final Path input) {
        val output = input.resolveSibling("blastx.out");
        blastx(input, output, db).run();
        return output;
    }
	
	public Path runBlastDbCmd(final String input, final Path output) {
        blastdbcmd(input, output).run();
        return output;
    }
	
	
	
	
}
