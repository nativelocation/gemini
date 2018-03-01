package com.gemini.beans.forms;

import com.gemini.beans.types.RequestStatus;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 2/21/18
 * Time: 11:58 AM
 */
public class PreEnrollmentBean {

    private Long id;
    private RequestStatus requestStatus;
    private String nextGradeLevel;
    private String nextGradeLevelDescription;
    private AddressBean schoolAddress;
    private Long schoolYear;
    private Long regionId = -1L;
    private Long districtId = -1L;
    private String municipalityCode = "NONE";
    private Long schoolId = -1L;
    private String schoolName;
    private Long extSchoolNumber = -1L;
    private String comments;
    private boolean hasPreviousEnrollment;
    private Date submitDate;
    private String studentFullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getNextGradeLevel() {
        return nextGradeLevel;
    }

    public void setNextGradeLevel(String nextGradeLevel) {
        this.nextGradeLevel = nextGradeLevel;
    }

    public String getNextGradeLevelDescription() {
        return nextGradeLevelDescription;
    }

    public void setNextGradeLevelDescription(String nextGradeLevelDescription) {
        this.nextGradeLevelDescription = nextGradeLevelDescription;
    }

    public AddressBean getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(AddressBean schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public Long getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Long schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getExtSchoolNumber() {
        return extSchoolNumber;
    }

    public void setExtSchoolNumber(Long extSchoolNumber) {
        this.extSchoolNumber = extSchoolNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isHasPreviousEnrollment() {
        return hasPreviousEnrollment;
    }

    public void setHasPreviousEnrollment(boolean hasPreviousEnrollment) {
        this.hasPreviousEnrollment = hasPreviousEnrollment;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public String getRequestStatusText() {
        return this.requestStatus != null ? this.requestStatus.getDescription() : RequestStatus.ACTIVE.getDescription();
    }
}