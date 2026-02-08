package com.yash.minicas;

import com.yash.minicas.util.LoggerUtility;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LoggerUtility.getLogger(Main.class);

    private Main() { }

    public static void main(String[] args) {
        logger.info("Application Started...");
    }
}
