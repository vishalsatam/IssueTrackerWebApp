package com.wt.issuetracker.filter;

public class CustomValidator {

	public static String sanitizeInputAlphaNumeric(String input){
        
        input = input.trim().replaceAll("[^\\dA-Za-z\\s-.,&_\'?\"@$]","").trim();
        return input;
	}
	
	 public static boolean validateAlphanumericName(String alphaNumeric)
	    {
	        if (!alphaNumeric.trim().isEmpty() && alphaNumeric.matches("^[a-zA-Z][a-zA-Z0-9.*()\"!@$, ]*$"))
	        {
	            return true;
	        }
	        return false;
	    }
	
}
