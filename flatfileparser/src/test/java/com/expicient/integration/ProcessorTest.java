/**
 *
 */
package com.expicient.integration;

import com.expicient.integration.schema.FlatFileSchema;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {

	@Test
	public void isValidFilePositiveTest() {
		File file = new File(this.getClass().getClassLoader()
				.getResource("DataFile.txt").getPath());
		boolean value = Processor.isValid(file, true);
		assertEquals(value, true);
	}

	@Test
	public void isValidFileNegativeTest() {
		File file = new File("data");
		boolean value = Processor.isValid(file, true);
		assertEquals(value, false);
	}

	@Test
	public void isValidDirectoryPositiveTest() {
		File file = new File(this.getClass().getClassLoader().getResource("")
				.getPath());
		boolean value = Processor.isValid(file, false);
		assertEquals(value, true);
	}

	@Test
	public void isValidDirectoryNegativeTest() {
		File file = new File("../abc");
		boolean value = Processor.isValid(file, false);
		assertEquals(value, false);
	}

	@Test
	public void parseRecordAndFillDataTest() {
		ArrayList<FlatFileSchema> ffs = new ArrayList<>();
		ArrayList<LinkedHashMap<String, String>> mapList = new ArrayList<>();
		String line = "srctestjava";

		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("first", "src");
		map.put("second", "test");
		map.put("third", "java");

		ffs.add(new FlatFileSchema("first", 0, 3));
		ffs.add(new FlatFileSchema("second", 3, 4));
		ffs.add(new FlatFileSchema("third", 7, 4));

		Processor.parseRecordAndFillData(ffs, mapList, line);
		assertEquals(mapList.get(0), map);
	}

	@Test
	public void parseSchemaLengthTest() throws IOException {
		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<>();

		File schemaFile = new File(this.getClass().getClassLoader()
				.getResource("File_Schema.csv").getPath());
		CSVParser parser = CSVParser.parse(schemaFile,
				Charset.defaultCharset(), CSVFormat.DEFAULT);
		flatFileSchema = Processor.parseSchema(true, parser);
		assertEquals(flatFileSchema.get(0).getFieldName(), "run-type");
		assertEquals(flatFileSchema.get(0).getStartPosition(), 0);
		assertEquals(flatFileSchema.get(0).getLength(), 7);
	}

	@Test
	public void parseSchemaEndpointTest() throws IOException {
		ArrayList<FlatFileSchema> flatFileSchema = new ArrayList<>();

		File schemaFile = new File(this.getClass().getClassLoader()
				.getResource("File_Schema_End.csv").getPath());
		CSVParser parser = CSVParser.parse(schemaFile,
				Charset.defaultCharset(), CSVFormat.DEFAULT);
		flatFileSchema = Processor.parseSchema(false, parser);
		assertEquals(flatFileSchema.get(0).getFieldName(), "run-type");
		assertEquals(flatFileSchema.get(0).getStartPosition(), 0);
		assertEquals(flatFileSchema.get(0).getLength(), 7);
	}
}
