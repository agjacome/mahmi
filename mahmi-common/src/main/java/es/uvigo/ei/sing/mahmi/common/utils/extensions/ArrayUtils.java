package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

public final class ArrayUtils {

    @DisallowConstruction
    private ArrayUtils() { }

    public static Boolean[ ] box(final boolean ... xs) {
        final Boolean[ ] result = new Boolean[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Byte[ ] box(final byte ... xs) {
        final Byte[ ] result = new Byte[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Character[ ] box(final char ... xs) {
        final Character[ ] result = new Character[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Short[ ] box(final short ... xs) {
        final Short[ ] result = new Short[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Integer[ ] box(final int ... xs) {
        final Integer[ ] result = new Integer[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Long[ ] box(final long ... xs) {
        final Long[ ] result = new Long[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Float[ ] box(final float ... xs) {
        final Float[ ] result = new Float[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static Double[ ] box(final double ... xs) {
        final Double[ ] result = new Double[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static boolean[ ] unbox(final Boolean ... xs) {
        final boolean[ ] result = new boolean[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static byte[ ] unbox(final Byte ... xs) {
        final byte[ ] result = new byte[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static char[ ] unbox(final Character ... xs) {
        final char[ ] result = new char[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static short[ ] unbox(final Short ... xs) {
        final short[ ] result = new short[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static int[ ] unbox(final Integer ... xs) {
        final int[ ] result = new int[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static long[ ] unbox(final Long ... xs) {
        final long[ ] result = new long[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static float[ ] unbox(final Float ... xs) {
        final float[ ] result = new float[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    public static double[ ] box(final Double ... xs) {
        final double[ ] result = new double[xs.length];
        for (int i = 0; i < xs.length; i++)
            result[i] = xs[i];

        return result;
    }

    @SafeVarargs
    public static <A> List<A> asList(final A ... as) {
        final List<A> result = new ArrayList<>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static List<Boolean> asList(final boolean ... bs) {
        return asList(box(bs));
    }

    public static List<Byte> asList(final byte ... bs) {
        return asList(box(bs));
    }

    public static List<Character> asList(final char ... cs) {
        return asList(box(cs));
    }

    public static List<Short> asList(final short ... ss) {
        return asList(box(ss));
    }

    public static List<Integer> asList(final int ... is) {
        return asList(box(is));
    }

    public static List<Long> asList(final long ... ls) {
        return asList(box(ls));
    }

    public static List<Float> asList(final float ... fs) {
        return asList(box(fs));
    }

    public static List<Double> asList(final double ... ds) {
        return asList(box(ds));
    }

    @SafeVarargs
    public static <A> Set<A> asSet(final A ... as) {
        final Set<A> result = new HashSet<>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static Set<Boolean> asSet(final boolean ... bs) {
        return asSet(box(bs));
    }

    public static Set<Byte> asSet(final byte ... bs) {
        return asSet(box(bs));
    }

    public static Set<Character> asSet(final char ... cs) {
        return asSet(box(cs));
    }

    public static Set<Short> asSet(final short ... ss) {
        return asSet(box(ss));
    }

    public static Set<Integer> asSet(final int ... is) {
        return asSet(box(is));
    }

    public static Set<Long> asSet(final long ... ls) {
        return asSet(box(ls));
    }

    public static Set<Float> asSet(final float ... fs) {
        return asSet(box(fs));
    }

    public static Set<Double> asSet(final double ... ds) {
        return asSet(box(ds));
    }

    @SafeVarargs
    public static <A extends Comparable<? super A>> SortedSet<A> asSortedSet(
        final A ... as
    ) {
        final SortedSet<A> result = new TreeSet<>();
        Collections.addAll(result, as);
        return result;
    }

    public static SortedSet<Boolean> asSortedSet(final boolean ... bs) {
        return asSortedSet(box(bs));
    }

    public static SortedSet<Byte> asSortedSet(final byte ... bs) {
        return asSortedSet(box(bs));
    }

    public static SortedSet<Character> asSortedSet(final char ... cs) {
        return asSortedSet(box(cs));
    }

    public static SortedSet<Short> asSortedSet(final short ... ss) {
        return asSortedSet(box(ss));
    }

    public static SortedSet<Integer> asSortedSet(final int ... is) {
        return asSortedSet(box(is));
    }

    public static SortedSet<Long> asSortedSet(final long ... ls) {
        return asSortedSet(box(ls));
    }

    public static SortedSet<Float> asSortedSet(final float ... fs) {
        return asSortedSet(box(fs));
    }

    public static SortedSet<Double> asSortedSet(final double ... ds) {
        return asSortedSet(box(ds));
    }

    @SafeVarargs
    public static <A> void reverse(final A ... as) {
        for (int i = 0, j = as.length - 1; i < j; i++, j--) {
            final A tmp = as[i];
            as[i] = as[j];
            as[j] = tmp;
        }
    }

    public static void reverse(final boolean ... bs) {
        for (int i = 0, j = bs.length - 1; i < j; i++, j--) {
            final boolean tmp = bs[i];
            bs[i] = bs[j];
            bs[j] = tmp;
        }
    }

    public static void reverse(final char ... cs) {
        for (int i = 0, j = cs.length - 1; i < j; i++, j--) {
            final char tmp = cs[i];
            cs[i] = cs[j];
            cs[j] = tmp;
        }
    }

    public static void reverse(final byte ... bs) {
        for (int i = 0, j = bs.length - 1; i < j; i++, j--) {
            final byte tmp = bs[i];
            bs[i] = bs[j];
            bs[j] = tmp;
        }
    }

    public static void reverse(final short ... ss) {
        for (int i = 0, j = ss.length - 1; i < j; i++, j--) {
            final short tmp = ss[i];
            ss[i] = ss[j];
            ss[j] = tmp;
        }
    }

    public static void reverse(final int ... is) {
        for (int i = 0, j = is.length - 1; i < j; i++, j--) {
            final int tmp = is[i];
            is[i] = is[j];
            is[j] = tmp;
        }
    }

    public static void reverse(final long ... ls) {
        for (int i = 0, j = ls.length - 1; i < j; i++, j--) {
            final long tmp = ls[i];
            ls[i] = ls[j];
            ls[j] = tmp;
        }
    }

    public static void reverse(final float ... fs) {
        for (int i = 0, j = fs.length - 1; i < j; i++, j--) {
            final float tmp = fs[i];
            fs[i] = fs[j];
            fs[j] = tmp;
        }
    }

    public static void reverse(final double ... ds) {
        for (int i = 0, j = ds.length - 1; i < j; i++, j--) {
            final double tmp = ds[i];
            ds[i] = ds[j];
            ds[j] = tmp;
        }
    }

}
