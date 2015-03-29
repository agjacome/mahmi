package es.uvigo.ei.sing.mahmi.psort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;

import static es.uvigo.ei.sing.mahmi.psort.PSortRunner.psort;

@AllArgsConstructor(staticName = "of")
public final class PSortFastaFilter {

    private final PSortGramMode            mode;
    private final EnumSet<PSortFilterType> filter;

    public Fasta<AminoAcidSequence> filter(final Path input) throws IOException {
        val output = runPsort(input);
        val fasta  = filterFasta(
            FastaReader.forAminoAcid().fromPath(input), output
        );

        Files.deleteIfExists(output);

        return fasta;
    }

    private Path runPsort(final Path input) {
        val output = input.resolveSibling("psortb.out");
        psort(mode, input, output).run();
        return output;
    }

    // FIXME: all filtered sequences and contents of outfile are kept in memory,
    // if the original Fasta file is too big or if the filter does not delete
    // enough sequences, this can cause memory-related problems.
    private Fasta<AminoAcidSequence> filterFasta(
        final Fasta<AminoAcidSequence> fasta, final Path output
    ) throws IOException {
        val pattern = PSortFilterType.compile(filter);

        val outLines  = Files.lines(output).skip(1);
        val fastaIter = fasta.iterator();

        val filtered = new java.util.LinkedList<AminoAcidSequence>();

        outLines.forEach(line -> {
            val sequence = fastaIter.next();
            if (Pattern.matches(pattern, line)) filtered.add(sequence);
        });

        return Fasta.of(filtered.iterator());
    }

}
