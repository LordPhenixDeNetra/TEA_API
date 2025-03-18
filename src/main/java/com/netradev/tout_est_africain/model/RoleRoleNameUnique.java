package com.netradev.tout_est_africain.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.netradev.tout_est_africain.service.RoleService;
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
 * Validate that the roleName value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = RoleRoleNameUnique.RoleRoleNameUniqueValidator.class
)
public @interface RoleRoleNameUnique {

    String message() default "{Exists.role.roleName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class RoleRoleNameUniqueValidator implements ConstraintValidator<RoleRoleNameUnique, RoleType> {

        private final RoleService roleService;
        private final HttpServletRequest request;

        public RoleRoleNameUniqueValidator(final RoleService roleService,
                final HttpServletRequest request) {
            this.roleService = roleService;
            this.request = request;
        }

        @Override
        public boolean isValid(final RoleType value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(roleService.get(Long.parseLong(currentId)).getRoleName())) {
                // value hasn't changed
                return true;
            }
            return !roleService.roleNameExists(value);
        }

    }

}
