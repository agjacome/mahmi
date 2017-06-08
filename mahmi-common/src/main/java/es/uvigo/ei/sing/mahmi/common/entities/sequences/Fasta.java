package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static java.util.Collections.emptyIterator;

import java.util.Iterator;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Compound;
import fj.data.Stream;
import lombok.Value;

/**
 * {@linkplain Fasta} is a class that represents a parsed FASTA file
 * 
 * @author Alberto Gutierrez-Jacome
 *
 * @param <A> The specific compound sequence
 * 
 * @see Compound
 * @see CompoundSequence
 */
@Value(staticConstructor = "of")
public final class Fasta<A extends CompoundSequence<? extends Compound>> implements Iterable<A> {

    /**
     * The FASTA sequences
     */
    private final Iterator<A> sequences;

    /**
     * Gets a empty FASTA
     * 
     * @return A empty FASTA
     */
    public static <A extends CompoundSequence<? extends Compound>> Fasta<A> empty() {
        return of(emptyIterator());
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<A> iterator() {
        return sequences;
    }

    /**
     * Gets the {@code Stream} of the sequences
     * 
     * @return the {@code Stream} of the sequences
     */
    public Stream<A> toStream() {
        return Stream.iterableStream(() -> sequences);
    }

}
