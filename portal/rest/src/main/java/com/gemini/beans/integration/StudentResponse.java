package com.gemini.beans.integration;

import com.gemini.beans.IdentityForm;
import com.gemini.beans.types.Gender;
import com.gemini.utils.Utils;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 2/9/18
 * Time: 12:27 AM
 */
public class StudentResponse implements IdentityForm {
    private Long studentNumber;
    private String firstName;
    private String middleName;
    private String fatherLastName;
    private String motherLastName;
    private Date dateOfBirth;
    private Gender gender;
    //todo: refactoring alert fran check these fields they are not needed
    private boolean isEnrolled;
    private int currentGradeLevel;
    private boolean found;

    public Long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public int getCurrentGradeLevel() {
        return currentGradeLevel;
    }

    public void setCurrentGradeLevel(int currentGradeLevel) {
        this.currentGradeLevel = currentGradeLevel;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public void setLastName(String lastName) {
        StringTokenizer tokenizer = new StringTokenizer(lastName.trim(), " ");
        this.fatherLastName = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
        this.motherLastName = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
    }

    public String getFullName() {
        return Utils.toFullName(firstName, middleName, fatherLastName, motherLastName);
    }

}