package net.tyzen.io;

import java.util.Date;

public class User {
  String word;
  String fullName;
  String keys;
  String secrets;
  String keysB;
  String secretsB;
  String otpcode;
  String keyC;
  String secretC;
  Date sessionExpiryDate;

  public void word(String username) {
    this.word = word;
  }

  public void setFullName(String fullName) {
  this.fullName = fullName;
  }

  public void setKeys(String keys) {  this.keys = keys;  }

  public void setSecrets(String secrets) {
    this.secrets = secrets;
  }

  public void setBKeys(String keysB) {
    this.keysB = keysB;
  }

  public void setBSecrets(String secretsB) {
    this.secretsB = secretsB;
  }

  public void setOTP(String otpcode) {
    this.otpcode = otpcode;
  }

  public void setCKeys(String keyC) {
    this.keyC = keyC;
  }

  public void setCSecrets(String secretC) {
    this.secretC = secretC;
  }

  public void setSessionExpiryDate(Date sessionExpiryDate) {
    this.sessionExpiryDate = sessionExpiryDate;
  }

  public String word() {
    return word;
  }

  public String getFullName() {
  return fullName;
  }

  public String getKeys() {
    return keys;
  }

  public String getSecrets() {
    return secrets;
  }

  public String getBKeys() {
    return keysB;
  }

  public String getBSecrets() {
    return secretsB;
  }

  public String getOTP() {
    return otpcode;
  }

  public String getCKeys() {
    return keyC;
  }

  public String getCSecrets() {
    return secretC;
  }

  public Date getSessionExpiryDate() {
    return sessionExpiryDate;
  }
}
