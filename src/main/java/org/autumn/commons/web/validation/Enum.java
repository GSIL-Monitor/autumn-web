package org.autumn.commons.web.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.autumn.commons.Consts;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 校验枚举值<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Enum.Validator.class)
public @interface Enum {

    String message() default "{" + Consts.PROPERTIES_PREFIX + ".validation.Enum.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends java.lang.Enum<?>> cls() default Default.class;

    String[] allows() default {};

    class Validator implements ConstraintValidator<Enum, Object> {
        private Set<String> enums;
        private Set<String> allows;

        @Override
        public void initialize(Enum constraintAnnotation) {
            Class cls = constraintAnnotation.cls();
            if (!Default.class.equals(cls)) {
                this.enums = new HashSet<>();
                for (Object value : EnumSet.allOf(cls)) {
                    java.lang.Enum<?> e = (java.lang.Enum<?>) value;
                    this.enums.add(e.name().toLowerCase());
                }
            }
            String[] allows = constraintAnnotation.allows();
            if (null != allows && allows.length >= 1) {
                this.allows = new HashSet<>(Arrays.asList(allows));
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {//空值可以通过@NotNull等校验
                return true;
            }

            String str = value.toString();
            if (null != allows && allows.contains(str)) {
                return true;
            }

            if (null != enums && enums.contains(str.toLowerCase())) {
                return true;
            }
            return false;
        }
    }


    enum Default {}
}
