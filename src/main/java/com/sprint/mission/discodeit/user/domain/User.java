package com.sprint.mission.discodeit.user.domain;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.domain.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
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
  private Password password;

  @Column(name = "email", nullable = false, length = 100, unique = true)
  private Email email;

  @OneToOne(fetch = FetchType.LAZY,
      cascade = CascadeType.REMOVE,
      orphanRemoval = true)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;
  @OneToOne(
      mappedBy = "user",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private UserStatus userStatus;

  private User(String name, Password password, Email email, BinaryContent profile) {
    this.username = name;
    this.password = password;
    this.email = Objects.requireNonNull(email, "email 값은 필수임.");
    this.profile = profile;
    this.userStatus = UserStatus.create(this, Instant.now());
  }

  public UserStatus refreshActivity(Instant lastActiveAt) {
    return this.userStatus.refresh(lastActiveAt);
  }


  public User update(String updateName, Password password, Email email, BinaryContent profile) {
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


  @Override
  public String toString() {
    return "이름 = " + this.username + " 메일 주소 = " + this.email;
  }


  public static User create(String username, Password password, Email email,
      BinaryContent profile) {

    return new User(username, password, email, profile);
  }
}
