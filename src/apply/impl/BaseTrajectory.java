package apply.impl;

import apply.util.MysqlHelper;
import bada.bean.AcftPerformance;
import bada.bean.AcftState;
import bada.bean.Route;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class BaseTrajectory {
//    Map<String, AcftPerformance> acftPerformances = new HashMap<>();
//    List<Route> routes;
//
//    public BaseTrajectory() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        InputStream is = BaseTrajectory.class.getResourceAsStream("/sample/data/acft_performance.json");
//        AcftPerformance[] performances = mapper.readValue(is, AcftPerformance[].class);
//        for(AcftPerformance performance: performances){
//            acftPerformances.put(performance.ACFT_ID, performance);
//        }
//        is = BaseTrajectory.class.getResourceAsStream("/sample/data/route.json");
//        routes = mapper.readValue(is, new TypeReference<List<Route>>(){});
//    }
//
//    void output(String filename, List<AcftState> list) throws IOException {
//        FileWriter fw = new FileWriter(filename);
//        for(AcftState state: list){
//            fw.write(String.valueOf(state.longitude));
//            fw.write(",");
//            fw.write(String.valueOf(state.latitude));
//            fw.write("\n");
//        }
//        fw.close();
//    }
//    public void insertData(List<AcftState> list, String fltNo) {
//        StringBuffer suffix = new StringBuffer();
//        for(int i = 0; i < list.size(); i++) {
//            AcftState state = list.get(i);
//            suffix.append("('"
//                    + fltNo + "',"
//                    + state.pAltitude + ","
//                    + state.heading + ","
//                    + state.longitude + ","
//                    + state.latitude + ","
//                    + state.CAS + ","
//                    + state.TAS + ","
//                    + state.time
//                    + "),");
//        }
//        MysqlHelper.insert(suffix);
//        System.out.println("Insert " + fltNo + "'s tracks into table.");
//    }
//}
