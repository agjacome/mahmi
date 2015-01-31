package es.uvigo.ei.sing.mahmi.common.utils;

import static es.uvigo.ei.sing.mahmi.common.utils.ArrayUtils.box;
import static es.uvigo.ei.sing.mahmi.common.utils.ArrayUtils.unbox;
import static fj.Equal.arrayEqual;
import static fj.Equal.byteEqual;
import static fj.Hash.arrayHash;
import static fj.Hash.byteHash;
import static fj.Show.showS;
import static fj.data.Array.array;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import fj.Equal;
import fj.Hash;
import fj.Show;
import fj.data.Array;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;

@Slf4j
@Value(staticConstructor = "fromByteArray")
public final class SHA1 {

    public static final Hash<SHA1>  hash  = arrayHash(byteHash).comap(SHA1::getDigest);
    public static final Equal<SHA1> equal = arrayEqual(byteEqual).comap(SHA1::getDigest);
    public static final Show<SHA1>  show  = showS(SHA1::asHexString);

    private final Array<Byte> digest;

    private SHA1(final Byte ... hash) {
        this.digest = array(hash);
    }

    private SHA1(final byte ... hash) {
        this.digest = array(box(hash));
    }

    public static SHA1 of(final String str) {
        return new SHA1(DigestUtils.sha1(str));
    }

    public static SHA1 of(final List<Character> chars) {
        return of(List.asString(chars));
    }

    public static SHA1 of(final Stream<Character> chars) {
        return of(Stream.asString(chars));
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
        final byte[ ] array = unbox(digest.array(Byte[ ].class));
        return Hex.encodeHexString(array);
    }

}
