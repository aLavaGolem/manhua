package com.manhua;

import org.apache.log4j.*;

public class Test {
    private static Logger logger = Logger.getLogger(Test.class);

    public static void main(String[] args) {
        // debug级别的信息  
        logger.debug("This is debug message.");  
        // info级别的信息  
        logger.info("This is info message.");  
        // error级别的信息  
        logger.error("This is error message."); 
    }
}