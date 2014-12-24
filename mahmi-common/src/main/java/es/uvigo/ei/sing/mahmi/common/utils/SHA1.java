package es.uvigo.ei.sing.mahmi.common.utils;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.codec.digest.DigestUtils.sha1;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import fj.data.Option;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public final class SHA1 {

    private final byte[ ] hash;

    public static SHA1 of(final String str) {
        return new SHA1(sha1(str));
    }

    public static Option<SHA1> fromHexString(final String code) {
        try {
            return fromByteArray(Hex.decodeHex(code.toCharArray()));
        } catch (final DecoderException e) {
            return Option.none();
        }
    }

    public static Option<SHA1> fromByteArray(final byte[ ] bytes) {
        return Option.iif(bytes.length == 20, bytes).map(SHA1::new);
    }

    public String asHexString() {
        return encodeHexString(hash);
    }

}
