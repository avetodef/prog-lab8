package parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import interaction.Request;
import interaction.Response;

import java.time.ZonedDateTime;

public class JsonConverter {

    private static final ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper() {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ZonedDateTime.class, new TimeDeserializer());

        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.registerModule(module);
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //defaultObjectMapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy : HH.mm.ss"));
        return defaultObjectMapper;
    }


    public static String ser(Request request) {
        String output = "";
        try {
            output = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            System.out.println("беды с сериализацией реквеста " + e.getMessage());
        }

        return output + "\0";
    }

    public static Request des(String s){
        Request output = null;

        try {
            output = objectMapper.readValue(s, Request.class);

        } catch (JsonProcessingException e) {
            System.out.println("краказябра хи хи ха ха чин чань чунь (десер реквеста)" + e.getMessage());
        }
        return output;
    }

    public static String serResponse(Response r){
        String ouput = " ";
        try {
            ouput = objectMapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            System.out.println("КАКОЙ ЖЕ ТЫ......говно. залупа. пенис. хер. давалка. хуй. " +
                    "блядина. головка. шлюха. жопа. член. еблан. петух. мудила. рукоблуд. ссанина. очко. " +
                    "блядун. вагина. сука. ебланище. влагалище. пердун. дрочила. пидор. пизда. туз. молофья. " +
                    "гомик. мудила. пилотка. манда. анус. вагина. путана. педрила. шалава. хуила. машонка. елда.");
        }
        return ouput + "\0";
    }

    public static Response desResponse(String s) {
        Response output = null;

        try {

            output = objectMapper.readValue(s, Response.class); //TODO теряет значения from и to здесь, причем теряет только икс и игрек.

        } catch (JsonProcessingException e) {
            System.out.println("десериализация response..." + e.getMessage());
            e.printStackTrace();
        }
        return output;
    }

}
