package com.gemini.utils;

import com.gemini.beans.IdentityForm;
import com.gemini.database.IdentityEntity;
import org.springframework.util.StringUtils;

import java.util.StringTokenizer;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;


/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 2/22/18
 * Time: 9:22 PM
 */
public final class Utils {

    public static String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

    public static String toLastName(String fatherLastName, String motherLastName) {
        fatherLastName = fatherLastName != null ? StringUtils.capitalize(fatherLastName.trim()) : "";
        motherLastName = motherLastName != null ? StringUtils.capitalize(motherLastName.trim()) : "";
        return String.format("%s %s", fatherLastName, motherLastName);
    }

    public static String toFullName(String... names) {
        return Stream.of(names)
                .filter(s -> s != null && !s.isEmpty())
                .collect(joining(" "));
    }

    public static void copyLastNames(IdentityEntity entity, IdentityForm form) {
        String token = StringUtils.hasText(entity.getLastName()) ? entity.getLastName().trim() : "";
        StringTokenizer tokenizer = new StringTokenizer(token, " ");
        String fatherLastName = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
        String motherLastName = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
        form.setFatherLastName(fatherLastName);
        form.setMotherLastName(motherLastName);
    }

}