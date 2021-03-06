package com.gemini.services;

import com.gemini.beans.forms.*;
import com.gemini.beans.integration.SchoolResponse;
import com.gemini.beans.internal.RequestSearchResult;
import com.gemini.beans.requests.ReasonForNotAttendingRequest;
import com.gemini.beans.requests.enrollment.AlternateSchoolPreEnrollmentSubmitRequest;
import com.gemini.beans.requests.enrollment.PreEnrollmentInitialRequest;
import com.gemini.beans.requests.enrollment.PreEnrollmentSubmitRequest;
import com.gemini.beans.requests.enrollment.VocationalPreEnrollmentSubmitRequest;
import com.gemini.beans.types.*;
import com.gemini.database.dao.beans.*;
import com.gemini.database.jpa.entities.*;
import com.gemini.database.jpa.respository.AddressRepository;
import com.gemini.database.jpa.respository.PreEnrollmentRepository;
import com.gemini.database.jpa.respository.StudentRepository;
import com.gemini.database.jpa.respository.UserRepository;
import com.gemini.utils.CopyUtils;
import com.gemini.utils.Utils;
import com.gemini.utils.ValidationUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 2/20/18
 * Time: 10:21 PM
 */
@Service
public class PreEnrollmentService {

    final String PR_COUNTRY = "PR";
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolmaxService smaxService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PreEnrollmentRepository preEnrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CommonService commonService;

    public PreEnrollmentBean createPreEnrollment(PreEnrollmentInitialRequest request, User loggedUser) {
        PreEnrollmentBean preEnrollmentBean = null;
        PreEnrollmentRequestEntity entity = new PreEnrollmentRequestEntity();
        Long studentNumber = request.getStudentNumber();
        entity.setType(request.getType());
//        UserEntity parent = CopyUtils.convert(loggedUser, UserEntity.class);
//        entity.setUser(parent);
        entity.setSchoolYear(commonService.getPreEnrollmentYear());
        Student student = null;
        StudentAddress address = null;
        String cleanSSN = Utils.cleanSsn(request.getSsn());
        request.setSsn(cleanSSN);
        if (studentNumber != null && studentNumber > 0L) {
            student = smaxService.retrieveStudentInfo(studentNumber);
            if (student != null) {
                preEnrollmentBean = new PreEnrollmentBean();
                address = smaxService.retrieveStudentAddress(studentNumber);
                //let's find the most recent enrollment from SIS
                EnrollmentInfo enrollmentInfo = smaxService.retrieveMostRecentEnrollment(student.getStudentId());
                if (enrollmentInfo != null) {

                    preEnrollmentBean = CopyUtils.convert(entity, PreEnrollmentBean.class);
                    boolean hasPrevious = commonService.isPreviousEnrollment(enrollmentInfo);
                    boolean hasPreEnrollment = commonService.isPreEnrollmentYear(enrollmentInfo);
                    boolean hasOld = commonService.isOldEnrollment(enrollmentInfo);
                    if (hasPrevious || hasOld) {
                        setDataFromSIEEnrollmentOn(enrollmentInfo, entity, true);
                    } else if (hasPreEnrollment) {
                        setDataFromSIEEnrollmentOn(enrollmentInfo, entity, false);
                    }
                }
                AddressEntity postal = copyAddressFrom(address, AddressType.POSTAL);
                AddressEntity physical = copyAddressFrom(address, AddressType.PHYSICAL);
                postal = saveAddress(postal);
                physical = saveAddress(physical);
                //todo: fran verify this later
                student.setFamilyId(address.getFamilyId());
                StudentEntity studentEntity = saveStudent(student, postal, physical);
                entity.setStudent(studentEntity);
                entity = preEnrollmentRepository.save(entity);
            }

        } else {
            AddressEntity postal = copyAddressFrom(null, AddressType.POSTAL);
            AddressEntity physical = copyAddressFrom(null, AddressType.PHYSICAL);
            StudentEntity studentEntity = CopyUtils.convert(request, StudentEntity.class);
            //saving addresses
            postal = saveAddress(postal);
            physical = saveAddress(physical);
            studentEntity.setPhysical(physical);
            studentEntity.setPostal(postal);
            studentEntity.setEntryType(EntryType.NEW);
            studentEntity.setLastName(request.getLastName());
            //saving student
            studentEntity = saveStudent(studentEntity);
            entity.setStudent(studentEntity);
            entity = preEnrollmentRepository.save(entity);
            preEnrollmentBean = CopyUtils.convert(entity, PreEnrollmentBean.class);
        }

        if (preEnrollmentBean != null && entity != null) {
            preEnrollmentBean.setId(entity.getId());
            preEnrollmentBean.setStudent(getPreEnrollmentStudentInfoBean(entity));
            userService.saveUserPreEnrollment(loggedUser.getId(), entity);
        }

        return getPreEnrollmentBean(entity);

    }

    public PreEnrollmentBean updatePreEnrollment(PreEnrollmentInitialRequest request) {
        PreEnrollmentRequestEntity requestEntity;
        if (request.getRequestId() != null && request.getRequestId() > 0L)
            requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        else
            requestEntity = preEnrollmentRepository.findByStudentNumber(request.getStudentNumber());
        StudentEntity studentEntity = studentRepository.findOne(requestEntity.getStudent().getId());
        //todo: check if personal data will be able to modified on found students
        if (EntryType.NEW.equals(studentEntity.getEntryType())) {
            studentEntity.setFirstName(request.getFirstName());
            studentEntity.setMiddleName(request.getMiddleName());
            studentEntity.setLastName(request.getLastName());
            studentEntity.setDateOfBirth(request.getDateOfBirth());
            studentEntity.setGender(request.getGender());
            studentEntity = studentRepository.save(studentEntity);
        }

        return requestEntity != null ? getPreEnrollmentBean(requestEntity) : null;
    }

    public PreEnrollmentBean findById(Long id) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findOne(id);
        if (entity != null) {
            return getPreEnrollmentBean(entity);
        }
        return null;
    }

    //    todo: fran validate this with client
    public SchoolResponse findSchoolByPreEnrollmentId(Long id) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findOne(id);
        Long schoolId = 0L;
        switch (entity.getType()) {
            case REGULAR:
                schoolId = entity.getSchoolId();
                break;
            case REGULAR_ALTERNATE_SCHOOLS:
            case SPECIALIZED_ALTERNATE_SCHOOLS:
                if (entity.getAlternateSchools() != null && !entity.getAlternateSchools().isEmpty())
                    schoolId = entity.getAlternateSchools().get(0).getSchoolId();
                break;
            case OCCUPATIONAL:
            case TECHNIQUE:
                VocationalPreEnrollmentBean vocational = findVocationalPreEnrollmentById(id);
                if (vocational.getEnrollments() != null && !vocational.getEnrollments().isEmpty())
                    schoolId = vocational.getEnrollments().get(0).getSchoolId();
                break;
        }
        School school = getSchool(schoolId);
        SchoolResponse response = CopyUtils.convert(school, SchoolResponse.class);
        response.setAddress(CopyUtils.createAddressBean(school));
        return response;
    }

    public List<PreEnrollmentBean> findPreEnrollmentByUser(User loggedUser) {
        UserEntity user = userRepository.findOne(loggedUser.getId());
        List<PreEnrollmentRequestEntity> entities = user.getRequests();//userService.findByUserIdOrderBySubmitDateDesc(loggedUser.getId());
        if (entities != null) {
            List<PreEnrollmentBean> list = new ArrayList<>();
            for (PreEnrollmentRequestEntity entity : entities) {
                list.add(getPreEnrollmentBean(entity));
            }
            return list;
        }
        return null;
    }

    public RequestSearchResult exists(PreEnrollmentInitialRequest request, User loggedUser) {
        RequestSearchResult result = new RequestSearchResult();
        PreEnrollmentRequestEntity entity = null;

        if (ValidationUtils.valid(request.getRequestId())) {
            entity = preEnrollmentRepository.findOne(request.getRequestId());
            result.setExists(entity != null);
        }

        if (!result.isExists() && ValidationUtils.valid(request.getStudentNumber())) {
            entity = preEnrollmentRepository.findByStudentNumber(request.getStudentNumber());
            result.setExists(entity != null);
        }

        if (!result.isExists()) {
            //do search by firstname, lastname, birthdate & ssn
//            entity = preEnrollmentRepository.findByDateOfBirthAndFirstNameAndLastName(request.getDateOfBirth(), request.getFirstName(), request.getLastName());
            String ssn = Utils.cleanSsn(request.getSsn());
            entity = preEnrollmentRepository.findBySsn(ssn);
            result.setExists(entity != null);
        }

        if (result.isExists()) {
            final Long matchId = entity.getId();
            UserEntity userEntity = userRepository.findOne(loggedUser.getId());
            boolean isUserRequest = FluentIterable
                    .from(userEntity.getRequests())
                    .firstMatch(new Predicate<PreEnrollmentRequestEntity>() {
                        @Override
                        public boolean apply(PreEnrollmentRequestEntity preEntity) {
                            return preEntity.getId().equals(matchId);
                        }
                    }).isPresent();
            result.setBelongsToUser(isUserRequest);
        }

        if (result.isExists()) {
            result.setRequestId(entity.getId());
            result.setCompleted(!RequestStatus.ACTIVE.equals(entity.getRequestStatus()));
        }
        return result;
    }

    public PreEnrollmentStudentInfoBean findPreEnrollmentById(Long id) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findByIdAndRequestStatusIs(id, RequestStatus.ACTIVE);
        if (entity == null)
            return null;
        return getPreEnrollmentStudentInfoBean(entity);
    }

    public VocationalPreEnrollmentBean findVocationalPreEnrollmentById(Long id) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findByIdAndRequestStatusIs(id, RequestStatus.ACTIVE);
        if (entity == null)
            return null;
        PreEnrollmentBean preEnrollment = getPreEnrollmentBean(entity);
        VocationalPreEnrollmentBean vocationalPreEnrollment = CopyUtils.convert(preEnrollment, VocationalPreEnrollmentBean.class);
        Function<PreEnrollmentVocationalSchoolEntity, Long> toSchoolId = new Function<PreEnrollmentVocationalSchoolEntity, Long>() {
            @Override
            public Long apply(PreEnrollmentVocationalSchoolEntity schoolEntity) {
                return schoolEntity.getSchoolId();
            }
        };

        final Function<PreEnrollmentVocationalSchoolEntity, VocationalProgramSelection> toProgramSelection = new Function<PreEnrollmentVocationalSchoolEntity, VocationalProgramSelection>() {
            @Override
            public VocationalProgramSelection apply(PreEnrollmentVocationalSchoolEntity schoolEntity) {
                VocationalProgramSelection selection = new VocationalProgramSelection();
                selection.setSchoolId(schoolEntity.getSchoolId());
                selection.setProgramCode(schoolEntity.getProgramCode());
                selection.setProgramDescription(schoolEntity.getProgramDescription());
                return selection;
            }
        };
        final Multimap<Long, PreEnrollmentVocationalSchoolEntity> schoolProgramMap = Multimaps.index(entity.getVocationalSchools(), toSchoolId);
        Function<Long, VocationalSchoolEnrollment> toVocationalProgram = new Function<Long, VocationalSchoolEnrollment>() {
            @Override
            public VocationalSchoolEnrollment apply(Long schoolId) {
                VocationalSchoolEnrollment enrollment = new VocationalSchoolEnrollment();
                School school = getSchool(schoolId);
                enrollment.setSchoolId(schoolId);
                enrollment.setSchoolName(school.getSchoolName());
                enrollment.setSchoolAddress(CopyUtils.createAddressBean(school));
                List<VocationalProgramSelection> programs = Lists.transform(Lists.newArrayList(schoolProgramMap.get(schoolId)), toProgramSelection);
                enrollment.setPrograms(programs);
                return enrollment;
            }
        };

        List<Long> schoolIds = Lists.newArrayList(schoolProgramMap.keySet());
        vocationalPreEnrollment.setEnrollments(Lists.transform(schoolIds, toVocationalProgram));
        return vocationalPreEnrollment;

    }

    public boolean updateStudentAddress(AddressBean address) {
        AddressEntity entity = CopyUtils.convert(address, AddressEntity.class);
        entity = saveAddress(entity);
        return entity != null;
    }

    public PreEnrollmentAddressBean getAddress(Long requestId) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findOne(requestId);
        PreEnrollmentAddressBean addressBean = new PreEnrollmentAddressBean(requestId);
        if (entity != null && entity.getStudent() != null) {
            addressBean.setPhysical(CopyUtils.convert(entity.getStudent().getPhysical(), AddressBean.class));
            addressBean.setPostal(CopyUtils.convert(entity.getStudent().getPostal(), AddressBean.class));
        }
        return addressBean;
    }

    public boolean validAddressForRequestId(Long id, AddressBean physical, AddressBean postal) {
        boolean valid = false;
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findOne(id);
        if (entity != null && entity.getStudent() != null) {
            AddressEntity physicalEntity = entity.getStudent().getPhysical();
            AddressEntity postalEntity = entity.getStudent().getPostal();
            if (ValidationUtils.valid(physical.getId(), physicalEntity.getId())) {
                valid = physical.getId().equals(physicalEntity.getId());
            }

            if (ValidationUtils.valid(postal.getId(), postalEntity.getId())) {
                valid &= postal.getId().equals(postalEntity.getId());
            }

        }
        return valid;
    }

    public boolean submitPreEnrollment(PreEnrollmentSubmitRequest request) {
        PreEnrollmentRequestEntity requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        setSchoolInfo(requestEntity, request.getSchoolId(), request.getNextGradeLevel());
        requestEntity.setType(EnrollmentType.REGULAR);
        requestEntity.setRequestStatus(RequestStatus.APPROVED);
        requestEntity.setSubmitDate(commonService.getCurrentDate());
        requestEntity = preEnrollmentRepository.save(requestEntity);
        return requestEntity != null;
    }

    public boolean partialVocationalPreEnrollmentSave(final VocationalPreEnrollmentSubmitRequest request) {
        final PreEnrollmentRequestEntity requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        requestEntity.setType(request.getType());
        final List<PreEnrollmentVocationalSchoolEntity> vocationalSchoolsDB = requestEntity.getVocationalSchools();
        List<VocationalProgramSelection> selectionsInDBList = CopyUtils.convert(vocationalSchoolsDB, VocationalProgramSelection.class);
        Set<VocationalProgramSelection> selectionsInDB = new HashSet<>(selectionsInDBList);

        if (ValidationUtils.valid(request.getSchoolIdToDelete())) {
            List<VocationalProgramSelection> schoolIdProgramsToDelete = FluentIterable
                    .from(vocationalSchoolsDB)
                    .filter(new Predicate<PreEnrollmentVocationalSchoolEntity>() {
                        @Override
                        public boolean apply(PreEnrollmentVocationalSchoolEntity vocSchoolEntity) {
                            return vocSchoolEntity.getSchoolId().equals(request.getSchoolIdToDelete());
                        }
                    })
                    .transform(new Function<PreEnrollmentVocationalSchoolEntity, VocationalProgramSelection>() {
                        @Override
                        public VocationalProgramSelection apply(PreEnrollmentVocationalSchoolEntity vocSchoolEntity) {
                            return CopyUtils.convert(vocSchoolEntity, VocationalProgramSelection.class);
                        }
                    }).toList();
            request.setProgramsToDelete(schoolIdProgramsToDelete);
        }


        Set<VocationalProgramSelection> selectionsToSave = Sets.newHashSet();
        boolean deleting = request.getProgramsToDelete() != null && !request.getProgramsToDelete().isEmpty();
        boolean adding = request.getPrograms() != null && !request.getPrograms().isEmpty();
        //deleting
        if (deleting) {
            selectionsToSave = Sets.difference(selectionsInDB, Sets.newHashSet(request.getProgramsToDelete()));
            selectionsInDB = selectionsToSave;
        }
        //adding
        if (adding)
            selectionsToSave = Sets.union(selectionsInDB, new HashSet<>(request.getPrograms()));

        if (!(adding || deleting)) {
            selectionsToSave = selectionsInDB;
        }

        List<VocationalProgramSelection> list = Lists.newArrayList(selectionsToSave);

        Function<VocationalProgramSelection, PreEnrollmentVocationalSchoolEntity> toPreEnrollmentVocSchool = new Function<VocationalProgramSelection, PreEnrollmentVocationalSchoolEntity>() {
            public PreEnrollmentVocationalSchoolEntity apply(VocationalProgramSelection program) {
                PreEnrollmentVocationalSchoolEntity vocationalSchool = CopyUtils.convert(program, PreEnrollmentVocationalSchoolEntity.class);
                School school = smaxService.findSchoolById(program.getSchoolId());
                vocationalSchool.setSchoolId(school.getSchoolId());
                vocationalSchool.setRegionId(school.getRegionId());
                vocationalSchool.setDistrictId(school.getDistrictId());
                vocationalSchool.setMunicipalityCode(school.getCityCd());
                vocationalSchool.setPreEnrollment(requestEntity);
                return vocationalSchool;
            }
        };

        if (request.getNextGradeLevel() != null)
            requestEntity.setGradeLevel(request.getNextGradeLevel());

        requestEntity.getVocationalSchools().clear();
        requestEntity.getVocationalSchools().addAll(Lists.transform(list, toPreEnrollmentVocSchool));

        return preEnrollmentRepository.save(requestEntity) != null;

    }

    public boolean vocationalPreEnrollmentSubmit(VocationalPreEnrollmentSubmitRequest request) {
        final PreEnrollmentRequestEntity requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        requestEntity.setType(request.getType());
        List<PreEnrollmentVocationalSchoolEntity> list = requestEntity.getVocationalSchools();
        if (list.size() == 1) {
            Long schoolId = list.get(0).getSchoolId();
            setSchoolInfo(requestEntity, schoolId, request.getNextGradeLevel());
        }
        requestEntity.setRequestStatus(RequestStatus.PENDING_TO_REVIEW);
        requestEntity.setSubmitDate(commonService.getCurrentDate());

        return preEnrollmentRepository.save(requestEntity) != null;
    }

    public AlternateSchoolPreEnrollmentBean findAlternatePreEnrollmentById(Long id) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findByIdAndRequestStatusIs(id, RequestStatus.ACTIVE);
        if (entity == null)
            return null;
        PreEnrollmentBean preEnrollment = getPreEnrollmentBean(entity);
        AlternateSchoolPreEnrollmentBean alternatePreEnrollmentBean = CopyUtils.convert(preEnrollment, AlternateSchoolPreEnrollmentBean.class);
        Function<PreEnrollmentAlternateSchoolEntity, AlternateSchoolBean> toAlternate = new Function<PreEnrollmentAlternateSchoolEntity, AlternateSchoolBean>() {
            @Override
            public AlternateSchoolBean apply(PreEnrollmentAlternateSchoolEntity altEntity) {
                AlternateSchoolBean bean = new AlternateSchoolBean();
                School school = smaxService.findSchoolById(altEntity.getSchoolId());
                bean.setPriority(altEntity.getPriority());
                bean.setRegionId(school.getRegionId());
                bean.setSchool(CopyUtils.createSchoolResponse(school));
                return bean;
            }
        };
        alternatePreEnrollmentBean.setAlternateSchools(Lists.transform(entity.getAlternateSchools(), toAlternate));

        return alternatePreEnrollmentBean;
    }

    public boolean partialAlternatePreEnrollmentSave(final AlternateSchoolPreEnrollmentSubmitRequest request) {
        final PreEnrollmentRequestEntity requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        requestEntity.setType(request.getType());

        final List<PreEnrollmentAlternateSchoolEntity> alternateSchoolsDB = requestEntity.getAlternateSchools();
        List<AlternateSchoolBean> altSchoolsInDBList = CopyUtils.convert(alternateSchoolsDB, AlternateSchoolBean.class);
        Set<AlternateSchoolBean> alternateSchoolsFormInDb = new HashSet<>(altSchoolsInDBList);

        List<AlternateSchoolBean> cleanedList = FluentIterable
                .from(request.getAlternateSchools())
                .filter(new Predicate<AlternateSchoolBean>() {
                    @Override
                    public boolean apply(AlternateSchoolBean alternateSchoolBean) {
                        return ValidationUtils.valid(alternateSchoolBean.getSchoolId());
                    }
                }).toList();


        Set<AlternateSchoolBean> alternateSchoolToSave = Sets.newHashSet();
        boolean deleting = request.getAlternateSchoolsToDelete() != null && !request.getAlternateSchoolsToDelete().isEmpty();
        boolean adding = cleanedList != null && !cleanedList.isEmpty();
        //deleting
        if (deleting) {
            alternateSchoolToSave = Sets.difference(alternateSchoolsFormInDb, Sets.newHashSet(request.getAlternateSchoolsToDelete()));
            alternateSchoolsFormInDb = alternateSchoolToSave;
        }
        //adding
        if (adding)
            alternateSchoolToSave = Sets.union(alternateSchoolsFormInDb, new HashSet<>(cleanedList));

        if (!(adding || deleting)) {
            alternateSchoolToSave = alternateSchoolsFormInDb;
        }

        List<PreEnrollmentAlternateSchoolEntity> toSave = FluentIterable
                .from(Lists.newArrayList(alternateSchoolToSave))
                .transform(new Function<AlternateSchoolBean, PreEnrollmentAlternateSchoolEntity>() {
                    @Override
                    public PreEnrollmentAlternateSchoolEntity apply(AlternateSchoolBean alternateSchool) {
                        PreEnrollmentAlternateSchoolEntity entity = new PreEnrollmentAlternateSchoolEntity();
                        School school = smaxService.findSchoolById(alternateSchool.getSchoolId());
                        entity.setPriority(alternateSchool.getPriority());
                        entity.setSchoolId(school.getSchoolId());
                        entity.setRegionId(school.getRegionId());
                        entity.setDistrictId(school.getDistrictId());
                        entity.setMunicipalityCode(school.getCityCd());
                        entity.setPreEnrollment(requestEntity);
                        return entity;
                    }
                })
                .toList();

        if (toSave != null && !toSave.isEmpty() && toSave.size() == 1) {
            toSave.get(0).setPriority(1);
        }

        if (request.getNextGradeLevel() != null)
            requestEntity.setGradeLevel(request.getNextGradeLevel());

        requestEntity.getAlternateSchools().clear();
        requestEntity.getAlternateSchools().addAll(toSave);

        return preEnrollmentRepository.save(requestEntity) != null;
    }

    public boolean alternatePreEnrollmentSubmit(final AlternateSchoolPreEnrollmentSubmitRequest request) {
        final PreEnrollmentRequestEntity requestEntity = preEnrollmentRepository.findOne(request.getRequestId());
        requestEntity.setType(request.getType());
        requestEntity.setRequestStatus(RequestStatus.PENDING_TO_REVIEW);
        requestEntity.setSubmitDate(commonService.getCurrentDate());
        return preEnrollmentRepository.save(requestEntity) != null;
    }

    public boolean saveReasonForNotAttending(ReasonForNotAttendingRequest request) {
        PreEnrollmentRequestEntity entity = preEnrollmentRepository.findOne(request.getRequestId());
        if (entity == null)
            return false;
        entity.setReasonForNotAttendSchool(request.getReason());

        if (ReasonForNotAttendingSchool.MOVE_OUT_OF_COUNTRY.equals(request.getReason())) {
            entity.setType(EnrollmentType.REGULAR);
            entity.setRequestStatus(RequestStatus.DENIED_BY_PARENT);
            entity.setSubmitDate(commonService.getCurrentDate());
        }

        entity = preEnrollmentRepository.save(entity);
        return entity != null;
    }

    //    Private Methods
    private PreEnrollmentStudentInfoBean getPreEnrollmentStudentInfoBean(PreEnrollmentRequestEntity entity) {
        StudentEntity studentEntity = entity.getStudent();
        PreEnrollmentStudentInfoBean studentInfo = CopyUtils.convert(studentEntity, PreEnrollmentStudentInfoBean.class);
        Utils.copyLastNames(studentEntity, studentInfo);
        studentInfo.setStudentNumber(studentEntity.getExtStudentNumber());
        studentInfo.setId(studentEntity.getId());
        if (ValidationUtils.valid(studentEntity.getExtStudentNumber())) {
            studentInfo.setLastSsnFormatted(Utils.obfuscatedSsn(studentEntity.getSsn()));
        }
        //todo: fran change this!!!
        studentInfo.setType(entity.getType());
        return studentInfo;
    }

    private PreEnrollmentBean getPreEnrollmentBean(PreEnrollmentRequestEntity entity) {
        PreEnrollmentBean enrollmentBean = CopyUtils.convert(entity, PreEnrollmentBean.class);
        enrollmentBean.setStudent(getPreEnrollmentStudentInfoBean(entity));
        if (ValidationUtils.valid(entity.getSchoolId())) {
            School school = getSchool(entity.getSchoolId());
            enrollmentBean.setSchoolId(school.getSchoolId());
            enrollmentBean.setSchoolName(school.getSchoolName());
            enrollmentBean.setSchoolAddress(CopyUtils.createAddressBean(school));
        }
        enrollmentBean.setHasPreEnrollment(ValidationUtils.valid(entity.getPreEnrollmentId()));
        enrollmentBean.setHasPreviousEnrollment(ValidationUtils.valid(entity.getPreviousEnrollmentId()));
        enrollmentBean.setDeniedByUser(ReasonForNotAttendingSchool.MOVE_OUT_OF_COUNTRY.equals(entity.getReasonForNotAttendSchool()));
        setGradeLevelInfo(entity.getGradeLevel(), enrollmentBean);
        return enrollmentBean;
    }

    private StudentEntity saveStudent(Student bean, AddressEntity postal, AddressEntity physical) {
        StudentEntity entity = CopyUtils.convert(bean, StudentEntity.class);
        entity.setGender(bean.getGender());
        entity.setSisStudentId(bean.getStudentId());
        entity.setPostal(postal);
        entity.setPhysical(physical);
        if (StringUtils.hasText(bean.getEthnicCd())) {
            EthnicCodeEntity ethnicCodeEntity = new EthnicCodeEntity();
            ethnicCodeEntity.setValue(bean.getEthnicCd());
            ethnicCodeEntity.setDescription(bean.getEthnicCode());
            entity.setEthnicCodes(Arrays.asList(ethnicCodeEntity));
        }
        return studentRepository.save(entity);
    }

    private AddressEntity saveAddress(AddressEntity entity) {
        return addressRepository.save(entity);
    }

    private StudentEntity saveStudent(StudentEntity entity) {
        return studentRepository.save(entity);
    }

    private AddressEntity copyAddressFrom(StudentAddress address, AddressType type) {
        AddressEntity entity = new AddressEntity();
        entity.setType(type);
        if (address == null)
            return entity;
        switch (type) {
            case POSTAL:
                entity.setLine1(address.getPostalAddress_1());
                entity.setLine2(address.getPostalAddress_2());
                entity.setCity(address.getPostalCityCode());
                entity.setCountry(PR_COUNTRY);
                entity.setZipcode(address.getPostalZipcode());
                break;
            case PHYSICAL:
                entity.setLine1(address.getPhysicalAddress_1());
                entity.setLine2(address.getPhysicalAddress_2());
                entity.setCity(address.getPhysicalCityCode());
                entity.setCountry(PR_COUNTRY);
                entity.setZipcode(address.getPhysicalZipcode());
                break;
        }
        return entity;
    }

    private School setDataFromSIEEnrollmentOn(EnrollmentInfo info, PreEnrollmentRequestEntity entity, boolean isPreviousEnrollment) {
        Long schoolId = info.getSchoolId();
        String gradeLevel = info.getGradeLevel();
        Long schoolYear = info.getSchoolYear();
        Long enrollmentId = info.getEnrollmentId();
        School school = smaxService.findSchoolById(schoolId);
        if (isPreviousEnrollment) {
            entity.setPreviousEnrollmentId(enrollmentId);
            entity.setPreviousGradeLevel(gradeLevel);
            entity.setPreviousEnrollmentYear(schoolYear);
        } else if (ValidationUtils.valid(schoolYear, gradeLevel)) {
            entity.setPreEnrollmentId(enrollmentId);
            if (school != null) {
                entity.setSchoolId(schoolId);
                entity.setDistrictId(school.getDistrictId());
                entity.setRegionId(school.getRegionId());
                entity.setExtSchoolNumber(school.getExtSchoolNumber());
                entity.setMunicipalityCode(school.getCityCd());
            }
            if (StringUtils.hasText(gradeLevel)) {
                GradeLevel nextGradeLevel = smaxService.getGradeLevelByCode(gradeLevel);
                entity.setGradeLevel(nextGradeLevel.getName());
            }
        }
        return school;
    }

    private School getSchool(Long schoolId) {
        return smaxService.findSchoolById(schoolId);
    }

    private void setSchoolInfo(PreEnrollmentRequestEntity requestEntity, Long schoolId, String nextGradeLevel) {
        School school = smaxService.findSchoolById(schoolId);
        if (nextGradeLevel != null)
            requestEntity.setGradeLevel(nextGradeLevel);
        requestEntity.setExtSchoolNumber(school.getExtSchoolNumber());
        requestEntity.setSchoolId(school.getSchoolId());
        requestEntity.setRegionId(school.getRegionId());
        requestEntity.setDistrictId(school.getDistrictId());
        requestEntity.setMunicipalityCode(school.getCityCd());
    }

    private void setGradeLevelInfo(String nextLevel, PreEnrollmentBean enrollmentBean) {
        if (StringUtils.hasText(nextLevel)) {
            GradeLevel gradeLevel = smaxService.getGradeLevelByCode(nextLevel);
            if (gradeLevel != null) {
                enrollmentBean.setNextGradeLevel(gradeLevel.getName());
                enrollmentBean.setNextGradeLevelDescription(gradeLevel.getDisplayName());
            }
        }
    }

}