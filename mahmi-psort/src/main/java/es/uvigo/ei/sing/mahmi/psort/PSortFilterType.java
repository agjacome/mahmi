package es.uvigo.ei.sing.mahmi.psort;

import java.util.EnumSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

import static java.util.stream.Collectors.joining;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PSortFilterType {

    Cytoplasmic("Cytoplasmic"),
    CytoplasmicMembrane("CytoPlasmic"),
    Extracellular("Extracellular"),
    Unknown("Unknown");

    private final String regex;

    static String compile(final EnumSet<PSortFilterType> filter) {
        val or = filter.stream().map(p -> p.getRegex()).collect(joining("|"));
        return ".*(" + or + ").*";
    }

    String getRegex() {
        return regex;
    }

    public EnumSet<PSortFilterType> single() {
        return EnumSet.of(this);
    }

    public EnumSet<PSortFilterType> or(final PSortFilterType that) {
        return EnumSet.of(this, that);
    }

}
