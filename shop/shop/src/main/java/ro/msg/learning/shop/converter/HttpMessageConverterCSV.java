package ro.msg.learning.shop.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.exceptions.CSVExporterException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HttpMessageConverterCSV extends AbstractGenericHttpMessageConverter {

    private final CSVConversion csvConverter;

    public HttpMessageConverterCSV() {
        super(new MediaType("text", "csv"));

        this.csvConverter = new CSVConversion();
    }


    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage httpOutputMessage) throws HttpMessageNotWritableException, IOException {

        List<Object> arrayList = new ArrayList<>();

        if (o instanceof List)
            arrayList = new ArrayList<>((ArrayList<Object>) o);
        else if (o instanceof LinkedHashMap) {
            throw new CSVExporterException("Couldn't export results as csv file");
        } else {
            arrayList = Collections.singletonList(o);
        }

        csvConverter.toCsv(arrayList.get(0).getClass(), arrayList, httpOutputMessage.getBody());
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException, IOException {
        return csvConverter.fromCsv(aClass, httpInputMessage.getBody());
    }

    @Override
    public Object read(Type type, Class aClass, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException, IOException {
        return readInternal(aClass, httpInputMessage);
    }
}
