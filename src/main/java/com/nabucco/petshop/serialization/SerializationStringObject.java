package com.nabucco.petshop.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializationStringObject {

  /**
   * Read a string from object Base64 and return the boject
   *
   * @param s the {@link String} as an object serialized
   * @return the object
   */
  public static Object fromString(String s) throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(s);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    return ois.readObject();
  }

  /**
   * Serialize an {@link Serializable} object to an String Base64 enconded
   *
   * @param o the object
   * @return the object serialized as String
   * @throws IOException throwed by {@link ObjectOutputStream}
   */
  public static String toString(Serializable o) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(o);
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }
}
