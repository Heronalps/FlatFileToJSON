package com.expicient.integration;

/**
 * Created by michael.zhang on 10/2/2015.
 */

import com.expicient.integration.Processor;
import java.io.IOException;


public class Test {

    public static void main(String[] args) {
        try {

            //String path = "/Users/michaelzhang/Dropbox/FlatfileToJson/data/File_Schema.csv";
            //System.out.println(Processor.isFilePathValid(path));

            String delimiter = ";";
            System.out.println(Processor.isDelimiterValid(delimiter));


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
