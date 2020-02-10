package com.nabucco.petshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElasticLogDTO {

  private String region;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = Shape.STRING)
  private LocalDateTime postDate;

  private String message;
}
