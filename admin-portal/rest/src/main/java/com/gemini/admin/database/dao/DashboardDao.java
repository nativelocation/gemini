package com.gemini.admin.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 4:21 PM
 */
@Repository
public class DashboardDao extends NamedParameterJdbcDaoSupport {

  @Autowired
  private DataSource dataSource;

  @PostConstruct
  private void init() {
    setDataSource(dataSource);
  }

}
