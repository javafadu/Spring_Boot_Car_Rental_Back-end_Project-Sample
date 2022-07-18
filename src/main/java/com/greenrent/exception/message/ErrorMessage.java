package com.greenrent.exception.message;

public class ErrorMessage {
    public final static String RESOURCE_NOT_FOUND_MESSAGE="Resource with id %d not found";
    public final static String ROLE_NOT_FOUND_MESSAGE="Role with name %s not found";

    public final static String NAME_NOT_FOUND="The name with name %s is not found";

    public final static String USER_NOT_FOUND="The username with email %s is not found";

    public final static String EMAIL_ALREADY_EXIST="Email already exists : %s";

    public final static String NOT_PERMITTED_METHOD_MESSAGE="You do not have any permission to change this value";

    public final static String PASSWORD_NOT_MATCHED="Your password are not matched";

    public final static String IMAGE_NOT_FOUND_MESSAGE="ImageFile with id %s not found";

    public final static String RESERVATION_TIME_INCORRECT_MESSAGE="Reservation pick up time or drop off time not correct";

    public final static String CAR_NOT_AVAILABLE_MESSAGE="Car is not available for selected times";

    public final static String EXCEL_REPORT_CREATION_ERROR_MESSAGE="Error Occured While Generating excel report";

    public final static String CAR_USED_BY_RESERVATION_MESSAGE="Car could not be deleted. Car is used by a reservation";

    public final static String USER_USED_BY_RESERVATION_MESSAGE="User could not be deleted. User is used by a reservation";

}
