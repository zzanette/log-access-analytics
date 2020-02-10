package com.nabucco.petshop.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String url;
  private Long amountAccess;
}