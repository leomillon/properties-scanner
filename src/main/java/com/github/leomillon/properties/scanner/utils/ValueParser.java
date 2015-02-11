package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.EvaluatedProperty;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.*;

public final class ValueParser {

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{(.+?)}");

    private ValueParser() {
        // Prevent instanciation
    }

    @Nonnull
    public static Register<EvaluatedProperty> evaluateProperties(@Nonnull Register<SimpleProperty> register) {
        requireNonNull(register);
        Register.Builder<EvaluatedProperty> evaluatedRegister = Register.builder();
        for (SimpleProperty simpleProperty : register) {
            String interpretedValue = findInterpretedValue(simpleProperty.getValue(), register);
            evaluatedRegister.addProperty(new EvaluatedProperty(simpleProperty, interpretedValue));
        }
        return evaluatedRegister.build();
    }

    private static String findInterpretedValue(String value, @Nonnull Register<SimpleProperty> register) {
        Set<String> foundPropKeys = extractKeysFromExpressions(value);
        if (foundPropKeys.isEmpty()) {
            return value;
        }
        String interpretedValue = value;
        for (String foundPropKey : foundPropKeys) {
            Optional<SimpleProperty> foundProperty = register.getPropertyForKey(foundPropKey);
            if (foundProperty.isPresent()) {
                String interpretedValueFound = findInterpretedValue(foundProperty.get().getValue(), register);
                interpretedValue = interpretedValue.replaceAll("\\$\\{" + foundPropKey + "}", interpretedValueFound);
            }
        }
        return interpretedValue;
    }

    @VisibleForTesting
    @Nonnull
    static Set<String> extractKeysFromExpressions(String content) {

        if (StringUtils.isBlank(content)) {
            return Collections.emptySet();
        }

        Matcher matcher = EXPRESSION_PATTERN.matcher(content);
        ImmutableSet.Builder<String> propKeysBuilder = ImmutableSet.builder();
        while (matcher.find()) {
            propKeysBuilder.add(matcher.group(1));
        }
        return propKeysBuilder.build();
    }
}
