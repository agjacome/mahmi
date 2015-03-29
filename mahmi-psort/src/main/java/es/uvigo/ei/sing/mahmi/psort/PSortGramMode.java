package es.uvigo.ei.sing.mahmi.psort;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PSortGramMode {

    Positive("--positive"),
    Negative("--negative");

    private final String modeFlag;

}
