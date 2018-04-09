package com.gemini.admin.services;

import com.gemini.admin.beans.AdminUser;
import com.gemini.admin.database.dao.SmaxUserDao;
import com.gemini.admin.database.dao.beans.SieRole;
import com.gemini.admin.database.dao.beans.SieUser;
import com.gemini.admin.database.jpa.repositories.AdminUserRepository;
import com.gemini.commons.utils.CopyUtils;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(sieRoles == null){
            return null;
        }
        AdminUser user = CopyUtils.convert(sieUser, AdminUser.class);
        user.setPrecedence(sieRoles.get(0).getPrecedence());
//        FluentIterable.from(sieRoles) ;

        return user;
    }


}