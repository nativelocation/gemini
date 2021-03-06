package com.gemini.utils;

import com.gemini.beans.IdentityForm;
import com.gemini.database.IdentityEntity;
import com.google.common.base.Joiner;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.UUID;


/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 2/22/18
 * Time: 9:22 PM
 */
public final class Utils {

    public static String generateKey() {
        return UUID.randomUUID().toString();
    }

    public static String toLastName(String fatherLastName, String motherLastName) {
        fatherLastName = fatherLastName != null ? StringUtils.capitalize(fatherLastName.trim()) : "";
        motherLastName = motherLastName != null ? StringUtils.capitalize(motherLastName.trim()) : "";
        return String.format("%s %s", fatherLastName, motherLastName);
    }

    public static String toFullName(String... names) {
        return Joiner.on(" ")
                .skipNulls()
                .join(names);
    }

    public static void copyLastNames(IdentityEntity entity, IdentityForm form) {
        String lastName = StringUtils.hasText(entity.getLastName()) ? entity.getLastName() : "";
        form.setLastName(lastName);
    }

    public static String obfuscatedSsn(String ssn) {
        String last = "XXXX";
        int index = StringUtils.hasText(ssn) && ssn.length() >= 4
                ? ssn.length() - 4
                : -1;
        if (index >= 0)
            last = ssn.substring(index);
        return String.format("XXX-XX-%s", last);
    }

    public static String cleanSsn(String ssn) {
        return StringUtils.hasText(ssn) ? ssn.replaceAll("-", "") : ssn;
    }

    public static String canonString(String field){
        if (StringUtils.hasText(field)) {
            return field.replaceAll(" ", "").toUpperCase();
        }
        return field;
    }

    public static String removeAccents(String field) {
        if (StringUtils.hasText(field)) {
            String normalized = Normalizer.normalize(field, Normalizer.Form.NFD);
            normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
            return normalized.toUpperCase().trim();
        }
        return field;
    }

//    public static void main(String[] args) {
//        System.out.println(removeAccents("ÁÉÍÓÚáéíóúñÑ"));
//        System.out.println(canonString(""));
//
//    }

}