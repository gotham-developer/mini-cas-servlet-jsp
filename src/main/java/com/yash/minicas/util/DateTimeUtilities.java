package com.yash.minicas.util;

import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;

public class DateTimeUtilities {
    private static final Logger logger = LoggerUtility.getLogger(DateTimeUtilities.class);

    private DateTimeUtilities() {
    }

    public static int calculateAge(LocalDate from, LocalDate till) {
        if (from == null || till == null) {
            logger.error("Illegal Input, from={}, till={}. Dates cannot be null.", from, till);
            return 0;
        }
        if (till.isBefore(from)) {
            logger.error("Wrong Input, from={}, till={}. From must be before Till.", from, till);
            return 0;
        }

        return Period.between(from, till).getYears();

    }
}
