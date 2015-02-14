package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import fj.data.Seq;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeqExtensionMethods {

    public static String buildString(final Seq<Character> cs) {
        return cs.foldLeft(
            StringBuilder::append, new StringBuilder()
        ).toString();
    }

}
