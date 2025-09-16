package com.example.clinic.service;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Audited {
String action();      // "READ","CREATE","UPDATE","DELETE"
String resource();    // "Patient","Encounter","Note" 等
}
