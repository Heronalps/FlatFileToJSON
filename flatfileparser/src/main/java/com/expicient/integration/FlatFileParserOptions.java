package com.expicient.integration;

import java.io.File;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.EnumOptionHandler;


public class FlatFileParserOptions {
	public static enum Schematype {LENGTH, ENDPOINT};

	@Option(name="-s", required=true, usage="full path of schema file", metaVar = "Schema CSV")
    private File schemalocation;

    @Option(name="-d", required=true, usage="full path of flat file", metaVar = "Flat File TXT")
    private File datafile;

    @Option(name="-o", required=true, usage="full path of the output JSON file", metaVar = "DIR")
    private File outputdirectory;

    @Option(name="-f", usage="file name of the output JSON", metaVar = ".JSON")
    private String outputfilename = "output.json";

    @Option(name="-t",usage="Length-based or Endpoint-based")
    private Schematype schemaType = Schematype.LENGTH;
    
    @Option(name="-debug",usage="Log details", hidden = true)
    private boolean debug = false;
    
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
	public Schematype getSchemaType(){
		return schemaType;
	}

	public boolean isSchemaType() {
		if (this.getSchemaType().equals(Schematype.ENDPOINT)){
			return false;
		}else{
			return true;
		}
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
