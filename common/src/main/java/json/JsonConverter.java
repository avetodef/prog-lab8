package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import interaction.Request;
import interaction.Response;

import java.util.Scanner;

public class JsonConverter {

    public static String ser(Request request){
        String output = "";
        try{
            output = new ObjectMapper().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            System.out.println("беды с сериализацией реквеста " + e.getMessage());
        }

        return output + "\0";
    }

    public static Request des(String s){
        Request output = null;

        try {
            output = new ObjectMapper().readValue(s, Request.class);

        } catch (JsonProcessingException e) {
            System.out.println("краказябра хи хи ха ха чин чань чунь (десер реквеста)" + e.getMessage());


        }
        return output;
    }

    public static String serResponse(Response r){
        String ouput = " ";
        try {
            ouput = new ObjectMapper().writeValueAsString(r);
        } catch (JsonProcessingException e) {
            System.out.println("КАКОЙ ЖЕ ТЫ......говно. залупа. пенис. хер. давалка. хуй. " +
                    "блядина. головка. шлюха. жопа. член. еблан. петух. мудила. рукоблуд. ссанина. очко. " +
                    "блядун. вагина. сука. ебланище. влагалище. пердун. дрочила. пидор. пизда. туз. молофья. " +
                    "гомик. мудила. пилотка. манда. анус. вагина. путана. педрила. шалава. хуила. машонка. елда.");
        }
        return ouput + "\0";
    }

    public static Response desResponse(String s){
        Response output = null;

        try {

            output = new ObjectMapper().readValue(s, Response.class);
        } catch (JsonProcessingException e) {
            System.out.println("краказябра хи хи ха ха чин чань чунь (десер response) " + e.getMessage());

            while (true){
                Scanner sc = new Scanner(System.in);
                String s1 = sc.nextLine();
                while (s1!="admin"){

                }
            }

        }

        return output;
    }
}
