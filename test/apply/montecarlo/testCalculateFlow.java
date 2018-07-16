package apply.montecarlo;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class testCalculateFlow {

    @Test
    public void testGetJsonData() throws IOException {
        CalculateFlow cf = new CalculateFlow();
        cf.getData();
        for(Map.Entry entry: cf.deleteRoute.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }


}
