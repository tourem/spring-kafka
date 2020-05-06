package com.sunilvb.demo;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "batch.security.ssl")
public class SslConfig {
  private Resource keyStore;
  private String keyStorePassword;
  private Resource trustStore;
  private String trustStorePassword;


  @PostConstruct
  public void setSystemProperties() throws IOException {
    System.setProperty("javax.net.ssl.keyStore", keyStore.getFile().getAbsolutePath());
    System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
    System.setProperty("javax.net.ssl.trustStore", trustStore.getFile().getAbsolutePath());
    System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
  }

  public void setKeyStore(Resource keyStore) {
    this.keyStore = keyStore;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  public void setTrustStore(Resource trustStore) {
    this.trustStore = trustStore;
  }

  public void setTrustStorePassword(String trustStorePassword) {
    this.trustStorePassword = trustStorePassword;
  }

  public Resource getKeyStore() {
    return keyStore;
  }

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public Resource getTrustStore() {
    return trustStore;
  }

  public String getTrustStorePassword() {
    return trustStorePassword;
  }
}
