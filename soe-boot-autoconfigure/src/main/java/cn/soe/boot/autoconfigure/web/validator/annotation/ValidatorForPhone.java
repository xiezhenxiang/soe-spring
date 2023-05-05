package cn.soe.boot.autoconfigure.web.validator.annotation;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author xiezhenxiang 2023/5/5
 */
public class ValidatorForPhone implements ConstraintValidator<Phone, String> {
    private final Pattern pattern = Pattern.compile("^\\d{11}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!StringUtils.hasText(value)){
            return true;
        }
        return pattern.matcher(value).find();
    }
}

