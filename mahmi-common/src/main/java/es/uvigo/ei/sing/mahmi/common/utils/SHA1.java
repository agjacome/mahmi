package es.uvigo.ei.sing.mahmi.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.data.Array;
import fj.data.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.utils.extensions.ArrayExtensionMethods;

import static fj.Equal.stringEqual;
import static fj.Hash.stringHash;
import static fj.Ord.stringOrd;
import static fj.data.Array.array;

@Slf4j
@Getter
@AllArgsConstructor(staticName = "fromByteArray")
@ExtensionMethod(ArrayExtensionMethods.class)
public final class SHA1 {

    public static final Hash<SHA1>  hash  = stringHash.contramap(SHA1::asHexString);
    public static final Equal<SHA1> equal = stringEqual.contramap(SHA1::asHexString);
    public static final Ord<SHA1>   ord   = stringOrd.contramap(SHA1::asHexString);

    private final Array<Byte> digest;

    private SHA1(final Byte ... hash) {
        this.digest = array(hash);
    }

    private SHA1(final byte ... hash) {
        this.digest = array(hash.box());
    }

    public static SHA1 of(final String str) {
        return new SHA1(DigestUtils.sha1(str));
    }

    public static Option<SHA1> fromByteArray(final byte ... hash) {
        return Option.iif(hash.length == 20, hash).map(SHA1::new);
    }

    public static Option<SHA1> fromByteArray(final Byte ... hash) {
        return Option.iif(hash.length == 20, hash).map(SHA1::new);
    }

    public static Option<SHA1> fromHexString(final String code) {
        try {
            return fromByteArray(Hex.decodeHex(code.toCharArray()));
        } catch (final DecoderException de) {
            log.error("Could not parse SHA1 hash", de);
            return Option.none();
        }
    }

    public String asHexString() {
        final byte[ ] array = digest.array(Byte[ ].class).unbox();
        return Hex.encodeHexString(array);
    }

}
