package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static fj.Equal.charEqual;
import static fj.data.Stream.iterableStream;
import static fj.function.Characters.toUpperCase;

import java.util.EnumSet;

import lombok.Getter;
import lombok.val;
import fj.Equal;
import fj.data.Option;
import fj.data.Stream;

/**
 * <p>
 * The Nucleobase enum represents any of the five nucleotide bases (A, C, G,
 * T/U) known in nature. Each one of them is named ({@link #getFullName}), and
 * has a 1-letter code ({@link #getCode}). A {@link EnumSet} of all the
 * nucleobases is statically available at {@link Nucleobase#all}.
 * </p>
 * <p>
 * Construction of a single {@link Nucleobase} is available in the common Java
 * enum way:
 *
 * <pre>
 * {@code
 * final Nucleobase adenosine = Nucleobase.A;
 * final Nucleobase thymine   = Nucleobase.T;
 * }
 * </pre>
 *
 * But also through the two available static factory methods (aka smart
 * constructors) {@link #fromCode} and {@link #fromFullName} in the following
 * way:
 *
 * <pre>
 * {@code
 * final Option<Nucleobase> guanine = Nucleobase.fromCode('G');
 * final Option<Nucleobase> uracil  = Nucleobase.fromFullName("Uracil");
 * }
 * </pre>
 *
 * They both return an {@link Option}, being {@link Option#some} if there exists
 * a nucleobase that corresponds to the given parameter, and {@link Option#none}
 * otherwise.
 * </p>
 */
@Getter
public enum Nucleobase implements ChemicalCompound {

    A("Adenine"  , 'A'),
    C("Cytosine" , 'C'),
    G("Guanine"  , 'G'),
    T("Thymine"  , 'T'),
    U("Uracil"   , 'U'),
    N("Unknown"  , 'N');

    public static final EnumSet<Nucleobase> all = EnumSet.allOf(Nucleobase.class);

    private final char   code;
    private final String fullName;

    private Nucleobase(final String fullName, final char code) {
        this.fullName = fullName;
        this.code     = code;
    }

    public static Option<Nucleobase> fromCode(final char code) {
        val eq = charEqual.comap(toUpperCase).eq(code);
        return iterableStream(all).find(a -> eq.f(a.code));
    }

    public static Option<Nucleobase> fromFullName(final String fullName) {
        val eq = Equal.<String>equal(s -> s::equalsIgnoreCase).eq(fullName);
        return iterableStream(all).find(a -> eq.f(a.fullName));
    }

    @Override
    public Iterable<Character> getShortName() {
        return Stream.single(code);
    }

    @Override
    public String toString() {
        return fullName;
    }

}

