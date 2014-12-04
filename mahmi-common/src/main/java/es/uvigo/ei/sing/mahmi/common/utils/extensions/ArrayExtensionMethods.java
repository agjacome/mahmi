package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayExtensionMethods {

    public static Byte[ ] boxedArray(final byte[ ] array) {
        val result = new Byte[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static Integer[ ] boxedArray(final int[ ] array) {
        val result = new Integer[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static byte[ ] primitiveArray(final Byte[] array) {
        val result = new byte[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

    public static int[ ] primitiveArray(final Integer[] array) {
        val result = new int[array.length];
        for (int i = 0; i < array.length; ++i)
            result[i] = array[i];

        return result;
    }

}
