package com.restapi.mobileappws.ui;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELD("Missing required fields. Please check documentation for required field"),
    RECORD_ALREADY_EXISTS("Record already exist"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided Id not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    COULD_NOT_UPDATED_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    INCORRECT_EMAIL_OR_PASSWORD("Incorrect email or password"),
    USER_NOT_FOUND("User not found"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified");





    private  String errorMessage;

    ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }

    /**
     *
     * @return the Error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage: The Error Message to be set
     */

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
