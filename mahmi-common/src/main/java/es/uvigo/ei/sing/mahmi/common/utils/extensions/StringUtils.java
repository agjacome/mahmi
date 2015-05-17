package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

import static java.lang.Math.ceil;
import static java.lang.Math.min;

public final class StringUtils {

    @DisallowConstruction
    private StringUtils() { }

    public static List<String> splitFixedLength(final String str, final int length) {
        final int numChunks = (int) ceil(str.length() / length);
        final List<String> chunks = new ArrayList<>(numChunks);

        for (int start = 0; start < str.length(); start += length) {
            final int stop = min(str.length(), start + length);
            chunks.add(str.substring(start, stop));
        }

        return chunks;
    }

}
