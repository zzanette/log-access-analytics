package com.nabucco.petshop.domain;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "log")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Log extends AbastractEntity<Log> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotBlank
  @Column(name = "url")
  private String url;

  @NotNull
  @Column(name = "date_time")
  private Date dateTime;

  @NotBlank
  @Column(name = "user_uuid")
  private String userUuid;

  @NotNull
  @Column(name = "region_code")
  private Integer regionCode;

  public Log(String url, Date dateTime, String userUuid, Integer regionCode) {
    this.url = url;
    this.dateTime = dateTime;
    this.userUuid = userUuid;
    this.regionCode = regionCode;
    this.isValid();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Log)) {
      return false;
    }
    Log log = (Log) o;
    return Objects.equals(id, log.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
