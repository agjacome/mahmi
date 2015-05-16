package es.uvigo.ei.sing.mahmi.common.utils;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import es.uvigo.ei.sing.mahmi.common.utils.extensions.ArrayUtils;

public final class SHA1 {

    private final byte[ ] digest;

    private SHA1(final byte ... digest) {
        this.digest = digest;
    }

    public static SHA1 of(final String str) {
        return new SHA1(DigestUtils.sha1(str));
    }

    public static Optional<SHA1> fromByteArray(final byte ... digest) {
        return digest.length == 20
             ? Optional.of(digest).map(SHA1::new)
             : Optional.empty();
    }

    public static Optional<SHA1> fromByteArray(final Byte ... digest) {
        return digest.length == 20
             ? Optional.of(digest).map(ArrayUtils::unbox).map(SHA1::new)
             : Optional.empty();
    }

    public static Optional<SHA1> fromHexString(final String hexString) {
        try {
            return SHA1.fromByteArray(Hex.decodeHex(hexString.toCharArray()));
        } catch (final DecoderException de) {
            return Optional.empty();
        }
    }

    public String asHexString() {
        return Hex.encodeHexString(digest);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digest);
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof SHA1
            && Arrays.equals(this.digest, ((SHA1) that).digest);
    }

}
