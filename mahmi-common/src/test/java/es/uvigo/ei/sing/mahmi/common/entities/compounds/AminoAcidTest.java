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
public final class AminoAcidTest {

    /** ∀ AminoAcid aa: AminoAcid.fromCode(aa.code) = Option.some(aa) */
    @Theory public void from_code_returns_the_correct_aminoacid_for_each_valid_code(
        @ForAll @ValuesOf final AminoAcid aa
    ) {
        assertThat(AminoAcid.fromCode(aa.getCode()).isSome(), is(true));
        assertThat(AminoAcid.fromCode(aa.getCode()).some(),   is(aa));
    }

    /** ∀ char c | c ∉ AminoAcid.codes: AminoAcid.fromCode(c) = Option.none() */
    @Theory public void from_code_returns_an_empty_option_for_each_invalid_code(
        @ForAll final char invalidCode
    ) {
        assumeFalse(AminoAcid.codes.contains(invalidCode));
        assertThat(AminoAcid.fromCode(invalidCode).isNone(), is(true));
    }

}
