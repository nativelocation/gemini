package com.gemini.admin.services;

import com.gemini.admin.beans.AdminUser;
import com.gemini.admin.database.AdminAccessHelper;
import com.gemini.admin.database.dao.SmaxUserDao;
import com.gemini.admin.database.dao.beans.SieRole;
import com.gemini.admin.database.dao.beans.SieUser;
import com.gemini.admin.database.jpa.entities.AdminUserEntity;
import com.gemini.admin.database.jpa.repositories.AdminUserRepository;
import com.gemini.admin.security.Authorities;
import com.gemini.commons.utils.CopyUtils;
import com.gemini.commons.utils.ValidationUtils;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 3/17/18
 * Time: 2:41 PM
 */
@Service
public class UserService {

    @Autowired
    private SmaxUserDao userDao;
    @Autowired
    private AdminUserRepository userRepository;

    public AdminUser loadUserByUsername(String username) {
        SieUser sieUser = userDao.loadByUsername(username);
        if (sieUser == null)
            return null;
        List<SieRole> sieRoles = userDao.loadRoles(sieUser.getUserId());
        if (sieRoles == null) {
            return null;
        }
        int precedence = sieRoles.get(0).getPrecedence();
        List<Long> regionsAllowed = Collections.emptyList();
        List<Long> schoolsAllowed = Collections.emptyList();
        Authorities authority = null;

        switch (precedence) {
            case AdminAccessHelper.DE_CENTRAL_LEVEL:
                authority = Authorities.DE_CENTRAL;
                break;
            case AdminAccessHelper.REGION_LEVEL:
                regionsAllowed = FluentIterable
                        .from(sieRoles)
                        .filter(new Predicate<SieRole>() {
                            @Override
                            public boolean apply(SieRole role) {
                                return ValidationUtils.valid(role.getRegionId());
                            }
                        })
                        .transform(new Function<SieRole, Long>() {
                            @Override
                            public Long apply(SieRole role) {
                                return role.getRegionId();
                            }
                        })
                        .toList();
                authority = regionsAllowed.size() > 0
                        ? Authorities.ACCESS_ON_VARIOUS_REGIONS
                        : Authorities.REGION_DIRECTOR;
                break;
            case AdminAccessHelper.SCHOOL_LEVEL:
                schoolsAllowed = FluentIterable
                        .from(sieRoles)
                        .filter(new Predicate<SieRole>() {
                            @Override
                            public boolean apply(SieRole role) {
                                return ValidationUtils.valid(role.getSchoolId());
                            }
                        })
                        .transform(new Function<SieRole, Long>() {
                            @Override
                            public Long apply(SieRole role) {
                                return role.getGroupId();
                            }
                        })
                        .toList();
                authority = regionsAllowed.size() > 0
                        ? Authorities.ACCESS_ON_VARIOUS_SCHOOLS
                        : Authorities.SCHOOL_DIRECTOR;
                break;
        }

        if (authority == null)
            return null;


        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.name());
        AdminUser user = new AdminUser(
                sieUser.getUserId(),
                sieUser.getUsername(),
                sieUser.getPassword(),
                true,
                precedence,
                Sets.newHashSet(grantedAuthority),
                regionsAllowed,
                schoolsAllowed);


        AdminUserEntity entity = userRepository.findByUserId(user.getUserId());
        if (entity == null) {
            entity = CopyUtils.convert(user, AdminUserEntity.class);
        }
        entity.setLoggedAuthority(authority);
        userRepository.save(entity);

        return user;
    }


}