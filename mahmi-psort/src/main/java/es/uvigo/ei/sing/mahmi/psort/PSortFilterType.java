package es.uvigo.ei.sing.mahmi.psort;

import java.util.EnumSet;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PSortFilterType {

    Cytoplasmic("Cytoplasmic"),
    CytoplasmicMembrane("CytoPlasmic"),
    Extracellular("Extracellular"),
    Unknown("Unknown");

    private final String regex;

    static String compile(final EnumSet<PSortFilterType> filter) {
        return filter.stream()
              .map(PSortFilterType::getRegex)
              .collect(Collectors.joining("|"));
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
