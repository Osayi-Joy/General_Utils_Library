package com.osayijoy.common_utils_library.registhentication.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
@Slf4j
public class IDGeneratorUtil {



 public String generateRefId(){
  return generateId("REF-",9);
 }
 public String generateTempId(){
  return RandomStringUtils.randomAlphanumeric(25);
 }

 public String generateTempId(String prefix){
  return prefix.concat(RandomStringUtils.randomAlphanumeric(25));
 }

 public String generateProfileId(){
  return generateId("P-",7);
 }
 public String generateProfileId(String prefix){
  return generateId(prefix,9);
 }


 private String generateId(String prefix, int length){
  return prefix.concat(RandomStringUtils.randomAlphanumeric(length).toUpperCase());
 }


}
