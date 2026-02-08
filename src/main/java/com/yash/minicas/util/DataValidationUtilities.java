package com.yash.minicas.util;

import com.yash.minicas.entity.Customer;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DataValidationUtilities {
    private static final Logger logger = LoggerUtility.getLogger(DataValidationUtilities.class);

    private DataValidationUtilities() {
    }

    public static boolean checkDataType(Object field, String type) {
        if (field == null || type == null || type.isEmpty()) {
            return false;
        }

        return switch (type.toLowerCase()) {
            case "numeric" -> (field instanceof Number) || field.toString().matches("\\d+");
            case "alpha" -> field.toString().matches("[a-zA-Z ]+");
            case "alphanumeric" -> field.toString().matches("[a-zA-Z0-9 ]+");
            case "boolean" ->
                    field instanceof Boolean || field.toString().equalsIgnoreCase("true") || field.toString().equalsIgnoreCase("false");
            default -> type.equalsIgnoreCase(field.getClass().getSimpleName());
        };
    }

    public static boolean checkDataLength(Object field, int minLength, int maxLength) {
        if (minLength < 0 || maxLength < 0 || field == null) {
            return false;
        }

        int length = field.toString().length();
        return length >= minLength && length <= maxLength;
    }

    public static boolean checkSpecialCharacters(Object field, String specialCharacters) {
        if (field == null || specialCharacters == null) {
            return false;
        }

        String regex = ".*[" + Pattern.quote(specialCharacters) + "].*";
        return !field.toString().matches(regex);
    }

    public static boolean checkDomainValue(Object field, List<String> domainValues) {
        if (field == null || domainValues == null || domainValues.isEmpty()) {
            return false;
        }

        for (String domainValue : domainValues) {
            if (field.toString().equalsIgnoreCase(domainValue)) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkDateFormat(String dateString, String formatString) {
        if (dateString == null || dateString.isEmpty() || formatString == null || formatString.isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatString);
            formatter.setLenient(false);
            Date parsedDate = formatter.parse(dateString);
            return formatter.format(parsedDate).equals(dateString);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean checkEmail(String email) {
        if (email == null || email.isEmpty() || email.contains(" ")) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

        return Pattern.matches(emailRegex, email);
    }

    public static boolean customerFieldValidator(String fullName, String gender, LocalDate dateOfBirth, String contactNumber, String nationality, String salutation, String address, String qualificationType) {
        if (fullName == null || fullName.isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean loanApplicationFieldValidator(double loanAmount, double rate, int tenure, String productType, String product, String scheme, String tenureIn, Customer customer) {
        if (loanAmount <= 0 || rate <= 0 || tenure <= 0) {
            return false;
        }

        return true;
    }

    public static int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.error("{}", e.getMessage());
        }

        return defaultValue;
    }

    public static double parseDouble(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            logger.error("{}", e.getMessage());
        }

        return defaultValue;
    }
}
