package com.osayijoy.common_utils_library.commonUtils.util;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.helper.response.ApiError;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-02(Tue)-2023
 */

@UtilityClass
@Slf4j
public class CSVUtil {
    public static <T> void generateCsv(List<T> data, String[] headers, Map<String, Function<T, String>> fieldMappings, HttpServletResponse response, String fileName)  {
        if (data.isEmpty() || fieldMappings.isEmpty() || headers.length != fieldMappings.size())
            throw new ZeusRuntimeException(HttpStatus.BAD_REQUEST,new ApiError("data, or headers missing, unable to generate csv"));
        try {
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(fileName.concat(".csv")));

            // Create the CSV writer
            CsvWriterSettings settings = new CsvWriterSettings();
            settings.setHeaders(headers);
            settings.setQuoteAllFields(true);
            settings.setQuoteEscapingEnabled(true);
            CsvWriter writer = new CsvWriter(new OutputStreamWriter(response.getOutputStream()), settings);

            writer.writeHeaders();



            // Add the rows to the CSV output
            for (T obj : data) {
                String[] row = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    Function<T, String> fieldMapping = fieldMappings.get(headers[i]);
                    String value = fieldMapping.apply(obj);
                    row[i] = (value != null) ? value : "";
                }
                writer.writeRow(row);
            }

            // Close the CSV writer
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("csv generation failed because : {}",e.getMessage());
            throw new ZeusRuntimeException(new ApiError("unable to generate csv"));
        }
    }

}
