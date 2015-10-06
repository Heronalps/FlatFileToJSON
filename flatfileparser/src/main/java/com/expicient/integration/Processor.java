package com.expicient.integration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.expicient.integration.schema.FlatFileSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Processor {
	public static void main(String[] args) {
		// Process command line inputs
		String schemaFileLocation = "";
		String datafileLocation = "";
		String outputDirLocation = "";
		boolean flag_lengthorEnd = true;
		String outputfilename = "output.json";
		boolean debug = false;

		if (args.length < 3) {
			printMessage();
			return;
		} else if (args.length == 4) {
			flag_lengthorEnd = args[3].toLowerCase().trim().equals("true") ? true
					: false;
		} else {
			flag_lengthorEnd = args[3].toLowerCase().trim().equals("true") ? true
					: false;
			outputfilename = args[4].toLowerCase().trim();
		}

		// Process command line inputs
		schemaFileLocation = args[0];
		datafileLocation = args[1];
		outputDirLocation = args[2];

		// Validate the input and throw errors

		if (!isValid(schemaFileLocation, true)) {
			System.out.println("Please validate the path of schema file!");
			return;
		}

		if (!isValid(datafileLocation, true)) {
			System.out.println("Please validate the path of flat file!");
			return;
		}

		if (!isValid(outputDirLocation, false)) {
			System.out.println("Please validate the path of output JSON!");
			return;
		}

		File schemaFile = new File(schemaFileLocation);
		CSVParser parser;
		ArrayList<LinkedHashMap<String, String>> outputDataStructure = new ArrayList<LinkedHashMap<String, String>>();
		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<FlatFileSchema>();
		try {
			parser = CSVParser.parse(schemaFile, Charset.defaultCharset(),
					CSVFormat.DEFAULT);
			// Parse the schema and load it into an array list
			flatFileSchema = parseSchema(flag_lengthorEnd, parser);
			// Start reading the data file and use schema structure to convert
			// it
			Scanner scanner;
			scanner = new Scanner(new File(datafileLocation));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0) {
					parseRecordAndFillData(flatFileSchema, outputDataStructure,
							line);
				}
			}
			scanner.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Unable to parse the input schema CSV: "
					+ fnfe.getMessage());
			if (debug)
				fnfe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Unable to parse the input schema CSV: "
					+ ioe.getMessage());
			if (debug)
				ioe.printStackTrace();
		}

		// Generate output in json
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		try {
			FileWriter writer;
			writer = new FileWriter(outputDirLocation + "/" + outputfilename);
			writer.write(gson.toJson(outputDataStructure));
			writer.close();
		} catch (IOException e) {
			System.out.println("Unable to write input data file:"
					+ e.getMessage());
			if (debug)
				e.printStackTrace();
		}
	}

	private static void parseRecordAndFillData(
			ArrayList<FlatFileSchema> flatFileSchema,
			ArrayList<LinkedHashMap<String, String>> outputDataStructure,
			String line) {

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		for (FlatFileSchema schema : flatFileSchema) {
			data.put(
					schema.getFieldName(),
					line.substring(schema.getStartPosition(),
							schema.getStartPosition() + schema.getLength()));
		}
		outputDataStructure.add(data);
	}

	public static boolean isValid(String file, boolean isFile) {
		File filepath = new File(file);
		if (isFile) {
			if (filepath.exists() && filepath.isFile()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (filepath.exists() && filepath.isDirectory()) {
				return true;
			} else {
				return false;
			}
		}
	}

	private static void printMessage() {
		StringBuilder message = new StringBuilder();
		message.append("Usage: java -jar flatfileparser.jar schema={schemalocation} datafile={datafile} outputdir={outputdirectory} outputfilename={outputfilename} schematype={schematype} debug={debug}\n");
		message.append("schemalocation: full path of the file that contain the schema\n");
		message.append("datafile: full path of the file that contain the flat file data\n");
		message.append("outputdirectory: full path for the output file\n");
		message.append("outputfilename {optional} (default: output.json): file name for the output results\n");
		message.append("schematype {optional} (true|false) (default: true): true - length based, false - end point\n");
		System.out.println(message.toString());
	}

	public static ArrayList<FlatFileSchema> parseSchema(boolean flag,
			CSVParser parser) {

		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<FlatFileSchema>();
		if (flag) {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer
						.parseInt(csvRecord.get(1)), Integer.parseInt(csvRecord
						.get(2))));
			}
		} else {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer
						.parseInt(csvRecord.get(1)), (Integer
						.parseInt(csvRecord.get(2)) - Integer
						.parseInt(csvRecord.get(1)))));
				// Length = Endpoint - Startpoint
			}
		}
		return flatFileSchema;
	}
}
