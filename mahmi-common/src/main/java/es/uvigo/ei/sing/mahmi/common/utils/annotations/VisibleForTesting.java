package es.uvigo.ei.sing.mahmi.common.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation-only annotation. Denotes that an existing method exists or is
 * non-private because some testing (e.g. JUnit) requirements, and should
 * therefore not be removed nor used outside testing code.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForTesting { }
