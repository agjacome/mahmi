package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayExtensionMethods {

    public static Byte[ ] boxed(final byte[ ] array) {
        val result = new Byte[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static Integer[ ] boxed(final int[ ] array) {
        val result = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static byte[ ] primitive(final Byte[] array) {
        val result = new byte[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static int[ ] primitive(final Integer[] array) {
        val result = new int[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

}
