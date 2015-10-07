Flat File to JSON Processor Guideline
=====================================

* Author:  Pawandeep Singh, Michael Zhang
* Copyright:  Expicient, Inc.
* Version:  1.0.20151006

## Introduction

The initial idea of this Processor is to convert flat file of order management information into the format of JSON.
The processor takes flat file and schema in csv format as input and generates corresponding JSON file. The processor
is rigth now held as a commandline tool and, in the future versions, will be a hosted service for the entire team.


## Input / Output

* Input files:
    * Byte Stream Flat File (Compound data delimited by blank lines)
    * CSV Schema File (Three columns of field name, start point and length)
* Main class arguments:
    * Three files location:
        * [schemalocation] - _Schema file path_
        * [datafile] - _Data file path_
        * [outputdirectory] - _Output JSON path_
        * [outputfilename] {optional} - _Output JSON filename_
            * default: output.json
        * [schematype] {optional} - _Schema Type_
            * default: true
            * true-length based, false-endpoint based
        * [debug] {optional} - _Debug (optional)_
            * default: false
            * true-print exception backtrace, false-not print exception backtrace
* Output file:
    * JSON Data File

## Features in progress

* Capability for copebook in cobol format used in most mainstream programs
* Support for dynamic components in byte system
* To be a hosted service
