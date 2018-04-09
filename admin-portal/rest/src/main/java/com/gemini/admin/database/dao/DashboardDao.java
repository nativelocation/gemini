package com.gemini.admin.database.dao;

import com.gemini.admin.beans.AdminUser;
import com.gemini.admin.beans.CriteriaForm;
import com.gemini.admin.database.AccessFrom;
import com.gemini.admin.database.AdminAccessHelper;
import com.gemini.admin.database.dao.beans.EnrollmentProgress;
import com.gemini.commons.database.beans.City;
import com.gemini.commons.database.beans.Region;
import com.gemini.commons.database.beans.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 4:21 PM
 */
@Repository
public class DashboardDao extends NamedParameterJdbcDaoSupport {

    private final String REGION_SQL = "SELECT * FROM VW_REGIONS ";
    private final String CITIES_SQL = "SELECT CITY_CD, CITY FROM VW_SCHOOLS S ";
    private final String SCHOOL_SQL = "SELECT * FROM VW_SCHOOLS S ";

    private final String PRE_ENROLLMENT_BY_SIE = "SELECT COUNT(*) FROM ST_ENROLLMENT WHERE SCHOOL_YEAR = 2019 ";
    private final String CONFIRMED_ENROLLMENT = "SELECT COUNT(*) FROM VW_DASH_PRE_ENROLLMENTS P WHERE TYPE IN ('REGULAR' ,'SPECIALIZED_ALTERNATE_SCHOOLS', 'OCCUPATIONAL') AND ENTRY_TYPE = 'EXISTING' ";
    private final String NEW_ENTRY_ENROLLMENT = "SELECT COUNT(*) FROM VW_DASH_PRE_ENROLLMENTS P WHERE TYPE IN ('REGULAR_ALTERNATE_SCHOOLS', 'SPECIALIZED_ALTERNATE_SCHOOLS', 'OCCUPATIONAL') AND ENTRY_TYPE = 'NEW'";
    private final String DENIED_ENROLLMENT = "SELECT COUNT(*) FROM VW_DASH_PRE_ENROLLMENTS P WHERE TYPE IN ('REGULAR_ALTERNATE_SCHOOLS') AND ENTRY_TYPE = 'EXISTING'";
    private final String ENROLLMENT_SUMMARY = "SELECT " +
            "ACTION_DATE, " +
            "SUM(CONFIRMADOS) AS TOTAL_CONFIRMED, " +
            "SUM(NUEVO_INGRESO) AS TOTAL_NEW_ENTRY_ENROLLMENTS, " +
            "SUM(RECHAZADOS) AS TOTAL_DENIED " +
            "FROM VW_ENROLLMENT_PROGRESS ";
    private final String ENROLLMENT_PROGRESS = "SELECT * FROM VW_ENROLLMENT_PROGRESS ";

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void init() {
        setDataSource(dataSource);
    }

    //  regions, municipios & schools
    public List<Region> getRegions(AdminUser user) {
        String query = REGION_SQL.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteria(user, AccessFrom.REGIONS));
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper<Region>());
    }

    public List<City> getCities(Long regionId, AdminUser user) {
        String query = CITIES_SQL.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteria(user, "REGION_ID", regionId));
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper<City>(), regionId);
    }

    public List<School> getSchools(Long regionId, String cityCode, AdminUser user) {
        String query = SCHOOL_SQL.concat(" WHERE 1=1 ");
        query = query
                .concat(AdminAccessHelper.addCriteria(user, "REGION_ID", regionId))
                .concat(AdminAccessHelper.addCriteria(user, "CITY_CD", cityCode));
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper<School>(), cityCode);
    }

    public int getPreEnrollmentBySIETotal(AdminUser user, CriteriaForm criteria) {
        String query = PRE_ENROLLMENT_BY_SIE.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteriaFromUserInput(user, criteria));
        return getJdbcTemplate().queryForObject(query, new SingleColumnRowMapper<>(Integer.class));
    }

    public int getConfirmedTotal(AdminUser user, CriteriaForm criteria) {
        String query = CONFIRMED_ENROLLMENT.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteriaFromUserInput(user, criteria));
        return getJdbcTemplate().queryForObject(query, new SingleColumnRowMapper<>(Integer.class));
    }

    public int getNewEntryTotal(AdminUser user, CriteriaForm criteria) {
        String query = NEW_ENTRY_ENROLLMENT.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteriaFromUserInput(user, criteria));
        return getJdbcTemplate().queryForObject(query, new SingleColumnRowMapper<>(Integer.class));
    }

    public int getDeniedTotal(AdminUser user, CriteriaForm criteria) {
        String query = DENIED_ENROLLMENT.concat(" WHERE 1=1 ");
        query = query.concat(AdminAccessHelper.addCriteriaFromUserInput(user, criteria));
        return getJdbcTemplate().queryForObject(query, new SingleColumnRowMapper<>(Integer.class));
    }

    public List<EnrollmentProgress> getProgress(AdminUser user, CriteriaForm criteria) {
        String query = ENROLLMENT_SUMMARY.concat(" WHERE 1=1 ");
        query = query
                .concat(AdminAccessHelper.addCriteriaFromUserInput(user, criteria))
                .concat(" GROUP BY ACTION_DATE");
        return getJdbcTemplate().query(query, new BeanPropertyRowMapper<EnrollmentProgress>());
    }


}
