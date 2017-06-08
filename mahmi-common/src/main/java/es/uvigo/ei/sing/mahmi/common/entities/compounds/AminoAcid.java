package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import java.util.EnumSet;

import fj.data.HashMap;
import fj.data.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForTesting;

import static fj.Equal.charEqual;
import static fj.Hash.charHash;
import static fj.P.p;
import static fj.data.Stream.iterableStream;
import static fj.function.Characters.toUpperCase;

/**
 * {@linkplain AminoAcid} is a enum that represents an amino acid {@link Compound}
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Compound
 *
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AminoAcid implements Compound {

    ALA("Alanine"       , 'A'),
    ARG("Arginine"      , 'R'),
    ASN("Asparagine"    , 'N'),
    ASP("Aspartic acid" , 'D'),
    CYS("Cysteine"      , 'C'),
    GLU("Glutamic acid" , 'E'),
    GLN("Glutamine"     , 'Q'),
    GLY("Glycine"       , 'G'),
    HIS("Histidine"     , 'H'),
    ILE("Isoleucine"    , 'I'),
    LEU("Leucine"       , 'L'),
    LYS("Lysine"        , 'K'),
    MET("Methionine"    , 'M'),
    PHE("Phenylalanine" , 'F'),
    PRO("Proline"       , 'P'),
    SER("Serine"        , 'S'),
    THR("Threonine"     , 'T'),
    TRP("Tryptophan"    , 'W'),
    TYR("Tyrosine"      , 'Y'),
    VAL("Valine"        , 'V'),
    SEC("Selenocysteine", 'U'),
    PYL("Pyrrolysine"   , 'O'),

    // ambiguous amino-acids:
    ASX("Asparagine or aspartic acid", 'B'),
    GLX("Glutamine or glutamic acid" , 'Z'),
    XLE("Leucine or isoleucine"      , 'J'),
    XAA("Unknown"                    , 'X');

    @VisibleForTesting
    static final HashMap<Character, AminoAcid> codes = HashMap.from(
        iterableStream(EnumSet.allOf(AminoAcid.class)).map(aa -> p(aa.code, aa)).cons(p('*', XAA)),
        charEqual.contramap(toUpperCase),
        charHash.contramap(toUpperCase)
    );

    /**
     * The full name of the amino acid
     */
    private final String fullName;
    
    /**
     * The code of the amino acid
     */
    private final char   code;

    /**
     * Gets an amino acid from code
     * 
     * @param code The code of the amino acid
     * @return The amino acid as an {@code Option}
     */
    public static Option<AminoAcid> fromCode(final char code) {
        return codes.get(code);
    }

}
