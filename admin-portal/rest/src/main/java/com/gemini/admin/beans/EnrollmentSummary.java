package com.gemini.admin.beans;

import com.gemini.admin.database.dao.beans.EnrollmentProgress;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/9/18
 * Time: 10:19 AM
 */
public class EnrollmentSummary {

    private int totalPreEnrollments;
    private int totalNewEntryEnrollments;
    private int totalConfirmed;
    private int totalDenied;
    private List<ProgressSummary> progress;


    public int getTotalPreEnrollments() {
        return totalPreEnrollments;
    }

    public void setTotalPreEnrollments(int totalPreEnrollments) {
        this.totalPreEnrollments = totalPreEnrollments;
    }

    public int getTotalNewEntryEnrollments() {
        return totalNewEntryEnrollments;
    }

    public void setTotalNewEntryEnrollments(int totalNewEntryEnrollments) {
        this.totalNewEntryEnrollments = totalNewEntryEnrollments;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public int getTotalDenied() {
        return totalDenied;
    }

    public void setTotalDenied(int totalDenied) {
        this.totalDenied = totalDenied;
    }

    public List<ProgressSummary> getProgress() {
        return progress;
    }

    public void setProgress(List<ProgressSummary> progress) {
        this.progress = progress;
    }
}