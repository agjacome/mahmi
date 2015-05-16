package es.uvigo.ei.sing.mahmi.common.utils.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentation-only annotation. Denotes that an empty constructor marked as
 * private was not a mistake but a conscious decision to prevent object
 * construction. Useful for utility classes.
 */
@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.SOURCE)
public @interface DisallowConstruction { }
