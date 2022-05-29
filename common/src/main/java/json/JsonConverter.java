package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import interaction.Request;
import interaction.Response;
import interaction.User;
import utils.animation.Route;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public static Response desResponse(String s) {
        Response output = null;
        try {
            output = new ObjectMapper().readValue(s, Response.class);
        } catch (JsonProcessingException e) {
            System.out.println("краказябра хи хи ха ха чин чань чунь (десер response) " + e.getMessage());
            e.printStackTrace();
        }
        return output;
    }


//    public static String serializeRoute(List<Route> route){
//        String output = "";
//        try{
//            output = new ObjectMapper().writeValueAsString(route);
//        } catch (JsonProcessingException e) {
//            System.out.println("беды с сериализацией route " + e.getMessage());
//        }
//
//        return output + "\0";
//    }

//    public static ArrayList<Route> deserializeRoute(String s){
//        ArrayList<Route> output = null;
//
//        try {
//            output = new ObjectMapper().readValue(s, ArrayList.class);
//
//        } catch (JsonProcessingException e) {
//            System.out.println("краказябра хи хи ха ха чин чань чунь (десер реквеста)" + e.getMessage());
//        }
//        return output;
//    }

//    public static Route restoreRoute(LinkedHashMap s){
//        Route output = null;
//        try {
//            output = new ObjectMapper().readValue((DataInput) s, Route.class);
//
//        } catch (JsonProcessingException e) {
//            System.out.println("краказябра хи хи ха ха чин чань чунь (десер реквеста)" + e.getMessage());
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
//        return output;
//    }
}
