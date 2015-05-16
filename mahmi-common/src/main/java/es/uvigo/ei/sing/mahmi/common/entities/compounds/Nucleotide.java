package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import static java.lang.Character.toUpperCase;
import static java.util.function.Function.identity;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.IterableUtils.mapify;

public enum Nucleotide implements Compound {

    A("Adenine" , 'A'),
    C("Cytosine", 'C'),
    G("Guanine" , 'G'),
    T("Thymine" , 'T'),
    U("Uracil"  , 'U'),
    N("Unknown" , 'N');

    private static final Map<Character, Nucleotide> codes = mapify(
        EnumSet.allOf(Nucleotide.class), aa -> aa.getCode(), identity()
    );

    private final String name;
    private final char   code;

    private Nucleotide(final String name, final char code) {
        this.name = name;
        this.code = code;
    }

    public static Optional<Nucleotide> fromCode(final char code) {
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
