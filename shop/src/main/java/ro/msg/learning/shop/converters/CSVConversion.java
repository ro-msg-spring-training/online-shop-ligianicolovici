package ro.msg.learning.shop.converters;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVConversion<T> {
    private CsvMapper mapper = new CsvMapper();

    public List<T> fromCsv(Class<T> csvClass, InputStream csvData) throws IOException {
        CsvSchema schema = this.mapper.schemaFor(csvClass).withHeader();

        MappingIterator<T> it = this.mapper.readerFor(csvClass).with(schema)
                .readValues(csvData);

        return it.readAll();
    }

    public void toCsv(Class<T> csvClass, List<T> itemsList, OutputStream csvOutput) throws IOException {
        Field[] fields = csvClass.getDeclaredFields();
        CsvSchema schema = this.mapper.typedSchemaFor(csvClass);
        ObjectWriter objectWriter = this.mapper.writer(schema.withLineSeparator("\n"));
        List<String> headers = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        StringBuilder csvValue = new StringBuilder();
        csvValue.append(objectWriter.writeValueAsString(headers));
        for (T item : itemsList) {
            csvValue.append(objectWriter.writeValueAsString(item));
        }

        csvOutput.write(csvValue.toString().getBytes());
    }
}
