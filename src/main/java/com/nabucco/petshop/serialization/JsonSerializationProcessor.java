package com.nabucco.petshop.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.restexpress.serialization.json.JacksonJsonProcessor;

public class JsonSerializationProcessor extends JacksonJsonProcessor {

  @Override
  protected void initializeModule(SimpleModule module) {
    super.initializeModule(module);
  }
}
