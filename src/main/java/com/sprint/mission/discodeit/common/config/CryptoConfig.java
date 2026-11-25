package com.sprint.mission.discodeit.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

public class CryptoConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
   * Argon2PasswordEncode (
   *            saltLength bytes
   *            hashLength bytes
   *            parallelism
   *            memory  KiB * MiB
   *            iterations
   * */
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new Argon2PasswordEncoder(
//        16,
//        32,
//        1,
//        19 * 1024,
//        2
//    );
//  }
}
