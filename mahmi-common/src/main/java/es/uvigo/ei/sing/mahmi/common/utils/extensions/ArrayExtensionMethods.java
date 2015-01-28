package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArrayExtensionMethods {

    public static Boolean[ ] box(final boolean ... xs) {
        val result = new Boolean[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Byte[ ] box(final byte ... xs) {
        val result = new Byte[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Character[ ] box(final char ... xs) {
        val result = new Character[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Short[ ] box(final short ... xs) {
        val result = new Short[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Integer[ ] box(final int ... xs) {
        val result = new Integer[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Long[ ] box(final long ... xs) {
        val result = new Long[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Float[ ] box(final float ... xs) {
        val result = new Float[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Double[ ] box(final double ... xs) {
        val result = new Double[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static boolean[ ] unbox(final Boolean ... xs) {
        val result = new boolean[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static byte[ ] unbox(final Byte ... xs) {
        val result = new byte[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static char[ ] unbox(final Character ... xs) {
        val result = new char[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static short[ ] unbox(final Short ... xs) {
        val result = new short[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static int[ ] unbox(final Integer ... xs) {
        val result = new int[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static long[ ] unbox(final Long ... xs) {
        val result = new long[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static float[ ] unbox(final Float ... xs) {
        val result = new float[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static double[ ] box(final Double ... xs) {
        val result = new double[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    @SafeVarargs
    public static <A> List<A> asModifiableList(final A ... as) {
        val result = new ArrayList<A>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static List<Boolean> asList(final boolean[ ] bs) {
        return asModifiableList(box(bs));
    }

    public static List<Byte> asList(final byte[ ] bs) {
        return asModifiableList(box(bs));
    }

    public static List<Character> asList(final char[ ] cs) {
        return asModifiableList(box(cs));
    }

    public static List<Short> asList(final short[ ] ss) {
        return asModifiableList(box(ss));
    }

    public static List<Integer> asList(final int[ ] is) {
        return asModifiableList(box(is));
    }

    public static List<Long> asList(final long[ ] ls) {
        return asModifiableList(box(ls));
    }

    public static List<Float> asList(final float[ ] fs) {
        return asModifiableList(box(fs));
    }

    public static List<Double> asList(final double[ ] ds) {
        return asModifiableList(box(ds));
    }

    @SafeVarargs
    public static <A> Set<A> asSet(final A ... as) {
        val result = new HashSet<A>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static Set<Boolean> asSet(final boolean[ ] bs) {
        return asSet(box(bs));
    }

    public static Set<Byte> asSet(final byte[ ] bs) {
        return asSet(box(bs));
    }

    public static Set<Character> asSet(final char[ ] cs) {
        return asSet(box(cs));
    }

    public static Set<Short> asSet(final short[ ] ss) {
        return asSet(box(ss));
    }

    public static Set<Integer> asSet(final int[ ] is) {
        return asSet(box(is));
    }

    public static Set<Long> asSet(final long[ ] ls) {
        return asSet(box(ls));
    }

    public static Set<Float> asSet(final float[ ] fs) {
        return asSet(box(fs));
    }

    public static Set<Double> asSet(final double[ ] ds) {
        return asSet(box(ds));
    }

    @SafeVarargs
    public static <A extends Comparable<? super A>> SortedSet<A> asSortedSet(
        final A ... as
    ) {
        val result = new TreeSet<A>();
        Collections.addAll(result, as);
        return result;
    }

    public static SortedSet<Boolean> asSortedSet(final boolean[ ] bs) {
        return asSortedSet(box(bs));
    }

    public static SortedSet<Byte> asSortedSet(final byte[ ] bs) {
        return asSortedSet(box(bs));
    }

    public static SortedSet<Character> asSortedSet(final char[ ] cs) {
        return asSortedSet(box(cs));
    }

    public static SortedSet<Short> asSortedSet(final short[ ] ss) {
        return asSortedSet(box(ss));
    }

    public static SortedSet<Integer> asSortedSet(final int[ ] is) {
        return asSortedSet(box(is));
    }

    public static SortedSet<Long> asSortedSet(final long[ ] ls) {
        return asSortedSet(box(ls));
    }

    public static SortedSet<Float> asSortedSet(final float[ ] fs) {
        return asSortedSet(box(fs));
    }

    public static SortedSet<Double> asSortedSet(final double[ ] ds) {
        return asSortedSet(box(ds));
    }

    public static <A> void reverse(final A[ ] as) {
        for (int i = 0, j = as.length - 1; i < j; i++, j--) {
            val tmp = as[i];
            as[i] = as[j];
            as[j] = tmp;
        }
    }

    public static void reverse(final boolean[ ] bs) {
        for (int i = 0, j = bs.length - 1; i < j; i++, j--) {
            val tmp = bs[i];
            bs[i] = bs[j];
            bs[j] = tmp;
        }
    }

    public static void reverse(final char[ ] cs) {
        for (int i = 0, j = cs.length - 1; i < j; i++, j--) {
            val tmp = cs[i];
            cs[i] = cs[j];
            cs[j] = tmp;
        }
    }

    public static void reverse(final byte[ ] bs) {
        for (int i = 0, j = bs.length - 1; i < j; i++, j--) {
            val tmp = bs[i];
            bs[i] = bs[j];
            bs[j] = tmp;
        }
    }

    public static void reverse(final short[ ] ss) {
        for (int i = 0, j = ss.length - 1; i < j; i++, j--) {
            val tmp = ss[i];
            ss[i] = ss[j];
            ss[j] = tmp;
        }
    }

    public static void reverse(final int[ ] is) {
        for (int i = 0, j = is.length - 1; i < j; i++, j--) {
            val tmp = is[i];
            is[i] = is[j];
            is[j] = tmp;
        }
    }

    public static void reverse(final long[ ] ls) {
        for (int i = 0, j = ls.length - 1; i < j; i++, j--) {
            val tmp = ls[i];
            ls[i] = ls[j];
            ls[j] = tmp;
        }
    }

    public static void reverse(final float[ ] fs) {
        for (int i = 0, j = fs.length - 1; i < j; i++, j--) {
            val tmp = fs[i];
            fs[i] = fs[j];
            fs[j] = tmp;
        }
    }

    public static void reverse(final double[ ] ds) {
        for (int i = 0, j = ds.length - 1; i < j; i++, j--) {
            val tmp = ds[i];
            ds[i] = ds[j];
            ds[j] = tmp;
        }
    }

    @SafeVarargs
    public static <A> Iterable<A> reverseIterable(final A ... as) {
        return () -> new Iterator<A>() {
            private int index = as.length - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public A next() {
                if (hasNext())
                    return as[index--];
                else
                    throw new NoSuchElementException();
            }
        };
    }

}
