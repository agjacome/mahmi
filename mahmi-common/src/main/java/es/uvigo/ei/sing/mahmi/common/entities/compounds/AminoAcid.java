package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static es.uvigo.ei.sing.mahmi.common.utils.Validators.requireNonNull;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.enumToMap;
import static fj.data.vector.V.v;
import static java.lang.Character.toUpperCase;

import java.util.Map;

import lombok.Getter;
import fj.data.vector.V3;

@Getter
public enum AminoAcid implements ChemicalCompound {

    ALA("Alanine"       , 'A', v('A', 'L', 'A')),
    ARG("Arginine"      , 'R', v('A', 'R', 'G')),
    ASN("Asparagine"    , 'N', v('A', 'S', 'N')),
    ASP("Aspartic acid" , 'D', v('A', 'S', 'P')),
    CYS("Cysteine"      , 'C', v('C', 'Y', 'S')),
    GLU("Glutamic acid" , 'E', v('G', 'L', 'U')),
    GLN("Glutamine"     , 'Q', v('G', 'L', 'N')),
    GLY("Glycine"       , 'G', v('G', 'L', 'Y')),
    HIS("Histidine"     , 'H', v('H', 'I', 'S')),
    ILE("Isoleucine"    , 'I', v('I', 'L', 'E')),
    LEU("Leucine"       , 'L', v('L', 'E', 'U')),
    LYS("Lysine"        , 'K', v('L', 'Y', 'S')),
    MET("Methionine"    , 'M', v('M', 'E', 'T')),
    PHE("Phenylalanine" , 'F', v('P', 'H', 'E')),
    PRO("Proline"       , 'P', v('P', 'R', 'O')),
    SER("Serine"        , 'S', v('S', 'E', 'R')),
    THR("Threonine"     , 'T', v('T', 'H', 'R')),
    TRP("Tryptophan"    , 'W', v('T', 'R', 'P')),
    TYR("Tyrosine"      , 'Y', v('T', 'Y', 'R')),
    VAL("Valine"        , 'V', v('V', 'A', 'L')),
    SEC("Selenocysteine", 'U', v('S', 'E', 'C')),
    PYL("Pyrrolysine"   , 'O', v('P', 'Y', 'L')),

    // ambiguous amino-acids:
    ASX("Asparagine or aspartic acid", 'B', v('A', 'S', 'X')),
    GLX("Glutamine or glutamic acid" , 'Z', v('G', 'L', 'X')),
    XLE("Leucine or isoleucine"      , 'J', v('X', 'L', 'E')),
    XAA("Unknown"                    , 'X', v('X', 'A', 'A'));

    private static final Map<Character, AminoAcid> codeMapper = enumToMap(
        AminoAcid.class, AminoAcid::getCode, a -> a
    );

    private final char          code;
    private final V3<Character> shortName;
    private final String        fullName;

    static { codeMapper.put('*', XAA); }

    private AminoAcid(
        final String        fullName,
        final char          code,
        final V3<Character> shortName
    ) {
        this.code      = code;
        this.fullName  = fullName;
        this.shortName = shortName;
    }

    public static AminoAcid fromCode(final char code) {
        return requireNonNull(
            codeMapper.get(toUpperCase(code)),
            () -> new IllegalArgumentException("Invalid AminoAcid code: " + code)
        );
    }

}
