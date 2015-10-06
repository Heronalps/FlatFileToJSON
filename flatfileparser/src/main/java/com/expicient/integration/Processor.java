package com.expicient.integration;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedHashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.expicient.integration.schema.FlatFileSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Processor {
	public static void main(String[] args) throws Exception {
		try {

			if(args.length < 4) {
				printMessage();
				return;
			} else {

			}
			// Process command line inputs
			String schemaFileLocation = args[0];
			String datafileLocation = args[1];
			String outputDatafileLocation = args[2];
			String flag_lengthorEnd = args[3];
			String delimiter = args[4];

			// Validate the input and throw errors

			if (!isValid(schemaFileLocation, true)) {
				System.out.println("Please validate the path of schema file!");
				return;
			}

			if (!isValid(datafileLocation, true)) {
				System.out.println("Please validate the path of flat file!");
				return;
			}

			if (!isValid(outputDatafileLocation, false)) {
				System.out.println("Please validate the path of output JSON!");
				return;
			}

			if (!flag_lengthorEnd.equals("length") && !flag_lengthorEnd.equals("endpoint")) {
				System.out.println("Please enter either length or endpoint!");
				return;
			}

			if (!isDelimiterValid(delimiter)) {
				System.out.println("Please specify one special symbol as delimiter!");
				return;
			}


			File schemaFile = new File(schemaFileLocation);

			char dm = delimiter.charAt(0);
			CSVFormat fmt = CSVFormat.RFC4180.withDelimiter(dm);
			CSVParser parser = CSVParser.parse(schemaFile, Charset.defaultCharset(), fmt);

			// Parse the schema and load it into an array list
			ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<FlatFileSchema>();

			flatFileSchema = parseSchema(flag_lengthorEnd, parser);

			// Start reading the data file and use schema structure to convert it
			ArrayList<LinkedHashMap<String, String>> outputDataStructure = new ArrayList<LinkedHashMap<String, String>>();
			Scanner scanner = new Scanner(new File(datafileLocation));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0) {
					parseRecordAndFillData(flatFileSchema, outputDataStructure,
							line);
				}
			}
			scanner.close();

			// Generate output in json
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			FileWriter writer = new FileWriter(outputDatafileLocation);
			writer.write(gson.toJson(outputDataStructure));
			writer.close();

		} catch (IOException | IllegalArgumentException e) {

			e.printStackTrace();
		}
	}

	private static void parseRecordAndFillData(ArrayList<FlatFileSchema> flatFileSchema,
											   ArrayList<LinkedHashMap<String, String>> outputDataStructure,
											   String line) {

		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		for (FlatFileSchema schema : flatFileSchema) {
			data.put(schema.getFieldName(),
					line.substring(schema.getStartPosition(),
							schema.getStartPosition() + schema.getLength()));
		}
		outputDataStructure.add(data);
	}

	public static boolean isValid(String file, boolean isFile) throws IOException {
		File filepath = new File(file);
		if(isFile) {
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
		message.append("Usage: java -jar flatfileparser.jar {schemalocation} {datafile} {outputlocation} {delimeter} {schematype}\n");
		message.append("schemalocation: full path of the file that contain the schema\n");
		System.out.println(message.toString());
	}

	public static boolean isDelimiterValid(String delimiter) {

		if (delimiter.length() == 1 && delimiter.matches("[^A-Z0-9a-z]")) {

			return true;

		} else {
			return false;
		}
	}

	public static ArrayList<FlatFileSchema> parseSchema(String flag, CSVParser parser) {

		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<FlatFileSchema>();
		if (flag.equals("length")) {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer.parseInt(csvRecord.get(1)),
						Integer.parseInt(csvRecord.get(2))));
			}
		} else {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer.parseInt(csvRecord.get(1)),
						(Integer.parseInt(csvRecord.get(2)) - Integer.parseInt(csvRecord.get(1)))));
				//Length = Endpoint - Startpoint
			}
		}
		return flatFileSchema;
	}
}
