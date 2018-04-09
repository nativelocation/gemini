package com.gemini.admin.database;

import com.gemini.admin.beans.AdminUser;
import com.gemini.admin.beans.CriteriaForm;
import com.gemini.commons.utils.ValidationUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/9/18
 * Time: 9:23 AM
 */
final public class AdminAccessHelper {

    public static final int DE_CENTRAL_LEVEL = 0;
    public static final int REGION_LEVEL = 1;
    public static final int SCHOOL_LEVEL = 2;

    public static String addCriteria(AdminUser user, String column, Long id) {
        String criteria = addCriteria(user, null);
        if (ValidationUtils.valid(id)) {
            criteria = String.format(" AND %s = %s ", column, id);
        }
        return criteria;
    }

    public static String addCriteria(AdminUser user, String column, String code) {
        String criteria = addCriteria(user, null);
        if (ValidationUtils.valid(code)) {
            criteria = String.format(" AND %s = '%s' ", column, code);
        }
        return criteria;
    }

    public static String addCriteria(AdminUser user, AccessFrom from) {
        StringBuilder criteriaSql = new StringBuilder("");
        if (user.getPrecedence() == DE_CENTRAL_LEVEL)
            return criteriaSql.toString();

        if (user.getPrecedence() == REGION_LEVEL) {
            return constructCriteria(criteriaSql, "REGION_ID", user.getAllowedRegions());
        }

        //This should not happen
        if (user.getPrecedence() == SCHOOL_LEVEL && !AccessFrom.REGIONS.equals(from)) {
            return constructCriteria(criteriaSql, "SCHOOL_ID", user.getAllowedSchools());
        }
        return null;
    }

    public static String addCriteriaFromUserInput(AdminUser user, CriteriaForm userInput) {
        String criteria = addCriteria(user, null);

        if (ValidationUtils.valid(userInput.getRegionId())) {
            criteria = String.format(" AND %s = %s ", "REGION_ID", userInput.getRegionId());
        }

        if (ValidationUtils.valid(userInput.getCityCode())) {
            criteria = String.format(" AND %s = '%s' ", "CITY_CODE", userInput.getCityCode());
        }

        if (ValidationUtils.valid(userInput.getSchoolId())) {
            criteria = String.format(" AND %s = %s ", "SCHOOL_ID", userInput.getSchoolId());
        }
        return criteria;

    }

    private static String constructCriteria(StringBuilder criteriaSql, String column, List<Long> list) {
        int lastIndex = list.size() - 1;
        criteriaSql.append(String.format(" AND %s IN (", column));
        for (Long id : list) {
            criteriaSql.append(id);

            if (!(list.indexOf(id) == lastIndex))
                criteriaSql.append(",");

        }

        criteriaSql.append(" ) ");
        return criteriaSql.toString();
    }

    public static void main(String[] args) {
        System.out.println(String.format(" %d = %d", 1, 1));
    }
}