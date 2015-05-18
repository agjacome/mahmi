package es.uvigo.ei.sing.mahmi.common.utils.exceptions;

import java.util.function.Supplier;

public final class ExceptionWrapper extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Class<? extends Throwable> wrappedType;
    private final Throwable                  wrappedThrowable;

    private ExceptionWrapper(
        final Class<? extends Throwable> wrappedType,
        final Throwable wrappedThrowable
     ) {
        super(wrappedThrowable);

        this.wrappedType      = wrappedType;
        this.wrappedThrowable = wrappedThrowable;
    }

    public static <A extends Throwable> ExceptionWrapper ExceptionWrapper(
        final A throwable
    ) {
        return new ExceptionWrapper(throwable.getClass(), throwable);
    }

    public static <A extends Throwable> Supplier<ExceptionWrapper> ExceptionSupplier(
        final A throwable
    ){
        return () -> ExceptionWrapper(throwable);
    }

    public Class<? extends Throwable> getWrappedType() {
        return wrappedType;
    }

    public Throwable getWrappedThrowable() {
        return wrappedThrowable;
    }

    public <A extends Throwable> A unwrap(
        final Class<A> type
    ) throws ExceptionWrapper {
        if (wrappedType == type)
            return type.cast(wrappedThrowable);
        else throw this;
    }

}