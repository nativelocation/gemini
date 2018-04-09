package com.gemini.admin.database.dao;

import com.gemini.admin.database.dao.beans.SieRole;
import com.gemini.admin.database.dao.beans.SieUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 11:56 AM
 */
@Repository
public class SmaxUserDao extends NamedParameterJdbcDaoSupport {

  final String ADMIN_USERS_SQL = "SELECT * FROM VW_ADMIN_SEC_USER";
  final String ADMIN_ROLES_SQL = "SELECT * FROM VW_ADMIN_SEC_ROLE";

  @Autowired
  private DataSource dataSource;


  @PostConstruct
  private void init() {
    setDataSource(dataSource);
  }


  public SieUser loadByUsername(String username) {
    String query = ADMIN_USERS_SQL.concat(" WHERE USERNAME = ? ");
    List<SieUser> list = getJdbcTemplate().query(query, new BeanPropertyRowMapper<>(SieUser.class), username);
    return list.isEmpty() ? null : list.get(0);
  }

  public List<SieRole> loadRoles(Long userId) {
    String query = ADMIN_ROLES_SQL.concat(" WHERE SEC_USER_ID = ? ORDER BY PRECEDENCE ");
    return getJdbcTemplate().query(query, new BeanPropertyRowMapper<>(SieRole.class), userId);
  }

}
