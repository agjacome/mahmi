package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import static fj.data.Validation.success;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import fj.data.List;
import fj.data.Validation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationExtensionMethods {

    public static <A, B> Validation<A, List<B>> sequenceV(final Stream<Validation<A, B>> vs) {
        // TODO: this probably does not work as expected
        return vs.reduce(
            success(List.nil()),
            (prev, curr) -> prev.bind(list -> curr.map(list::snoc)),
            (prev, curr) -> prev.bind(list -> curr)
        );
    }

}
