package com.sprint.mission.discodeit.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username;
  @Column(name = "password", nullable = false, length = 60)
  private String password;
  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;
  @OneToOne(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private UserStatus userStatus;

  private User(String name, String password, String email, BinaryContent profile) {
    this.username = name;
    this.password = password;
    this.email = email;
    this.profile = profile;
  }


  public User update(String updateName, String password, String email, BinaryContent profile) {
    if (updateName != null) {
      this.username = updateName;
    }
    if (password != null) {
      this.password = password;
    }
    if (email != null) {
      this.email = email;
    }
    if (profile != null) {
      this.profile = profile;
    }
    return this;
  }

  public void initStatus(Instant lastActiveAt) {
    this.userStatus = UserStatus.create(this, lastActiveAt);
  }

  @Override
  public String toString() {
    return "이름 = " + this.username + " 메일 주소 = " + this.email;
  }


  public static User create(String username, String password, String email, BinaryContent profile) {
    return new User(username, password, email, profile);
  }
}
