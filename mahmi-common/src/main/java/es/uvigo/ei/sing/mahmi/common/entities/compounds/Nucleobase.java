package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static es.uvigo.ei.sing.mahmi.common.utils.Validators.requireNonNull;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.enumToMap;
import static java.lang.Character.toUpperCase;

import java.util.Map;

import lombok.Getter;
import fj.data.List;

@Getter
public enum Nucleobase implements ChemicalCompound {

    A("Adenine" , 'A'),
    C("Cytosine", 'C'),
    G("Guanine" , 'G'),
    T("Thymine" , 'T'),
    U("Uracil"  , 'U'),
    N("Unknown" , 'N');

    private static final Map<Character, Nucleobase> codeMapper = enumToMap(
        Nucleobase.class, Nucleobase::getCode, n -> n
    );

    private final char            code;
    private final String          fullName;
    private final List<Character> shortName;


    private Nucleobase(final String fullName, final char code) {
        this.code      = code;
        this.fullName  = fullName;
        this.shortName = List.single(code);
    }

    public static Nucleobase fromCode(final char code) {
        return requireNonNull(
            codeMapper.get(toUpperCase(code)),
            () -> new IllegalArgumentException("Invalid Nucleobase code: " + code)
        );
    }

}

