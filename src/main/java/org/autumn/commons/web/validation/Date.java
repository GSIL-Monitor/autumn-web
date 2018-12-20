package org.autumn.commons.web.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 校验日期值<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {Date.Validator.class})
public @interface Date {

    String message() default "{" + Consts.PROPERTIES_PREFIX + ".validation.Date.message}";

    String format() default "yyyyMMdd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Date, String> {

        private DateFormat format;

        @Override
        public void initialize(Date constraintAnnotation) {
            this.format = new SimpleDateFormat(constraintAnnotation.format());
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (null != value) {
                try {
                    this.format.parse(value);
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
    }
}
