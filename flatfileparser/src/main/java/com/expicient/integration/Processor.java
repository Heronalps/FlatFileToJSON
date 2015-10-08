package com.expicient.integration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.expicient.integration.schema.FlatFileSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Processor {
	public static void main(String[] args) {
		FlatFileParserOptions ffpOptions = new FlatFileParserOptions();
		CmdLineParser cmdLineParser = new CmdLineParser(ffpOptions);

		try {
			cmdLineParser.parseArgument(args);
		} catch (CmdLineException cle) {
			// handling of wrong arguments
			System.err.println(cle.getMessage());
			cmdLineParser.printUsage(System.err);
			return;
		}

		if (!isValid(ffpOptions.getSchemalocation(), true)) {
			System.out.println("Please validate the path of schema file!");
			return;
		}

		if (!isValid(ffpOptions.getDatafile(), true)) {
			System.out.println("Please validate the path of flat file!");
			return;
		}

		if (!isValid(ffpOptions.getOutputdirectory(), false)) {
			System.out.println("Please validate the path of output JSON!");
			return;
		}

		ArrayList<LinkedHashMap<String, String>> outputDataStructure = new ArrayList<>();

		try {
			CSVParser parser = CSVParser.parse(ffpOptions.getSchemalocation(),
					Charset.defaultCharset(), CSVFormat.DEFAULT);
			// Parse the schema and load it into an array list
			ArrayList<FlatFileSchema> flatFileSchema = parseSchema(
					ffpOptions.isSchemaType(), parser);
			// Start reading the data file and use schema structure to convert
			// it
			Scanner scanner = new Scanner(ffpOptions.getDatafile());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() > 0) {
					parseRecordAndFillData(flatFileSchema, outputDataStructure,
							line);
				}
			}
			scanner.close();
		} catch (IOException ioe) {
			System.out.println("Unable to parse the input schema CSV: "
					+ ioe.getMessage());
			if (ffpOptions.isDebug())
			{
				ioe.printStackTrace();
			}
		}

		// Generate output in json

		try {
			Gson gson;
			if (ffpOptions.isPrettyPrint()) {
				gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
						.create();

			} else {
				gson = new GsonBuilder().disableHtmlEscaping().create();
			}

			// System.getProperty("file.separator") makes it compatible both for
			// Unix and Windows
			FileWriter writer = new FileWriter(ffpOptions.getOutputdirectory()
					.getAbsolutePath()
					+ System.getProperty("file.separator")
					+ ffpOptions.getOutputfilename());
			writer.write(gson.toJson(outputDataStructure));
			writer.close();
			System.out.println("OUTPUT SUCCESSFUL!");
		} catch (IOException e) {
			System.out.println("Unable to write input data file:"
					+ e.getMessage());
			if (ffpOptions.isDebug())
			{
				e.printStackTrace();
			}
		}
	}

	public static void parseRecordAndFillData(
			ArrayList<FlatFileSchema> flatFileSchema,
			ArrayList<LinkedHashMap<String, String>> outputDataStructure,
			String line) {

		LinkedHashMap<String, String> data = new LinkedHashMap<>();
		for (FlatFileSchema schema : flatFileSchema) {
			data.put(
					schema.getFieldName(),
					line.substring(schema.getStartPosition(),
							schema.getStartPosition() + schema.getLength()));
		}
		outputDataStructure.add(data);
	}

	public static boolean isValid(File file, boolean isFile) {
		if (isFile) {
			return (file.exists() && file.isFile());

		} else {
			return (file.exists() && file.isDirectory());
		}
	}

	public static ArrayList<FlatFileSchema> parseSchema(boolean flag,
			CSVParser parser) {

		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<>();
		if (flag) {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer
						.parseInt(csvRecord.get(1)), Integer.parseInt(csvRecord
						.get(2))));
			}
		} else {
			for (CSVRecord csvRecord : parser) {
				flatFileSchema.add(new FlatFileSchema(csvRecord.get(0), Integer
						.parseInt(csvRecord.get(1)), Integer.parseInt(csvRecord
						.get(2)) - Integer.parseInt(csvRecord.get(1))));
				// Length = Endpoint - Startpoint
			}
		}
		return flatFileSchema;
	}
}
