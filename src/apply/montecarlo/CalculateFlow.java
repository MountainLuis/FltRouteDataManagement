package apply.montecarlo;

import bada.bean.FixPt;
import bada.bean.Route;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateFlow {
    Map<String, Integer> ptFlows = null;
    Map<String, String[]> linkedRoute = null;
    Map<String, Integer> deleteRoute = null;
    Map<String,Route> routes = null;

    public CalculateFlow() throws IOException {
        getData();
        initFlow();
    }
    /**
     * 初始化流量Map
     */
    public void initFlow() {
        ptFlows = new HashMap<>();
        for(String pt : linkedRoute.keySet()) {
            int initFlow = linkedRoute.get(pt).length * 30;
            ptFlows.put(pt, initFlow);
        }
    }

    public  void  getData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = CalculateFlow.class.getResourceAsStream("/sample/data/linkedRoute.json");
        linkedRoute = mapper.readValue(is, new TypeReference<Map<String, String[]>>(){});

        is = CalculateFlow.class.getResourceAsStream("/sample/data/deleteRoute.json");
        deleteRoute = mapper.readValue(is, new TypeReference<Map<String, Integer>>(){});

        routes = RouteManagement.getRoute(RouteManagement.getRouteName());
    }
    public void calFlow() {
        for(String route :deleteRoute.keySet()) {
            List<FixPt> pts = routes.get(route).enRoute;
            for (FixPt pt : pts) {
                ptFlows.put(pt.getID(), (ptFlows.get(pt.getID()) - deleteRoute.get(route)));
            }

        }
    }
    public static void main(String[] args) throws IOException {
        CalculateFlow cf = new CalculateFlow();
        int i = 0;
        for(String pt : cf.ptFlows.keySet()) {
            if(i %10 == 0) {
                System.out.println();
            }
            System.out.print(pt + ":" + cf.ptFlows.get(pt) + " \t");
            i++;
        }
        cf.calFlow();
        System.out.println("=====================================================");
        int j = 0;
        for(String pt : cf.ptFlows.keySet()) {
            if(j %10 == 0) {
                System.out.println();
            }
            System.out.print(pt + ":" + cf.ptFlows.get(pt) + " \t");
            j++;
        }
    }

}
