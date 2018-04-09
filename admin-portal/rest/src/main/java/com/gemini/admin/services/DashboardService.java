package com.gemini.admin.services;

import com.gemini.admin.beans.AdminUser;
import com.gemini.admin.beans.CriteriaForm;
import com.gemini.admin.beans.EnrollmentSummary;
import com.gemini.admin.beans.ProgressSummary;
import com.gemini.admin.database.dao.DashboardDao;
import com.gemini.admin.database.dao.beans.EnrollmentProgress;
import com.gemini.commons.database.beans.City;
import com.gemini.commons.database.beans.Region;
import com.gemini.commons.database.beans.School;
import com.gemini.commons.utils.CopyUtils;
import com.gemini.commons.utils.DateUtils;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/9/18
 * Time: 10:29 AM
 */
@Service
public class DashboardService {
    @Autowired
    private DashboardDao dashboardDao;

    public List<Region> getRegions(AdminUser user) {
        return dashboardDao.getRegions(user);
    }

    public List<City> getCities(Long regionId, AdminUser user) {
        return dashboardDao.getCities(regionId, user);
    }

    public List<School> getSchools(Long regionId, String cityCode, AdminUser user) {
        return dashboardDao.getSchools(regionId, cityCode, user);
    }

    public EnrollmentSummary retrieveSummary(AdminUser user, CriteriaForm criteria) {
        EnrollmentSummary summary = new EnrollmentSummary();
        summary.setTotalPreEnrollments(dashboardDao.getPreEnrollmentBySIETotal(user, criteria));
        summary.setTotalConfirmed(dashboardDao.getConfirmedTotal(user, criteria));
        summary.setTotalDenied(dashboardDao.getDeniedTotal(user, criteria));
        summary.setTotalNewEntryEnrollments(dashboardDao.getNewEntryTotal(user, criteria));
        List<ProgressSummary> progressSummary = FluentIterable
                .from(dashboardDao.getProgress(user, criteria))
                .transform(new Function<EnrollmentProgress, ProgressSummary>() {
                    @Override
                    public ProgressSummary apply(EnrollmentProgress enrollmentProgress) {
                        ProgressSummary progress = CopyUtils.convert(enrollmentProgress, ProgressSummary.class);
                        progress.setDate(DateUtils.formatDate(enrollmentProgress.getDate()));
                        return progress;
                    }
                })
                .toList();
        summary.setProgress(progressSummary);
        return summary;
    }
}