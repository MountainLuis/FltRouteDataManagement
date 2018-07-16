package apply.util;

import code.apply.bean.FlightPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTransform {

    public static int timeString2Int(String appearTime) {
        int time = 0;
        String hours = appearTime.substring(8, 10);
        int hour2seconds = Integer.parseInt(hours) * 3600;
        String minutes = appearTime.substring(10, 12);
        int minute2seconds = Integer.parseInt(minutes) * 60;
        String seconds = appearTime.substring(12);
        time = hour2seconds + minute2seconds + Integer.parseInt(seconds);
        return time;
    }
    public static double latLongString2Double(String lonLat) {
        double result = 0;
        String sec = lonLat.substring(lonLat.length()-2);
        String min = lonLat.substring(lonLat.length() - 4, lonLat.length() - 2);
        int seconds = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
        result = Double.parseDouble(lonLat.substring(0, lonLat.length() - 4)) + seconds / 3600.0;
        return result;
    }
    public static List<FlightPlan> getAcftIDFromFltPlanMap(Map<String, List<FlightPlan>> allPlans) {
        List<FlightPlan> allFltIDs = new ArrayList<>();
        for(String route : allPlans.keySet()) {
            for(FlightPlan plan : allPlans.get(route)){
                allFltIDs.add(plan);
            }
        }
        return allFltIDs;
    }
    public static void map2json(Map<String, List<String>> linkedRoutes) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            mapper.writeValue(new FileWriter("linkedRoute.json"), linkedRoutes);
            jsonStr = mapper.writeValueAsString(linkedRoutes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void map2jsons(Map map, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new FileWriter(fileName), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void list2json(List list){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new FileWriter("conflicts.json"), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
