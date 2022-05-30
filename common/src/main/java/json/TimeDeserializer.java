package json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss");
        LocalDateTime date = LocalDateTime.parse(
                jsonParser.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));

        return date.atZone(ZoneId.systemDefault());
    }
}
