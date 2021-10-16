package converters;

import com.google.gson.*;
import exceptions.ExceptionCode;
import exceptions.CustomException;
import lombok.extern.log4j.Log4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j
public abstract class JsonConverter<T> {
    private final String jsonFileName;
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    LocalDate localDate = LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("dd/MM/uuuu"));
                    log.info(localDate.toString());
                    return localDate;
                }
            })
            .setPrettyPrinting()
            .create();

    public JsonConverter(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    // conversion from POJO to JSON
    public void toJson(final T element) {
        try (FileWriter fileWriter = new FileWriter(jsonFileName)) {
            if (element == null) {
                throw new CustomException("ELEMENT TO PARSE IS NULL", ExceptionCode.GSON);
            }
            gson.toJson(element, fileWriter);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), ExceptionCode.GSON);
        }
    }

    // conversion from JSON to POJO
    public Optional<T> fromJson() {
        try (FileReader fileReader = new FileReader(jsonFileName)) {
            return Optional.of(gson.fromJson(fileReader, type));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), ExceptionCode.GSON);
        }
    }
}
