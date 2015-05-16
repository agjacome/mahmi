package es.uvigo.ei.sing.mahmi.common.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.Objects.deepEquals;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

public final class Tuple<A, B> {

    public final A left;
    public final B right;

    public Tuple(final A left, final B right) {
        requireNonNull(left, "Left value cannot be null");
        requireNonNull(right, "Right value cannot be null");

        this.left  = left;
        this.right = right;
    }

    public static <A, B> Tuple<A, B> of(final A left, final B right) {
        return new Tuple<>(left, right);
    }

    public static <A, B> Tuple<A, B> fromEntry(final Entry<A, B> entry) {
        return new Tuple<>(entry.getKey(), entry.getValue());
    }

    public A getLeft() {
        return left;
    }

    public B getRight() {
        return right;
    }

    public Tuple<B, A> swap() {
        return new Tuple<>(right, left);
    }

    public <C, D> Tuple<C, D> map(
        final Function<? super A, ? extends C> leftMapper,
        final Function<? super B, ? extends D> rightMapper
    ) {
        return new Tuple<>(leftMapper.apply(left), rightMapper.apply(right));
    }

    public Entry<A, B> toEntry() {
        return new Map.Entry<A, B>() {

            private final A key   = left;
            private       B value = right;

            @Override
            public A getKey() {
                return key;
            }

            @Override
            public B getValue() {
                return value;
            }

            @Override
            public B setValue(final B value) {
                final B tmp = this.value;
                this.value = value;
                return tmp;
            }

        };
    }

    @Override
    public int hashCode() {
        return 41 * (41 + left.hashCode()) + right.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Tuple)) return false;

        final Tuple<?, ?> that = (Tuple<?, ?>) other;
        return deepEquals(this.left,  that.left)
            && deepEquals(this.right, that.right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + ", " + right.toString() + ")";
    }

}
