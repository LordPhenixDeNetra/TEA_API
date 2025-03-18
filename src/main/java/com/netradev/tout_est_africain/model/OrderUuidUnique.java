package com.netradev.tout_est_africain.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.netradev.tout_est_africain.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the uuid value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = OrderUuidUnique.OrderUuidUniqueValidator.class
)
public @interface OrderUuidUnique {

    String message() default "{Exists.order.uuid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class OrderUuidUniqueValidator implements ConstraintValidator<OrderUuidUnique, String> {

        private final OrderService orderService;
        private final HttpServletRequest request;

        public OrderUuidUniqueValidator(final OrderService orderService,
                final HttpServletRequest request) {
            this.orderService = orderService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(orderService.get(Long.parseLong(currentId)).getUuid())) {
                // value hasn't changed
                return true;
            }
            return !orderService.uuidExists(value);
        }

    }

}
