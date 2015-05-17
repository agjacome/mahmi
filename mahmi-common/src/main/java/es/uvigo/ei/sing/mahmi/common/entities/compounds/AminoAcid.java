package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import static java.lang.Character.toUpperCase;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.mapify;

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

    ASX("Asparagine or aspartic acid", 'B'),
    GLX("Glutamine or glutamic acid" , 'Z'),
    XLE("Leucine or isoleucine"      , 'J'),
    XAA("Unknown"                    , 'X');

    private static final Map<Character, AminoAcid> codes;

    static {
        final Map<Character, AminoAcid> codeMap = mapify(
            EnumSet.allOf(AminoAcid.class), aa -> aa.getCode(), identity()
        );

        // special cases and synonyms
        codeMap.put('*', XAA);
        codeMap.put('-', XAA);

        codes = unmodifiableMap(codeMap);
    }

    private final String name;
    private final char   code;

    private AminoAcid(final String name, final char code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<AminoAcid> fromCode(final char code) {
        return Optional.ofNullable(codes.get(toUpperCase(code)));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public char getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.valueOf(getCode());
    }

}
