package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static fj.Equal.charEqual;
import static fj.data.Stream.iterableStream;
import static fj.data.vector.V.v;
import static fj.function.Characters.toUpperCase;

import java.util.EnumSet;

import lombok.Getter;
import lombok.val;
import fj.Equal;
import fj.data.Option;
import fj.data.vector.V3;

/**
 * <p>
 * The AminoAcid enum represents any of the 22 standard amino-acids known in
 * nature plus the four ambiguous ones. Each one of them is named
 * ({@link #getFullName}), has a 3-letter code ({@link #getShortName})
 * represented as a {@link V3} vector, and has a single-character code
 * ({@link #getCode}). A {@link EnumSet} of all the 22 amino-acids is
 * statically available at {@link AminoAcid#all}.
 * </p>
 * <p>
 * Construction of a single {@link AminoAcid} is available in the common Java
 * enum way:
 *
 * <pre>
 * {@code
 * final AminoAcid alanine  = AminoAcid.ALA;
 * final AminoAcid tyrosine = AminoAcid.TYR;
 * }
 * </pre>
 *
 * But also through the two available static factory methods (aka smart
 * constructors) {@link #fromCode} and {@link #fromFullName} in the following
 * way:
 *
 * <pre>
 * {@code
 * final Option<AminoAcid> serline = AminoAcid.fromCode('S');
 * final Option<AminoAcid> leucine = AminoAcid.fromFullName("Leucine");
 * }
 * </pre>
 *
 * They both return an {@link Option}, being {@link Option#some} if there exists
 * an aminoacid that corresponds to the given parameter, and {@link Option#none}
 * otherwise.
 * </p>
 */
@Getter
public enum AminoAcid implements ChemicalCompound {

    ALA("Alanine"        , 'A' , v('A', 'L', 'A')),
    ARG("Arginine"       , 'R' , v('A', 'R', 'G')),
    ASN("Asparagine"     , 'N' , v('A', 'S', 'N')),
    ASP("Aspartic acid"  , 'D' , v('A', 'S', 'P')),
    CYS("Cysteine"       , 'C' , v('C', 'Y', 'S')),
    GLU("Glutamic acid"  , 'E' , v('G', 'L', 'U')),
    GLN("Glutamine"      , 'Q' , v('G', 'L', 'N')),
    GLY("Glycine"        , 'G' , v('G', 'L', 'Y')),
    HIS("Histidine"      , 'H' , v('H', 'I', 'S')),
    ILE("Isoleucine"     , 'I' , v('I', 'L', 'E')),
    LEU("Leucine"        , 'L' , v('L', 'E', 'U')),
    LYS("Lysine"         , 'K' , v('L', 'Y', 'S')),
    MET("Methionine"     , 'M' , v('M', 'E', 'T')),
    PHE("Phenylalanine"  , 'F' , v('P', 'H', 'E')),
    PRO("Proline"        , 'P' , v('P', 'R', 'O')),
    SER("Serine"         , 'S' , v('S', 'E', 'R')),
    THR("Threonine"      , 'T' , v('T', 'H', 'R')),
    TRP("Tryptophan"     , 'W' , v('T', 'R', 'P')),
    TYR("Tyrosine"       , 'Y' , v('T', 'Y', 'R')),
    VAL("Valine"         , 'V' , v('V', 'A', 'L')),
    SEC("Selenocysteine" , 'U' , v('S', 'E', 'C')),
    PYL("Pyrrolysine"    , 'O' , v('P', 'Y', 'L')),

    // ambiguous amino-acids:
    ASX("Asparagine or aspartic acid" , 'B' , v('A', 'S', 'X')),
    GLX("Glutamine or glutamic acid"  , 'Z' , v('G', 'L', 'X')),
    XLE("Leucine or isoleucine"       , 'J' , v('X', 'L', 'E')),
    XAA("Unknown"                     , 'X' , v('X', 'A', 'A'));

    public static final EnumSet<AminoAcid> all = EnumSet.allOf(AminoAcid.class);

    private final char          code;
    private final V3<Character> shortName;
    private final String        fullName;

    private AminoAcid(final String fullName, final char code, final V3<Character> shortName) {
        this.code      = code;
        this.fullName  = fullName;
        this.shortName = shortName;
    }

    public static Option<AminoAcid> fromCode(final char code) {
        val eq = charEqual.comap(toUpperCase).eq(code);
        return eq.f('*') ? Option.some(XAA) : iterableStream(all).find(a -> eq.f(a.code));
    }

    public static Option<AminoAcid> fromFullName(final String fullName) {
        val eq = Equal.<String>equal(s -> s::equalsIgnoreCase).eq(fullName);
        return iterableStream(all).find(a -> eq.f(a.fullName));
    }

    @Override
    public String toString() {
        return getFullName();
    }

}
