package es.uvigo.ei.sing.mahmi.funpep.util;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import scala.Function0;
import scala.Function1;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;
import scalaz.IList;
import scalaz.Liskov;
import scalaz.Order;

import static scala.collection.JavaConversions.mapAsScalaMap;

public final class JavaToScala {

    // Disallow construction
    private JavaToScala() { }

    public static <A> Function0<A> asScala(final Supplier<A> f) {
        return new AbstractFunction0<A>() {
            @Override public A apply() { return f.get(); }
        };
    }

    public static <A, B> Function1<A, B> asScala(final Function<A, B> f) {
        return new AbstractFunction1<A, B>() {
            @Override public B apply(final A a) { return f.apply(a); }
        };
    }

    public static <A> Order<A> asScalaz(final Comparator<A> cmp) {
        return scalaz.Order$.MODULE$.fromScalaOrdering(
            scala.math.Ordering$.MODULE$.comparatorToOrdering(cmp)
        );
    }

    public static <A, B> scalaz.$eq$eq$greater$greater<A, B> asScalaz(
        final Map<A, B> map, final Order<A> ord
    ) {
        final scala.collection.mutable.Map<A, B> sMap = mapAsScalaMap(map);
        return IList.fromList(sMap.toList()).toMap(Liskov.isa(), ord);
    }

    public static <A, B> scalaz.$eq$eq$greater$greater<A, B> asScalaz(
        final Map<A, B> map, final Comparator<A> cmp
    ) {
        return asScalaz(map, asScalaz(cmp));
    }

    public static <A extends Comparable<A>, B> scalaz.$eq$eq$greater$greater<A, B> asScalaz(
        final Map<A, B> map
    ) {
        return asScalaz(map, (a1, a2) -> a1.compareTo(a2));
    }

}
