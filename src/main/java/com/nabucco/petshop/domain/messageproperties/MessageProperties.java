package com.nabucco.petshop.domain.messageproperties;

import java.util.ResourceBundle;

public class MessageProperties {

  private static final String MESSAGES = "messages";
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES);

  public static String getMensagem(MessagePropertiesEnum propertiesEnum, Object... params) {
    String mensagem = resourceBundle.getString(propertiesEnum.getMessageKey());
    return String.format(mensagem, params);
  }
}
