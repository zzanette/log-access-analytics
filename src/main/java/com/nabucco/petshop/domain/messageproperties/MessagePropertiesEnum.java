package com.nabucco.petshop.domain.messageproperties;

import lombok.Getter;

@Getter
public enum MessagePropertiesEnum {

  ERROR_EXTRACT_CACHE_FROM_URL("errorExtractCacheFromURL.message"),
  ERROR_ADD_CACHE_TO_URL("errorAddCacheToURL.message"),
  ERROR_FLUSH_CACHE("errorFlushCache.message");

  private String messageKey;

  MessagePropertiesEnum(String messageKey) {
    this.messageKey = messageKey;
  }
}
