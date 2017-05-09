package br.com.soserver.serialization;

import org.apache.commons.lang3.StringUtils;



public class Message {

  public static enum MessageType {
    //0x01
    READ_COIL_STATUS(0x01),
    //0x02
    READ_INPUT_STATUS(0x02),
    //0x03
    READ_HOLDING_REGISTERS(0x03),
    //0x04
    READ_INPUT_REGISTERS(0x04),
    //0x05
    WRITE_SINGLE_COIL(0x05),
    //0x06
    WRITE_SINGLE_REGISTER(0x06),
    //0x015
    WRITE_MULTIPLE_COILS(0x015),
    //0x1
    WRITE_MULTIPLE_REGISTERS(0x1);
    public int hexa;

    MessageType(int hexadecimal){
        this.hexa = hexadecimal;
    }
  }

  public String getAddress() {
  	return this.address;
  }

  public String getFunctionCode() {
  	return this.functionCode;
  }

  public String getData() {
  	return this.data;
  }

  public String getDataSlice(int from, int to) {
  	return this.data.substring(from, to);
  }

  public void setAddress(String address) {
  	this.address = StringUtils.leftPad(address, 2, '0');
  }

  public void setFunctionCode(String function_code) {
  	this.functionCode = StringUtils.leftPad(function_code, 2, '0');
  }


  public void setAddress(int address) {
  	setAddress(toHex(address));
  }

  public void setFunctionCode(int function_code) {
  	setFunctionCode(toHex(function_code));
  }

  public void setFunctionCode(MessageType function_code) {
    setFunctionCode(toHex(function_code.hexa));
  }



  public void setDataSlice(String s, int from, int to) {
    StringBuilder builder;
    if (this.data != null) {
      builder = new StringBuilder(this.data);
    } else {
      builder = new StringBuilder();
    }
    if (builder.length() < to) {
      builder.setLength(to);
    }
  	builder.replace(from, to, StringUtils.leftPad(s, to-from, '0'));
  	this.data = builder.toString();
  }

  public void setDataSlice(int new_data, int from, int to) {
    if (to-from < 1) {
      return;
    }
    setDataSlice(toHex(new_data), from, to);
  }

  public static String toHex(int data) {
  	return (Integer.toHexString(data)).toUpperCase();
  }
  public static int fromHex(String data) {
    return Integer.parseInt(data, 16);
  }

  protected String address;
  protected String functionCode;
  protected String data;

}
