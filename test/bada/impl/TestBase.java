package bada.impl;

import code.bada.bean.AcftPerformance;
import code.bada.bean.AcftState;
import code.bada.bean.Route;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBase {

    Map<String, AcftPerformance> acftPerformances = new HashMap<>();
    List<Route> routes;

    public TestBase() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = TestBase.class.getResourceAsStream("/sample/data/acft_performance.json");
        AcftPerformance[] performances = mapper.readValue(is, AcftPerformance[].class);
        for(AcftPerformance performance: performances){
            acftPerformances.put(performance.ACFT_ID, performance);
        }
        is = TestBase.class.getResourceAsStream("/sample/data/route.json");
        routes = mapper.readValue(is, new TypeReference<List<Route>>(){});
    }

    void output(String filename, List<AcftState> list) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for(AcftState state: list){
            fw.write(String.valueOf(state.longitude));
            fw.write(",");
            fw.write(String.valueOf(state.latitude));
            fw.write("\n");
        }
        fw.close();
    }
    void output2 () {

    }


}