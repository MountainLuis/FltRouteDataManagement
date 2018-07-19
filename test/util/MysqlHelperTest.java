package util;

import dataManager.DataAccessObject;
import bean.FltPlan;
import dataDump.DumpTxtToMySql;
import bean.PointInfo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlHelperTest {

    @Test
    public void getConnection() {
        Connection onn = MysqlHelper.getConnection();
        System.out.println("Done");
    }

    @Test
    public void getResultSet() {
        String sql = "select * from fltpath";
        ResultSet rs = MysqlHelper.getResultSet(sql);
        try{
            System.out.println(rs.wasNull());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }
    @Test
    public void insertIntoMysql(){
        DumpTxtToMySql dttm = new DumpTxtToMySql();
        String filename = "D:\\test.txt";
        List<PointInfo> pList = dttm.getDataFromTxt(filename);
        System.out.println(pList.size());
        MysqlHelper.insertIntoMysql(pList);
        System.out.println("Done.");
    }

    @Test
    public void insertFltPathIntoMysql() {
        DataAccessObject grd = new DataAccessObject();
        String r = "A1";
        String start = "SUC";
        String end = "OSTAR";
        List<PointInfo> pList = grd.getSubPtSeq(r, start, end, 1);
        Map<String, List<PointInfo>> pathMap = new HashMap<>();
        pathMap.put("zsss-zbaa", pList);
        MysqlHelper.insertFltPathIntoMysql(pathMap);
        System.out.println("Done.");
    }

    @Test
    public void insertPlanTable() {
        String table = "plan20180601";
        FltPlan fltPlan = new FltPlan("CCA1233", "ZBAA", "ZSPD");
        List<FltPlan> plans = new ArrayList<>();
        plans.add(fltPlan);
        MysqlHelper.insertPlanTable(table, plans);
    }
    @Test
    public void createTable() {
        String table = "plan20180601";
        MysqlHelper.createTable(table);
    }
}