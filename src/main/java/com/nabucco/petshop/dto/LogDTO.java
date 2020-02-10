package com.nabucco.petshop.dto;

import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.annotation.StringValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

  @StringValidation(name = "Url", required = true)
  private String url;

  @StringValidation(name = "Timestamp", required = true)
  private String timestampUser;

  @StringValidation(name = "User's uuid", required = true)
  private String userUuid;

  @Required
  private Integer regionCode;
}
