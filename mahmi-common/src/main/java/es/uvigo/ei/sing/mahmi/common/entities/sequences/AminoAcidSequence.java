package es.uvigo.ei.sing.mahmi.common.entities.sequences;

import static fj.Monoid.monoid;
import static fj.data.Option.none;
import static fj.data.Option.some;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import fj.Monoid;
import fj.data.Option;
import fj.data.Seq;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;

/**
 * {@linkplain AminoAcidSequence} is a class that represents a sequence of
 * {@link AminoAcid}s
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see CompoundSequence
 * @see AminoAcid
 */
@Value(staticConstructor = "fromSeq")
@EqualsAndHashCode(callSuper = false)
public final class AminoAcidSequence extends CompoundSequence<AminoAcid> {

	public static final Monoid<AminoAcidSequence> monoid = monoid(
			a1 -> a2 -> fromSeq(a1.residues.append(a2.residues)), empty());

	/**
	 * The {@code Seq) of {@link AminoAcid}s
	 */
	private final Seq<AminoAcid> residues;

	/**
	 * Gets an empty {@linkplain AminoAcidSequence}
	 * 
	 * @return an empty {@linkplain AminoAcidSequence}
	 */
	public static AminoAcidSequence empty() {
		return fromSeq(Seq.empty());
	}

	/**
	 * Gets a {@linkplain AminoAcidSequence} from a {@code String} as
	 * {@code Option}
	 * 
	 * @param sequence
	 *            The amino acid sequence
	 * @return A {@linkplain AminoAcidSequence} from a {@code String} as
	 *         {@code Option}
	 */
	public static Option<AminoAcidSequence> fromString(final String sequence) {
		Seq<AminoAcid> seq = Seq.empty();

		for (val code : sequence.toCharArray()) {
			val aa = AminoAcid.fromCode(code);
			if (aa.isNone())
				return none();
			else
				seq = seq.snoc(aa.some());
		}

		return some(fromSeq(seq));
	}

}
