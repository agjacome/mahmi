package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.generator.ValuesOf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;

@RunWith(Theories.class)
public final class NucleobaseTest {

    /** ∀ Nucleobase nb: Nucleobase.fromCode(nb.code) = Option.some(nb) */
    @Theory public void from_code_returns_the_correct_nucleobase_for_each_valid_code(
        @ForAll @ValuesOf final Nucleobase nb
    ) {
        assertThat(Nucleobase.fromCode(nb.getCode()).isSome(), is(true));
        assertThat(Nucleobase.fromCode(nb.getCode()).some(),   is(nb));
    }

    /** ∀ char c | c ∉ Nucleobase.codes: Nucleobase.fromCode(c) = Option.none() */
    @Theory public void from_code_returns_an_empty_option_for_each_invalid_code(
        @ForAll final char invalidCode
    ) {
        assumeFalse(Nucleobase.codes.contains(invalidCode));
        assertThat(Nucleobase.fromCode(invalidCode).isNone(), is(true));
    }

}
