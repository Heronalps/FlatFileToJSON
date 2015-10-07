package com.expicient.integration;

import java.io.File;

import org.kohsuke.args4j.Option;

public class FlatFileParserOptions {
	@Option(name="-s", required=true, usage="full path of schema file")
    private File schemalocation;

    @Option(name="-d", required=true, usage="full path of flat file")
    private File datafile;
    
    @Option(name="-o", required=true, usage="full path of the output JSON file")
    private File outputdirectory;

    @Option(name="-f", usage="file name of the output JSON")
    private String outputfilename = "output.json";

    @Option(name="-t",usage="true -length based, false - endpoint based")
    private boolean schemaType = true;
    
    @Option(name="-debug",usage="Log details")
    private boolean debug = true;
    
    @Option(name="-prettyprint",usage="Print formatted JSON")
    private boolean prettyPrint = true;

	public FlatFileParserOptions() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the schemalocation
	 */
	public File getSchemalocation() {
		return schemalocation;
	}

	/**
	 * @return the datafile
	 */
	public File getDatafile() {
		return datafile;
	}

	/**
	 * @return the outputdirectory
	 */
	public File getOutputdirectory() {
		return outputdirectory;
	}

	/**
	 * @return the outputfilename
	 */
	public String getOutputfilename() {
		return outputfilename;
	}

	/**
	 * @return the schemaType
	 */
	public boolean isSchemaType() {
		return schemaType;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the prettyPrint
	 */
	public boolean isPrettyPrint() {
		return prettyPrint;
	}
}
