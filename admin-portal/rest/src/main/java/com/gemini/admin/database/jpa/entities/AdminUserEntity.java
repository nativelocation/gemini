package com.gemini.admin.database.jpa.entities;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: fran
 * Date: 4/8/18
 * Time: 12:09 PM
 */
@Entity
@Table(name = "admin_users")
public class AdminUserEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private Long secUsedId;

  @Column(nullable = false)
  private String username;

  @Column
  private Date lastLogin;

  @Column
  private boolean enabled = false;

  @CreatedDate
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
  private Date creationDate;

  @LastModifiedDate
  @Column()
  private Date revisionDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSecUsedId() {
    return secUsedId;
  }

  public void setSecUsedId(Long secUsedId) {
    this.secUsedId = secUsedId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getRevisionDate() {
    return revisionDate;
  }

  public void setRevisionDate(Date revisionDate) {
    this.revisionDate = revisionDate;
  }
}
