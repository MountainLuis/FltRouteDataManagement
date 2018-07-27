package dataManager;

import bean.FltPlan;
import bean.Point;
import bean.PointInfo;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * v2 : 将飞往国外航班的path在国境点出截止；
 * v3 :新增一个国境点LAMEN
 * v4 : 将计划中起落机场相同或起落机场是ZZZZ的记录丢弃；
 * v4_2：新增国境点 DOTMI
 * V4_3: 若计划的path有两段在国内，只截取前一段；
 * V4_4 : 将一些没有替换的重名点替换了；
 * V4_5 : 修改了飞往蒙古乌兰巴托航班的path
 * V4_6 : 修改了乌兰巴托飞越的航班path，删除了ZBUT起落航班，删除了ZMUB飞往UACC/UCFM 的航班；
 * V4_7: 国内飞往国外的经A575飞越UDA的航路截断到INTIK，经B339飞越UDA的航路截断到POLHO；
 * V4_8: 国内飞往国外的飞越PIDOX经A575出国的航路截断到INTIK;
 * V4_9:修改了平壤起落航班path
 * V4_10 : 修改了ZSZS-ZSWZ、ZBZL-ZBLA、ZLZY-ZLLL的path，国内飞越SUNUV经B330飞往国外的截断到MORIT
 * V4_11 : 修改了ZBHH-ZBMZ的航路；ZJHK-RPLL, ZJHK-WMKK,ZGNN-VVNB,ZGNN-ZPPP,ZGNN-VTBU,ZWCM-ZWKl
 * V4_12: 修改了ZSNJ-ZJHK、ZLIC-ZLJC，ZUGY-VVCR的航路。国内自B208经MU离境航线从NIXAL处截断
 * V4_13 : 修改了ZJGX-ZGWZ,ZJSY-RCMQ,SJSY-ZSOF,ZJHK-YSSY  的航线；
 * v5 : 替换flt_path为 点-点 形式；
 */
public class FltPlanSelector {
    private static final Logger LOGGER = Logger.getLogger(FltPlanSelector.class);
    DataAccessObject dao = new DataAccessObject();
    public FltPlanSelector() {

    }
    public List<FltPlan> getFltPlans() {
        String table = "fme201806_domestic_v4_13";
        List<FltPlan> res = dao.getStandardFltPlanFromMySQL(table,"");
        return res;
    }
    public void storageFltPlan(List<FltPlan> planList) {
        String table = "fme201806_domestic_v5";
        dao.storageFltPlan(table,planList);
    }
    public Set<String> notInNAIPPoint = new HashSet<>();
    public static void main(String[] args){
        FltPlanSelector fps = new FltPlanSelector();
        List<FltPlan> fltPlanList = fps.getFltPlans();
//        List<FltPlan> res = fps.deleteUselessPlan(fltPlanList);
//         List<FltPlan> res0 = fps.cutGoAbroadPlan(fltPlanList);
        List<FltPlan> res = fps.changeFltRoute(fltPlanList);
//        List<FltPlan> res = fps.changePath(fltPlanList);
        LOGGER.debug(fltPlanList.size() + "  " + res.size());

        fps.storageFltPlan(res);
    }

    //============================================
    static int countCHC = 0, countNoPt = 0,countVMMH = 0;
    public List<FltPlan> changeFltRoute(List<FltPlan> planList) {
        List<FltPlan> res = new ArrayList<>();
        for (FltPlan plan : planList) {
            if (plan.flt_no.startsWith("CHC")){
                countCHC++;
                continue;
            }
            if (plan.ld_ap.equals("VMMH")) {
                countVMMH++;
                continue;
            }
            try {
                String path = addToString(splitRoute(plan));
                plan.flt_path = path;
                res.add(plan);
            } catch (NullPointerException e) {
                LOGGER.debug(plan.toString() + "   " +plan.flt_path);
            }
        }
        LOGGER.debug("CHC plans : " +countCHC + " VMMH plans :" + countVMMH);
        return res;
    }
    public String addToString(List<PointInfo> pList){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pList.size(); i++) {
            PointInfo pi = pList.get(i);
            res.append(pi.fix_pt);
            if (i != pList.size() - 1) {
                res.append("-");
            }
        }
        return res.toString();
    }
    public List<PointInfo> splitRoute(FltPlan plan){
        String path =plan.flt_path;
        List<PointInfo> fixPts = new ArrayList<>();  //as result

        String[] tmp = path.trim().split("\\s+");
        int[] flag = new int[tmp.length]; //route = 1; point = 0;
        if (tmp[0].equals("DCT")) {
            flag[0] = -1;
        } else {
            PointInfo p0 = createPoint(splitPointName(tmp[0]));
            if (p0.fix_pt.equals("")) {
//                LOGGER.debug(plan.toString() + " ====== " + tmp[0] + "!!!!!");
            }
            fixPts.add(p0);
            flag[0] = 0;
        }
        for(int i = 1; i < tmp.length; i++) {
            if (tmp[i].equals("DCT")) {
                flag[i] = -1;
                continue;
            }
            if (dao.isNaipMapContainKey(tmp[i]) && (flag[i - 1] == 0)) {
                flag[i] = 1;
//                System.out.println("R:" + tmp[i]);
                List<PointInfo> ptsOnR = new ArrayList<>();
                if (i == tmp.length - 1) {
                   LOGGER.info("不能以航路作为path结尾。" + plan.toString() + " " + path);
                } else {
                    String pointPrev = splitPointName(tmp[i-1]);
                    String pointAfter = splitPointName(tmp[i+1]);
                    ptsOnR = dao.getSubPtSeq(tmp[i], pointPrev,pointAfter,0);
                }
                if (ptsOnR == null) {
                    LOGGER.info( countNoPt++ + "航路没有点  " + plan.toString());
                    continue;
                }else {
                    fixPts.addAll(ptsOnR);
                }
            } else {
                flag[i] = 0;
//                System.out.println("Point:" + tmp[i]);
                PointInfo p = new PointInfo(splitPointName(tmp[i]));
                if (p.fix_pt.equals("")) {
                    LOGGER.debug(plan.toString() + " ====== " + tmp[i] + "+++++++ " + i);
                }
                fixPts.add(p);

            }
        }
        return fixPts;
    }
    public PointInfo createPoint(String pt) {
        PointInfo pi = null;
        if (dao.isNaipPoints(pt)) {
           Point p = dao.getNaipPoint(pt);
           pi = new PointInfo(p.pid);
        } else {
//            LOGGER.info(pt + " 不是NAIP中的点");
            notInNAIPPoint.add(pt);
            pi = new PointInfo("");
        }
        return pi;
    }
    private String splitPointName(String s) {
        if (s.contains("/")) {
            String[] t = s.split("/");
            if (t[0].equals("VRK")) {
                return "VKR";
            }
            return s.split("/")[0];
        } else {
            return s;
        }
    }
    //================================================
    static int countZMUB = 0;
    public List<FltPlan> changePathForZMUB(List<FltPlan> planList) {
        List<FltPlan> res = new ArrayList<>();
        String path1 = "HLD A345 KAGAK G338 SARUL"; //ZBLA-ZMUB
        String path2 = "HET G218 TMR B458 LHT A575 INTIK";//ZBHH-ZMUB
        String path3 = "YV B334 PIDOX A575 INTIK";//ZBAA_ZMUB
        String path4 = "MZL W606 IKARU G338 SARUL/K0506S0660";//ZBMZ-ZMUB
        String path5 = "LATUX A591 MUDAL/K0833S1160 A326 LADIX B339 POLHO";//ZSQD-ZMUB
        String path6 = "TMR B458 LHT A575 INTIK";//ZBZJ-ZMUB
        for (FltPlan plan : planList) {
            if (plan.ld_ap.equals("ZMUB")) {
                if (plan.to_ap.equals("ZBLA")) {
                    plan.flt_path = path1;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
                if (plan.to_ap.equals("ZBHH")) {
                    plan.flt_path = path2;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
                if (plan.to_ap.equals("ZBAA")) {
                    plan.flt_path = path3;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
                if (plan.to_ap.equals("ZBMZ")) {
                    plan.flt_path = path4;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
                if (plan.to_ap.equals("ZSQD")) {
                    plan.flt_path = path5;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
                if (plan.to_ap.equals("ZBZJ")) {
                    plan.flt_path = path6;
                    res.add(plan);
                    countZMUB++;
                    continue;
                }
            }else {
                res.add(plan);
            }
        }
        LOGGER.debug("plans about ZMUB : " + countZMUB);
        return res;
    }
    // 替换ZMUB起飞航班的path ，注意，先不要运行
    public List<FltPlan> changePathFromZMUB(List<FltPlan> planList) {
        List<FltPlan> res = new ArrayList<>();
        for (FltPlan plan : planList) {
            if (plan.to_ap.equals("ZMUB")) {
                countZMUB++;
                String path1 = "NIXAL B208 CGO W129 OBLIK/K0845S1160 A461 LIG/K0843S1130 R473 NNX/K0845S1160 R473 WYN W18 NLG W23 ZUH R473  SIERA"; //ZMUB-VHHH
                String path2 = "INTIK/K0889S1220 W32 HET/K0893S1190 B208  CGO W129 KAMDA DCT OBLIK/K0889S1220 A461 LIG/K0891S1190 R473  NNX/K0887S1220 R473 NOLON W90 POU W7 SAREX W6 LATOP";//ZMUB-VMMC
                String path3 = "NIXAL B208 CGO W129 OBLIK/K0863S1100 A461  LKO R343 WUY R474 TEBAK/N0463F380";//ZMUB-WSSS
                String path4 = "TEBUS  G588 FKG B215 PURPA/N0451F340"; //ZMUB-OTBH
                String path5 = "SARUL G338 KAGAK A345 HLD/K0837S1130 A345  KYU/K0830S1160 A345 BIDIB A588 CHI W107 SANKO/K0833S1190 A326 DONVO  G597 AGAVO/N0451F390";//ZMUB-PHJR
                String path6 = "POLHO W33 PIDOX B339 LADIX A326 DONVO G597  AGAVO/N0445F350";//ZMUB-RJAA
                String path7 = "SARUL G338 KAGAK A345 KYU/K0804S1100 A345 BIDIB A588  CHI W107 SANKO/K0807S1070 A326 DONVO G597 AGAVO/N0434F370";//ZMUB-RJTY
                String path8 = "POLHO W33 PIDOX B339 LADIX A326 DONVO G597  AGAVO/N0454F370";//ZMUB-RKPK/RKSI/RKTU/RKPC/
                String path9 = "SARUL G338 KAGAK A345 KYU/N0320F276 A345  BIDIB A588 ISKEM/N0320F280 DCT NODAL DCT CHI/N0320F276 W107  SANKO/N0320F266 A326 DONVO G597 AGAVO/N0320F270";//ZMUB-RKSO
                String path10 = "TEBUS  G588 FKG B215 POSOT A343 RULAD";//ZMUB-UAAA  此处缺少G588入境点
                String path11 = "INTIK/K0833S0890  A575  LHT  B458  TMR/K0814S0810  B458  TZH  A596  KM_怀来";//ZMUB-ZBAA
                String path12 = "INTIK A575 LHT B458 TMR G218 HET";//ZMUB-ZBHH
                String path13 = "SARUL G338 KAGAK A345 HLD";//ZMUB-ZBLA
                String path14 = "SARUL G338 SARUL/K0486S0690 G338 IKARU W606 MZL";//ZMUB-ZBMZ
                String path15 = " INTIK A575 LHT B458 TMR";//ZMUB-ZBZJ
                String path16 = "POLHO W33 PIDOX B339 HUR/K0815S0980 W80 VYK   A593 BTO/K0811S1010 A593 RAXEV W4 HCH W5 ATLED";// ZMUB-ZSQD
                //ZMUB-UACC/UCFM/ 没有path
                if (plan.ld_ap.equals("VHHH")) {
                    plan.flt_path = path1;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("VMMC")) {
                    plan.flt_path = path2;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("WSSS")) {
                    plan.flt_path = path3;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("OTBH")) {
                    plan.flt_path = path4;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("PHJR")) {
                    plan.flt_path = path5;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("RJAA")) {
                    plan.flt_path = path6;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("RJTY")) {
                    plan.flt_path = path7;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("RKPK") || plan.ld_ap.equals("RKSI") || plan.ld_ap.equals("RKTU")
                        || plan.ld_ap.equals("RKPC")) {
                    plan.flt_path = path8;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("RKSO")) {
                    plan.flt_path = path9;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("UAAA")) {
                    plan.flt_path = path10;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZBAA")) {
                    plan.flt_path = path11;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZBHH")) {
                    plan.flt_path = path12;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZBLA")) {
                    plan.flt_path = path13;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZBMZ")) {
                    plan.flt_path = path14;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZBZJ")) {
                    plan.flt_path = path15;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("ZSQD")) {
                    plan.flt_path = path16;
                    res.add(plan);
                    continue;
                }
                if (plan.ld_ap.equals("UACC") || plan.ld_ap.equals("UCFM")) {
                    continue;
                }

            }else {
                if (plan.to_ap.equals("ZBUT") || plan.ld_ap.equals("ZBUT")) {
                    continue;
                } else {
                    res.add(plan);
                }
            }
        }
        LOGGER.debug("plans about ZMUB : " + countZMUB);
        return res;
    }
    static int countZKPY = 0;
    public List<FltPlan> changePathForZKPY(List<FltPlan> planLIst) {
        List<FltPlan> res = new ArrayList<>();
        String path1 = "CDY W83 ANDIN A575 BIDIB A345 GOLOT/N0400F250"; //ZBAA-ZKPY
        String path2 = " DCT HSH G455 SURAK A326 SANKO/K0830S1130 B332  TOMUK/N0380F250";//ZSPD-ZKPY
        String path3 = "ANSUK A345 GOLOT/N0320F190";//ZYTX-ZKPY
        String path4 = "GOLOT/K0750S0920 A345 BIDIB A575 CHG G332  GITUM";//ZKPY-ZBAA
        String path5 = "TOMUK/K0780S0920 B332  SANKO/K0830S1130 A326 AKARA A593 PUD";//ZKPY_ZSPD
        String path6 = " YIN A461 WXI W4 HCH W173 NIXEP W8 DOBGA A326 SANKO  B332 TOMUK/N0420F250";//ZGGG_ZKPY
        String path7 = "TOMUK/K0780S0720 B332 SANKO A326  MAKNO    W5 HCH W4 WXI A461 LIG R473 BEMAG V5 ATAGA";//ZKPY-ZGGG
        for (FltPlan plan : planLIst) {
                if (plan.ld_ap.equals("ZKPY") && plan.to_ap.equals("ZBAA")) {
                    plan.flt_path = path1;
                    res.add(plan);
                    continue;
                } else if (plan.ld_ap.equals("ZKPY") && plan.to_ap.equals("ZSPD")) {
                    plan.flt_path = path2;
                    res.add(plan);
                    continue;
                } else if (plan.ld_ap.equals("ZKPY") && plan.to_ap.equals("ZYTX")) {
                    plan.flt_path = path3;
                    res.add(plan);
                    continue;
                }else if (plan.ld_ap.equals("ZKPY") && plan.to_ap.equals("ZGGG")) {
                    plan.flt_path = path6;
                    res.add(plan);
                    continue;
                }else if (plan.to_ap.equals("ZKPY") && plan.ld_ap.equals("ZGGG")) {
                    plan.flt_path = path7;
                    res.add(plan);
                    continue;
                }else if (plan.to_ap.equals("ZKPY") && plan.ld_ap.equals("ZBAA")) {
                    plan.flt_path = path4;
                    res.add(plan);
                    continue;
                }else if (plan.to_ap.equals("ZKPY") && plan.ld_ap.equals("ZSPD")) {
                    plan.flt_path = path5;
                    res.add(plan);
                    continue;
                }else if (plan.to_ap.equals("ZKPY") && plan.ld_ap.equals("WSSS")) {
                    continue;
                } else {
                    countZKPY++;
                    res.add(plan);
                }
        }
        LOGGER.debug("ZKPY : " + (planLIst.size() - countZKPY));
        return res;
    }
    public List<FltPlan> changePath(List<FltPlan> planLIst) {
        List<FltPlan> res = new ArrayList<>();
        String path1 = "MZL W538 KAGAK G338 SARUL/K0846S0920 POLHO/K0846S0920 G218 HET";//ZBMZ-ZBHH
        String path2 = " HSN W67 BK W58 SHZ B221 DST";//ZSZS-ZSWZ
        String path3 = "DST B221 SHZ W58 BK W67 HSN";
        String path4 = "ZLT J186 P28 B451 HLD"; //ZBZL-ZBLA
        String path5 = "GAZ J105 NODID H74 XNN H15 DNC";//ZLZY-ZLLL
        String path6 = "HET G218 POLHO/K0846S0950  SARUL/K0846S0950 G338 KAGAK W538 MZL";//ZBHH-ZBMZ
        String path7 = "WUY R474 TEBAK";//ZGNN-VVNB
        String path8 = "WUY  R339  BSE A599 LXI"; //ZGNN-ZPPP
        String path9 = "WUY R474 TEBAK/N0442F300 ";//ZGNN-VTBU
        String path10 = "MLT DCT SAMAS/K0850F350 A202 SIKOU/N0460F350";//ZJHK-WMKK
        String path11 = "QIM H63 SADAN H63 KEL";//ZWCM-ZWKL
        String path12 = " YHD B215 P152 P151 JTA B330 AKMAT P92 J99 GJC";//ZLIC-ZLJC
        String path13 = "SUMUN W179 BIPIP A581 SGM/K0848S1130 A599 ADBAG R471  KATBO";//ZUGY-VVCR
        String path14 = "TESIG A470 TOL A599 PLT W19 NOMAR W18 NLG W23 ZUH R200  BIGRO G221 GIVIV W605 DOMGO";//ZSNJ-ZJHK
        String path15 = " WL G221 BIGRO V20 UDUTI V21 AVPAM A599 BIPOP"; //ZJGX-ZGWZ
        String path16 = "WL G221 SAMAS/K0885S0890 A202 SIKOU/N0478F290";//ZJSY-RCMQ
        String path17 = " WL G221 NYB V12 AGPOR H81 GYA A599 POU G471 PLT W19  OSONO H46 P262/M078S0980 H2 P321";//SJSY-ZSOF
        String path18 = "MLT SAMAS A202 SIKOU/N0477F350";//ZJHK-YSSY
//        String path19 = "WL G221 SAMAS/K0885S0890 A202 SIKOU/N0478F290";//ZJSY-RCMQ
        for (FltPlan plan : planLIst) {
            if (plan.to_ap.equals("ZJGX") && plan.ld_ap.equals("ZGWZ")) {
                plan.flt_path = path15;
                res.add(plan);
                continue;
            } else if(plan.to_ap.equals("ZUGY") && plan.ld_ap.equals("VVCR")) {
                 plan.flt_path = path13;
                 res.add(plan);
                 continue;
            } else if(plan.to_ap.equals("ZJSY") && plan.ld_ap.equals("RCMQ")) {
                plan.flt_path = path16;
                res.add(plan);
                continue;
            } else if(plan.to_ap.equals("ZJSY") && plan.ld_ap.equals("ZSOF")) {
                plan.flt_path = path17;
                res.add(plan);
                continue;
            } else if(plan.to_ap.equals("ZJHK") && plan.ld_ap.equals("YSSY")) {
                plan.flt_path = path18;
                res.add(plan);
                continue;
//            } else if(plan.to_ap.equals("ZSNJ") && plan.ld_ap.equals("ZJHK")) {
//                plan.flt_path = path14;
//                res.add(plan);
//                continue;
//             if (plan.to_ap.equals("ZBHH") && plan.ld_ap.equals("ZBMZ")) {
//                plan.flt_path = path6;
//                res.add(plan);
//                continue;
//             } else if(plan.to_ap.equals("ZGNN") && plan.ld_ap.equals("VVNB")) {
//                 plan.flt_path = path7;
//                 res.add(plan);
//                 continue;
//             }else if(plan.to_ap.equals("ZGNN") && plan.ld_ap.equals("ZPPP")) {
//                     plan.flt_path = path8;
//                     res.add(plan);
//                     continue;
//             }else if(plan.to_ap.equals("ZGNN") && plan.ld_ap.equals("VTBU")) {
//                 plan.flt_path = path9;
//                 res.add(plan);
//                 continue;
//             }else if(plan.to_ap.equals("ZJHK") && (plan.ld_ap.equals("WMKK")||plan.ld_ap.equals("RPLL"))) {
//                 plan.flt_path = path10;
//                 res.add(plan);
//                 continue;
//             }else if(plan.to_ap.equals("ZWCM") && plan.ld_ap.equals("ZWKL")) {
//                 plan.flt_path = path11;
//                 res.add(plan);
//                 continue;
//            } else if(plan.to_ap.equals("ZSZS") && plan.ld_ap.equals("ZSWZ")) {
//                 plan.flt_path = path2;
//                 res.add(plan);
//                 continue;
//             }else if(plan.to_ap.equals("ZSWZ") && plan.ld_ap.equals("ZSZS")) {
//                 plan.flt_path = path3;
//                 res.add(plan);
//                 continue;
//             } else if(plan.to_ap.equals("ZBZL") && plan.ld_ap.equals("ZBLA")) {
//                 plan.flt_path = path4;
//                 res.add(plan);
//                 continue;
//             }else if(plan.to_ap.equals("ZLZY") && plan.ld_ap.equals("ZLLL")) {
//                 plan.flt_path = path5;
//                 res.add(plan);
//                 continue;
             }else{
                 if (plan.to_ap.equals(plan.ld_ap)) {
                     LOGGER.info(plan.toString());
                     continue;
                 }
                countZKPY++;
                res.add(plan);
            }
        }
        LOGGER.debug("changed : " + (planLIst.size() - countZKPY));
        return res;
    }
    //==================================================
        public List<FltPlan> deleteUselessPlan(List<FltPlan> planList) {
        List<FltPlan> res = new ArrayList<>();
        int count = 0;
        for (FltPlan plan : planList) {
            if (plan.to_ap.equals(plan.ld_ap)|| plan.to_ap.equals("ZZZZ") || plan.ld_ap.equals("ZZZZ")) {
                count++;
                continue;
            } else {
                res.add(plan);
            }
        }
        System.out.println("droped plans : " + count);
        return res;
        }
    //===============================================
    static  int countAbroad = 0, countChanged = 0;
    public List<FltPlan> cutGoAbroadPlan(List<FltPlan> plans) {
        List<FltPlan> res = new ArrayList<>();
        for (FltPlan plan : plans) {
            if (isDomesticAP(plan.ld_ap)) {
                res.add(plan);
            } else {
                countAbroad++;
                if (plan.to_ap.equals("ZMUB") || plan.ld_ap.equals("ZMUB") || plan.to_ap.equals("ZKPY")
                        || plan.ld_ap.equals("ZKPY")) {
                    res.add(plan);
                } else {
                    String path = cutPath(plan);
                    if(!path.equals(plan.flt_path)) {
                        countChanged++;
//                        if (!plan.to_ap.equals("ZBAA") && !plan.to_ap.equals("ZSPD"))
                        LOGGER.info(plan.toString() + " " + plan.flt_path);
                        LOGGER.info("========" +path);
                    }
                    plan.flt_path = path;
                    res.add(plan);
                }
            }
        }
        LOGGER.debug("changed " + countChanged);
//        LOGGER.info("flt_path有两段在国内空域的航班，共计 " + countChanged + " 条");
        return res;
    }
    public String cutPath(FltPlan plan) {
        String path = plan.flt_path;
        String[] ptss = path.split("\\s+");
        List<String> pts = Arrays.asList(ptss);
        String res = null;
        for (int i = 0; i < pts.size(); i++) {
            String s = pts.get(i);
            if (s.contains("/")) {
                String tt = s.split("/")[0];
                if (dao.isBoundriesContainPoint(tt)) {
                    res = path.substring(0,path.indexOf(s) + s.length());
                    break;
                } else {
//                    if (s.equals("ANIKU/K0826S1160") || (tt.equals("UDA") && pts.get(i-1).equals("A575"))) {
//                        res = path.substring(0,path.indexOf(s)) + " INTIK";
//                        break;
//                    } else if (tt.equals("UDA") && pts.get(i-1).equals("B339")) {
//                        res = path.substring(0,path.indexOf(s)) + " POLHO";
//                    }else {
                        continue;
//                    }
                }
            } else {
                if (dao.isBoundriesContainPoint(s)) {
                    res = path.substring(0,path.indexOf(s) + s.length());
                    break;
                }else  {
                    if (s.equals("MU") && pts.get(i-1).equals("B208")) {
                        res = path.substring(0,path.indexOf(s)) + " NIXAL";
                        break;
//                    if (s.equals("UDA") && pts.get(i-1).equals("A575")) {
//                        res = path.substring(0,path.indexOf(s)) + " INTIK";
//                        break;
//                    } else if (s.equals("UDA") && pts.get(i-1).equals("B339")) {
//                        res = path.substring(0,path.indexOf(s)) + " POLHO";
//                        break;
//                    }else if (s.equals("A575") && pts.get(i-1).equals("PIDOX")){
//                        res = path.substring(0,path.indexOf(s)) +" INTIK";
//                        break;
                    }else{
                            continue;
                        }
                    }
                }
        }
        if (res != null) {
            return res;
        } else {
//            LOGGER.debug("No Boundries : " + plan.toString() + " " + plan.flt_path );
            return path;
        }
    }
    public boolean isDomesticAP(String s) {
        if (s.equals("ZKPY") || s.equals("ZMUB")) {
            return false;
        }
        String pattern = "^Z[A-Z]*$";
        return s.matches(pattern);
    }
 }
