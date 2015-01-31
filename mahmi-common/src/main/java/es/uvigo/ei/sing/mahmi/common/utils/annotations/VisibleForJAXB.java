package es.uvigo.ei.sing.mahmi.common.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation-only annotation. Denotes that an existing empty constructor
 * exists due to the requirements imposed by JAXB, and therefore should not
 * be removed nor used by any client code but JAXB.
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
public @interface VisibleForJAXB { }
