package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Equal.streamEqual;
import static fj.Hash.streamHash;
import static fj.Show.streamShow;
import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import fj.Equal;
import fj.Hash;
import fj.Show;
import fj.data.Stream;

@Value(staticConstructor = "of")
public final class Fasta<A extends CompoundSequence<? extends Compound>> {

    public static final Hash<Fasta<CompoundSequence<? extends Compound>>> hash =
        streamHash(CompoundSequence.hash).comap(Fasta::getSequences);

    public static final Equal<Fasta<CompoundSequence<? extends Compound>>> equal =
        streamEqual(CompoundSequence.equal).comap(Fasta::getSequences);

    public static final Show<Fasta<CompoundSequence<? extends Compound>>> show =
        streamShow(CompoundSequence.show).comap(Fasta::getSequences);

    private final Stream<A> sequences;

    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> empty() {
        return of(Stream.nil());
    }

}
