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
    public static <A> List<A> asMutableList(final A ... as) {
        final List<A> result = new ArrayList<>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static List<Boolean> asMutableList(final boolean ... bs) {
        return asMutableList(box(bs));
    }

    public static List<Byte> asMutableList(final byte ... bs) {
        return asMutableList(box(bs));
    }

    public static List<Character> asMutableList(final char ... cs) {
        return asMutableList(box(cs));
    }

    public static List<Short> asMutableList(final short ... ss) {
        return asMutableList(box(ss));
    }

    public static List<Integer> asMutableList(final int ... is) {
        return asMutableList(box(is));
    }

    public static List<Long> asMutableList(final long ... ls) {
        return asMutableList(box(ls));
    }

    public static List<Float> asMutableList(final float ... fs) {
        return asMutableList(box(fs));
    }

    public static List<Double> asMutableList(final double ... ds) {
        return asMutableList(box(ds));
    }

    @SafeVarargs
    public static <A> Set<A> asMutableSet(final A ... as) {
        final Set<A> result = new HashSet<>(as.length);
        Collections.addAll(result, as);
        return result;
    }

    public static Set<Boolean> asMutableSet(final boolean ... bs) {
        return asMutableSet(box(bs));
    }

    public static Set<Byte> asMutableSet(final byte ... bs) {
        return asMutableSet(box(bs));
    }

    public static Set<Character> asMutableSet(final char ... cs) {
        return asMutableSet(box(cs));
    }

    public static Set<Short> asMutableSet(final short ... ss) {
        return asMutableSet(box(ss));
    }

    public static Set<Integer> asMutableSet(final int ... is) {
        return asMutableSet(box(is));
    }

    public static Set<Long> asMutableSet(final long ... ls) {
        return asMutableSet(box(ls));
    }

    public static Set<Float> asMutableSet(final float ... fs) {
        return asMutableSet(box(fs));
    }

    public static Set<Double> asMutableSet(final double ... ds) {
        return asMutableSet(box(ds));
    }

    @SafeVarargs
    public static <A extends Comparable<? super A>> SortedSet<A> asMutableSortedSet(
        final A ... as
    ) {
        final SortedSet<A> result = new TreeSet<>();
        Collections.addAll(result, as);
        return result;
    }

    public static SortedSet<Boolean> asMutableSortedSet(final boolean ... bs) {
        return asMutableSortedSet(box(bs));
    }

    public static SortedSet<Byte> asMutableSortedSet(final byte ... bs) {
        return asMutableSortedSet(box(bs));
    }

    public static SortedSet<Character> asMutableSortedSet(final char ... cs) {
        return asMutableSortedSet(box(cs));
    }

    public static SortedSet<Short> asMutableSortedSet(final short ... ss) {
        return asMutableSortedSet(box(ss));
    }

    public static SortedSet<Integer> asMutableSortedSet(final int ... is) {
        return asMutableSortedSet(box(is));
    }

    public static SortedSet<Long> asMutableSortedSet(final long ... ls) {
        return asMutableSortedSet(box(ls));
    }

    public static SortedSet<Float> asMutableSortedSet(final float ... fs) {
        return asMutableSortedSet(box(fs));
    }

    public static SortedSet<Double> asMutableSortedSet(final double ... ds) {
        return asMutableSortedSet(box(ds));
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
