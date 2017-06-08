package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import static fj.data.Option.none;
import static fj.data.Option.some;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Monoid;
import fj.data.Option;
import fj.data.Seq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

/**
 * {@linkplain NucleobaseSequence} is a class that represents a sequence of {@link Nucleobase}s
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see CompoundSequence
 * @see Nucleobase
 */
@Getter
@AllArgsConstructor(staticName = "fromSeq")
public final class NucleobaseSequence extends CompoundSequence<Nucleobase> {

    public static final Monoid<NucleobaseSequence> monoid =
        monoid(n1 -> n2 -> fromSeq(n1.residues.append(n2.residues)), empty());

    /**
     * The {@code Seq) of {@link Nucleobase}s
     */
    private final Seq<Nucleobase> residues;

    /**
     * Gets an empty {@linkplain NucleobaseSequence}
     * 
     * @return an empty {@linkplain NucleobaseSequence}
     */
    public static NucleobaseSequence empty() {
        return fromSeq(Seq.empty());
    }

	/**
	 * Gets a {@linkplain NucleobaseSequence} from a {@code String} as
	 * {@code Option}
	 * 
	 * @param sequence
	 *            The nucleo base sequence
	 * @return A {@linkplain NucleobaseSequence} from a {@code String} as
	 *         {@code Option}
	 */
	public static Option<NucleobaseSequence> fromString(
        final String sequence
    ) {
        Seq<Nucleobase> seq = Seq.empty();

        for (val code : sequence.toCharArray()) {
            val aa = Nucleobase.fromCode(code);
            if (aa.isNone()) return none();
            else seq = seq.snoc(aa.some());
        }

        return some(fromSeq(seq));
    }

}
