package es.uvigo.ei.sing.mahmi.psort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.val;

import fj.data.List.Buffer;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;

import static fj.data.List.list;

@AllArgsConstructor(staticName = "of")
public final class PSortRunner {

    private static final FastaReader<AminoAcidSequence> proteinFastaReader = FastaReader.forAminoAcid();

    private final PSortGramMode            mode;
    private final EnumSet<PSortFilterType> filter;

    public Fasta<AminoAcidSequence> filter(final Path input) throws IOException {
        val output = input.resolveSibling("psortb.out");
        val psortb = PSortWrapper.run(mode, input, output);

        // TODO: It's probably better to launch an exception, or return an
        // Option instead of failing silently with an empty Fasta.
        if (!psortb) return Fasta.empty();

        val fasta = filterFasta(proteinFastaReader.fromPath(input), output);
        Files.deleteIfExists(output);
        return fasta;
    }

    private Fasta<AminoAcidSequence> filterFasta(
        final Fasta<AminoAcidSequence> fasta, final Path output
    ) throws IOException {
        val pattern   = PSortFilterType.compile(filter);

        val outLines  = list(Files.readAllLines(output)).tail();
        val fastaIter = fasta.iterator();

        // FIXME: all these filtered sequences are kept in memory, if the
        // original Fasta file is too big or if the filter does keep too many
        // sequences, this can cause memory-related problems.
        val filtered = Buffer.<AminoAcidSequence>empty();

        // TODO: can be done without a side-effectful loop, it's just zip(),
        // filter() and map().
        for (final String line : outLines) {
            val sequence = fastaIter.next();
            if (Pattern.matches(pattern, line))
                filtered.snoc(sequence);
        }

        return Fasta.of(filtered.iterator());
    }

}
