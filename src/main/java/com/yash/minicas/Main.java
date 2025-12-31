package com.yash.minicas;

import com.yash.minicas.util.LoggerUtility;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LoggerUtility.getLogger(Main.class);

    private Main() { }

    static void main() {
        logger.info("Application Started...");
    }
}
