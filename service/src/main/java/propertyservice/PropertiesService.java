package propertyservice;

import exceptions.ExceptionCode;
import exceptions.CustomException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {
    private final String inputFile;
    private final Properties prop;

    public PropertiesService(String inputFile) {
        this.inputFile = inputFile;
        this.prop = new Properties();
    }

    public String loadProperty(String propertyType) {
        try (InputStream input = new FileInputStream(inputFile)) {
            prop.load(input);
            return prop.getProperty(propertyType);
        } catch (Exception e) {
            throw new CustomException("PROPERTIES SERVICE ERROR", ExceptionCode.PROPERTIES_SERVICE);
        }
    }
}
