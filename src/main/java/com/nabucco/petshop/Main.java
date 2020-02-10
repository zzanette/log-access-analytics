package com.nabucco.petshop;

import com.nabucco.petshop.config.Configuration;
import com.nabucco.petshop.server.Server;
import org.restexpress.util.Environment;

public class Main {

  public static void main(String[] args) throws Exception {
    Configuration config = Environment.load(args, Configuration.class);
    Server server = new Server(config);
    server.start().awaitShutdown();
  }
}
