package com.exceptions;

public class ApplianceReadException extends Exception{
    public ApplianceReadException(String line) {
        super("Unable to get Appliance from line: " + line);
    }
}
