
INSERT INTO users (id, date_of_birth, email, enabled, first_name, last_name, middle_name, password, relation_type, credentials_non_expired, profile_completed)
VALUES (HIBERNATE_SEQUENCE.NEXTVAL, CURRENT_TIMESTAMP, 'de.pentaho@gmail.com', 1, 'Test', 'User', '', '$2a$15$i8BsHOnBo49LqtxQZMUxH.hW1vv8xJh6DqdvUvU2I12iWkNWBez7O', 'FATHER', 1, 0);



INSERT INTO config (id, activation_key_expire_in_hours, can_edit_found_student, cred_key_expire_in_minutes, current_school_year, min_user_age_to_pre_enroll, pre_enrollment_end, pre_enrollment_school_year, pre_enrollment_start, min_alternate_schools)
VALUES (HIBERNATE_SEQUENCE.NEXTVAL, 24, 1, 15, 2018, 18, NULL, 2019, SYSDATE, 2);


