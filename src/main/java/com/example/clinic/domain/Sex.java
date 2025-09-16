package com.example.clinic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Sex { MALE, FEMALE, OTHER, UNKNOWN;
	
	  @JsonCreator
	  public static Sex from(String v) {
	    if (v == null) return null;
	    return switch (v.trim().toUpperCase()) {
	      case "MALE", "M" -> MALE;
	      case "FEMALE","F"-> FEMALE;
	      case "OTHER"     -> OTHER;
	      case "UNKNOWN","U"-> UNKNOWN;
	      default -> throw new IllegalArgumentException("Unsupported sex: " + v);
	    };
	  }
  }
