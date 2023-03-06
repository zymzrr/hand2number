package com.hs.trace.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class TraceOcr {


    //小段角度,大段头尾角度,x比例,y异轴比例
    static String n1 = "[60-120],[75-135],0-1,0.8-1";
    static String n2 = "[270-360 0-90],[315-360 0-75],0.1-1,0-1;[90-270],[90-180],0-1,0-1;[270-360 0-90],[300-360 0-30],0.2-1,0-1";
    static String n3 = "[270-360 0-90],[315-360 0-75],0.2-1,0-1;[90-270],[90-180],0.1-1,0-1;[270-360 0-90],[300-360 0-75],0.1-1,0-1;[90-270],[105-210],0.2-1,0-1";
    static String n4 = "[45-150],[0-180],0-1,0.2-1;[315-360 0-45],[315-360 0-45],0.2-1,0-1;[180-360],[180-270],0-1,0.02-1;[45-135],[45-120],0-1,0.4-1";
    static String n5 = "[315-360 0-30],[315-360 0-30],0.1-1,0-1;[180-270],[180-270],0-1,0-1;[75-135],[75-135],0-1,0.1-1;[335-360 0-75],[335-360 0-75],0-1,0.1-1;[90-270],[90-270],0.1-1,0-1";
    static String n6 = "[45-180],[45-135],0-1,0.6-1;[180-360],[180-360],0-1,0.05-0.7;[90-225],[90-225],0-1,0-1";
    static String n7 = "[270-360 0-45],[315-360 0-45],0.2-1,0-1,0-1,0.1-3;[60-135],[75-135],0-1,0.8-1";
    static String n81 = "[90-270],[105-270],0.1-1,0-1;[270-360 0-90],[0-90],0.2-1,0-1;[90-270],[90-270],0.2-1,0-1;[270-360 0-90],[270-360],0.2-1,0-1";
    static String n82 = "[270-360 0-90],[315-360 0-75],0.3-1,0-1;[90-270],[90-180],0.3-1,0-1;[270-360 0-90],[300-360 0-45],0.3-1,0-1;[90-270],[180-270],0.3-1,0-1";
    static String n91 = "[90-270],[105-225],0.3-1,0-1;[270-360 0-45],[315-360 0-45],0.3-1,0-1;[75-135],[75-135],0-1,0.3-1";
    static String n92 = "[270-360],[270-360],0.3-1,0-1;[135-270],[135-270],0.1-1,0-1;[45-135],[45-135],0-1,0.7-1";
    static String n01 = "[90-270],[90-180],0.02-1,0-1;[270-360 0-90],[300-360 0-75],0.8-1,0-1;[90-270],[90-270],0.01-1,0-1";
    static String n02 = "[45-135],[45-135],0-1,0-1;[315-360 0-45],[315-360 0-45],0-1,0-1;[235-315],[235-315],0-1,0-1;[135-225],[135-225],0-1,0-1;";

    static String n4_1_1 = "[45-150],[0-180],0-1,0.2-1;[315-360 0-45],[315-360 0-45],0.2-1,0-1";
    static String n4_1_2 = "[60-120],[75-135],0-1,0.8-1";

    static String n5_1_1 = "[75-135],[75-135],0-1,0.1-1;[90-270],[90-270],0.1-1,0-1";
    static String n5_1_2 = "[315-360 0-45],[315-360 0-45],0.5-1,0-1";

    static String A1 = "[270-360],[270-360],0-1,0.2-1;[0-115],[0-115],0-1,0.2-1;[180-270],[180-270],0-1,0-1;[270-360 0-45],[270-360 0-45],0.1-1,0-1";
    static String B1 = "[45-135],[45-135],0-1,0.2-1;[225-315],[225-315],0-1,0.2-1;[315-360 0-45],[315-360 0-45],0-1,0-1;[90-180],[90-180],0-1,0-1;[315-360 0-75],[315-360 0-75],0-1,0-1;[90-180],[90-180],0.1-1,0-1";
    static String C1 = "[45-135],[45-135],0-1,0-1;[315-360 0-45],[315-360 0-45],0-1,0-1";
    static String D1 = "[45-135],[45-135],0-1,0.2-1;[225-315],[225-315],0-1,0.2-1;[315-360 0-45],[315-360 0-45],0-1,0-1;[90-180],[90-180],0.1-1,0-1";


    static String X_1 = "[0-90],[0-90],0.1-1,0.1-1;[225-360],[225-360],0-1,0.1-1;[90-180],[90-180],0.1-1,0.1-1";
    static String X_2 = "[90-180],[90-180],0.1-1,0.1-1;[225-315],[225-315],0-1,0.1-1;[0-90],[0-90],0.1-1,0.1-1";
    static String X_1_1 = "[0-90],[0-90],0.1-1,0.1-1";
    static String X_1_2 = "[90-180],[90-180],0.1-1,0.1-1";
    //v[15-120],[15-120],0-1,0-1;
    static String V = "[270-360 0-15],[270-360 0-15],0.1-1,0-1";


    public static void main(String[] args) {
//        String txt = FileUtil.readString("F:\\wechat\\WeChat Files\\sanjijiji\\FileStorage\\File\\2022-04\\22.txt","UTF-8");
//        JSONArray array = JSONUtil.parseArray(txt);
//        List<Point> pointList = array.toList(Point.class);
//        ocrNumber(pointList);
    }

    //识别单个数字
    public static String ocrNumber(List<Point> objList) {
        if (objList.size() < 4) {
            return null;
        }
        HandList handList = preDoPoint(objList);
        String letter =  getNumber(handList);
        System.out.println(letter);
        return letter;
    }

    //识别单个数字
    public static String ocrLetter(List<Point> objList) {
        if (objList.size() < 4) {
            return null;
        }
        HandList handList = preDoPoint(objList);
        String letter =  getLetter(handList);
        System.out.println(letter);
        return letter;
    }


    //识别单个数字
    public static String ocrRaw(List<Point> objList) {
        if (objList.size() < 4) {
            return null;
        }
        HandList handList = preDoPoint(objList);
        String letter =  getRaw(handList);
        System.out.println(letter);
        return letter;
    }


    public static String getNumber(HandList handList) {

        if (handList.getLineList().size() == 1) {

            List<Point> userLine = handList.getLineList().get(0);
            if (userLine.size() <= 4) {
                return "1";
            }

            Helper helper = new Helper();
            setXY(helper,userLine);

            if(fuckByTrace(modelMap.get("5"),userLine,helper)){
                return "5";
            }
            if(fuckByTrace(modelMap.get("3"),userLine,helper)){
                return "3";
            }
            if(fuckByTrace(modelMap.get("8"),userLine,helper)){
                return "8";
            }
            if(fuckByTrace(modelMap.get("4"),userLine,helper)){
                return "4";
            }
            if(fuckByTrace(modelMap.get("2"),userLine,helper)){
                return "2";
            }
            if(fuckByTrace(modelMap.get("6"),userLine,helper)){
                return "6";
            }
            if(fuckByTrace(modelMap.get("9"),userLine,helper)){
                return "9";
            }
            if(fuckByTrace(modelMap.get("0"),userLine,helper)){
                return "0";
            }
            if(fuckByTrace(modelMap.get("7"),userLine,helper)){
                return "7";
            }
            if(fuckByTrace(modelMap.get("1"),userLine,helper)){
                return "1";
            }
            return null;
        }
        else if (handList.getLineList().size() == 2) {
            List<Point> userLine1 = handList.getLineList().get(0);
            List<Point> userLine2 = handList.getLineList().get(1);

            Helper helper1 = new Helper();
            setXY(helper1,userLine1);

            Helper helper2 = new Helper();
            setXY(helper2,userLine2);

            if(fuckByTrace(modelMap.get("421"),userLine1,helper1)
                    && fuckByTrace(modelMap.get("422"),userLine2,helper2)
                    ){
                return "4";
            }

            if(
                (fuckByTrace(modelMap.get("521"),userLine1,helper1)
                    && fuckByTrace(modelMap.get("522"),userLine2,helper2))||
                (fuckByTrace(modelMap.get("522"),userLine1,helper1)
                        && fuckByTrace(modelMap.get("521"),userLine2,helper2))
            ){
                return "5";
            }


            return null;
        }
        return null;
    }

    private static String getLetter(HandList handList) {

        if (handList.getLineList().size() == 1) {

            List<Point> userLine = handList.getLineList().get(0);
            if (userLine.size() <= 4) {
                return "C";
            }

            Helper helper = new Helper();
            setXY(helper,userLine);

            if(fuckByTrace(modelMap.get("B"),userLine,helper)){
                return "B";
            }
            if(fuckByTrace(modelMap.get("A"),userLine,helper)){
                return "A";
            }
            if(fuckByTrace(modelMap.get("D"),userLine,helper)){
                return "D";
            }
            if(fuckByTrace(modelMap.get("C"),userLine,helper)){
                return "C";
            }
            return null;
        }
        else if (handList.getLineList().size() == 2) {

            return null;
        }
        return null;
    }

    private static String getRaw(HandList handList) {

        if (handList.getLineList().size() == 1) {

            List<Point> userLine = handList.getLineList().get(0);

            Helper helper = new Helper();
            setXY(helper,userLine);

            if(fuckByTrace(modelMap.get("X1"),userLine,helper)
                    || fuckByTrace(modelMap.get("X2"),userLine,helper)
                    ){
                return "X";
            }
            if(fuckByTrace(modelMap.get("V"),userLine,helper)){
                return "V";
            }
        }
        else if (handList.getLineList().size() == 2) {

            List<Point> userLine1 = handList.getLineList().get(0);
            List<Point> userLine2 = handList.getLineList().get(1);

            Helper helper1 = new Helper();
            setXY(helper1,userLine1);

            Helper helper2 = new Helper();
            setXY(helper2,userLine2);

            if((fuckByTrace(modelMap.get("X21"),userLine1,helper1)
                    && fuckByTrace(modelMap.get("X22"),userLine2,helper2))
                    ||
                    (fuckByTrace(modelMap.get("X22"),userLine1,helper1)
                            && fuckByTrace(modelMap.get("X21"),userLine2,helper2))
                    ){
                return "X";
            }
        }
        return null;
    }


    static Map<String,List<Letter>> modelMap = new HashMap<>();

    static {
        //一笔
        modelMap.put("1", Arrays.asList(new Letter[]{convertModel("1", n1)}));
        modelMap.put("2", Arrays.asList(new Letter[]{convertModel("2",n2)}));
        modelMap.put("3", Arrays.asList(new Letter[]{convertModel("3",n3)}));
        modelMap.put("4", Arrays.asList(new Letter[]{convertModel("4",n4)}));
        modelMap.put("5", Arrays.asList(new Letter[]{convertModel("5",n5)}));
        modelMap.put("6", Arrays.asList(new Letter[]{convertModel("6",n6)}));
        modelMap.put("7", Arrays.asList(new Letter[]{convertModel("7",n7)}));
        modelMap.put("8", Arrays.asList(new Letter[]{
                convertModel("8",n81),
                convertModel("8",n82)}));
        modelMap.put("9", Arrays.asList(new Letter[]{
                convertModel("9",n91),
                convertModel("9",n92)}));
        modelMap.put("0", Arrays.asList(new Letter[]{
                convertModel("0",n01),
                convertModel("0",n02)
        }));

        //两笔
        modelMap.put("421", Arrays.asList(new Letter[]{convertModel("4",n4_1_1)}));
        modelMap.put("422", Arrays.asList(new Letter[]{convertModel("4",n4_1_2)}));

        //两笔
        modelMap.put("521", Arrays.asList(new Letter[]{convertModel("5",n5_1_1)}));
        modelMap.put("522", Arrays.asList(new Letter[]{convertModel("5",n5_1_2)}));

        modelMap.put("A", Arrays.asList(new Letter[]{convertModel("A", A1)}));
        modelMap.put("B", Arrays.asList(new Letter[]{convertModel("B",B1)}));
        modelMap.put("C", Arrays.asList(new Letter[]{convertModel("C",C1)}));
        modelMap.put("D", Arrays.asList(new Letter[]{convertModel("D",D1)}));

        //
        modelMap.put("X1", Arrays.asList(new Letter[]{convertModel("X", X_1)}));
        modelMap.put("X2", Arrays.asList(new Letter[]{convertModel("X", X_2)}));
        modelMap.put("X21", Arrays.asList(new Letter[]{convertModel("X", X_1_1)}));
        modelMap.put("X22", Arrays.asList(new Letter[]{convertModel("X", X_1_2)}));
        modelMap.put("V", Arrays.asList(new Letter[]{convertModel("v", V)}));

    }

    //字符串转模型
    static Letter convertModel(String charr,String modelStr){
        Letter letter = new Letter();
        letter.setLetter(charr);
        List<Line> lines = new ArrayList<>();
        String[] segs = modelStr.split(";");
        for (int i = 0;i<segs.length;i++) {
            String seg = segs[i];
            String[] actions = seg.split(",");
            String[] arrowStr = actions[0].replace("[","").replace("]","").split(" ");
            String[] degStr = actions[1].replace("[","").replace("]","").split(" ");
            String[] curXRange = actions[2].split("-");
            String[] curYRange = actions[3].split("-");


            Range[] arrowdegs = new Range[arrowStr.length];
            for (int j = 0;j<arrowStr.length;j++) {
                String[] ds = arrowStr[j].split("-");
                arrowdegs[j] = new Range(Float.parseFloat(ds[0]),Float.parseFloat(ds[1]));
            }
            Range[] degs = new Range[degStr.length];
            for (int j = 0;j<degStr.length;j++) {
                String[] ds = degStr[j].split("-");
                degs[j] = new Range(Float.parseFloat(ds[0]),Float.parseFloat(ds[1]));
            }

            Line line = new Line();
            line.setArrows(arrowdegs);
            line.setDegs(degs);
            line.setCurXPercent(new Range(Float.parseFloat(curXRange[0]),Float.parseFloat(curXRange[1])));
            line.setCurYPercent(new Range(Float.parseFloat(curYRange[0]),Float.parseFloat(curYRange[1])));
            if(actions.length >= 5){
                String[]  diffRange = actions[4].split("-");
                line.setDiffXPercent(new Range(Float.parseFloat(diffRange[0]),Float.parseFloat(diffRange[1])));
            }
            if(actions.length >= 6){
                String[]  diffRange = actions[5].split("-");
                line.setDiffYPercent(new Range(Float.parseFloat(diffRange[0]),Float.parseFloat(diffRange[1])));
            }
            lines.add(line);
        }
        letter.setLines(lines);
        System.out.println(JSONUtil.toJsonStr(letter));
        return letter;
    }



    private static boolean fuckByTrace(List<Letter> letters, List<Point> userLines,Helper helper) {

        for (Letter letter : letters) {
            System.out.println("正在识别letter:"+letter.getLetter());
            int i = 0 ;
            int j = 0 ;
            for (Line condLine : letter.getLines()) {
                System.out.println("正在识别line:"+j);
                for (; i < userLines.size(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    //2个点间的角度
                    Point prePoint = userLines.get(i-1);
                    Point curPoint = userLines.get(i);
                    boolean inStatus = isStart(condLine,prePoint,curPoint);

                    //4种状态，之前在不在，现在在不在
                    //之前在
                    if(inStatus){
                        if(!condLine.isInState()){
                            condLine.setInState(true);
                            condLine.setFirstPoint(prePoint);
                        }
                        if(i == userLines.size()-1){
                            condLine.setLastPoint(curPoint);
                            if(fuckByLine(condLine,helper)){
                                System.out.println("正在识别line:"+j+"success");
                                condLine.setPass(true);
                                break;
                            }else{
                                condLine.setInState(false);
                            }
                        }
                    }else{
                        if(condLine.isInState()){
                            //判断这大段是否符合condLine
                            condLine.setLastPoint(prePoint);
                            if(fuckByLine(condLine,helper)){
                                System.out.println("正在识别line:"+j+"success");
                                condLine.setPass(true);
                                condLine.setFirstPoint(null);
                                condLine.setLastPoint(null);
                                condLine.setInState(false);
                                break;
                            }else{
                                condLine.setInState(false);
                            }
                        }
                    }
                }
                j++;
            }
            if(isAllPass(letter)){
                return true;
            }

        }
        return false;
    }

    private static boolean isAllPass(Letter letter){
        boolean r = true;
        for (Line line : letter.getLines()) {
            r = r && line.isPass();

            line.setPass(false);
            line.setInState(false);
            line.setFirstPoint(null);
            line.setLastPoint(null);
        }
        return r;
    }

    private static void clearCondLine(Line line){
        line.setPass(false);
        line.setInState(false);
        line.setFirstPoint(null);
        line.setLastPoint(null);
    }


    private static boolean fuckByLine(Line condLine,Helper helper){

        float deg = getDeg(condLine.getFirstPoint(),condLine.getLastPoint());
        //先判断是否符合首尾角度
        if(!inRange(deg,condLine.getDegs())){
            System.out.println("deg...cur:"+deg+";expect:"+ JSONUtil.toJsonStr(condLine.getDegs()));
            return false;
        }
        if(!inRangeByDistance(condLine,helper)){
            return false;
        }

        return true;
    }


    private static boolean isStart(Line condLine, Point prePoint,Point curPoint) {

        Float deg = getDeg(prePoint,curPoint);
        return inRange(deg,condLine.getArrows());
    }

    private static boolean inRange(float deg,Range[] ranges){
        for (Range range : ranges) {
            if(deg >= range.getMin() && deg <= range.getMax()){
                return true;
            }
        }
        return false;
    }

    private static boolean inRange(float deg,Range range){
        if(deg >= range.getMin() && deg <= range.getMax()){
            return true;
        }
        return false;
    }

    private static boolean inRangeByDistance(Line line,Helper helper){

        float x = Math.abs(line.getLastPoint().getX() - line.getFirstPoint().getX());
        float y = Math.abs(line.getLastPoint().getY() - line.getFirstPoint().getY());
        if(!inRange(x / helper.getXDistance() , line.getCurXPercent())){
            System.out.println("比例:cur x:"+x / helper.getXDistance()+":"+ JSONUtil.toJsonStr(line.getCurXPercent()));
            return false;
        }
        if(!inRange(y / helper.getYDistance() , line.getCurYPercent())){
            System.out.println("比例:cur y:"+y / helper.getYDistance()+":"+ JSONUtil.toJsonStr(line.getCurYPercent()));
            return false;
        }

        if(line.getDiffYPercent() != null){
            if(!inRange(x / helper.getYDistance() , line.getDiffYPercent())){
                System.out.println("异轴比例:cur x:"+x / helper.getYDistance()+":"+ JSONUtil.toJsonStr(line.getDiffYPercent()));
                return false;
            }
        }
        if(line.getDiffXPercent() != null){
            if(!inRange(y / helper.getXDistance() , line.getDiffXPercent())){
                System.out.println("异轴比例2:cur y:"+y / helper.getXDistance()+":"+ JSONUtil.toJsonStr(line.getDiffXPercent()));
                return false;
            }
        }
        return true;
    }

    //预处理点信息
    private static HandList preDoPoint(List<Point> objList) {
        HandList handList = new HandList();
        List<Point> lineList = null;
        for (Point point : objList) {
            if ("ACTION_DOWN".equals(point.getAction())) {
                lineList = new ArrayList<>();
            }
            lineList.add(point);
            if ("ACTION_UP".equals(point.getAction())) {
                handList.getLineList().add(lineList);
            }
        }

        //预处理,去除一个地方多点的情况,排除干扰
        for (int j = 0; j < handList.getLineList().size(); j++) {
            List<Point> points = handList.getLineList().get(j);
            List<Point> newPoints = new ArrayList<>();
            Point stand = null;
            for (int i = 0; i < points.size(); i++) {
                if (i == 0) {
                    stand = points.get(i);
                    newPoints.add(points.get(i));
                    continue;
                }
                Point cur = points.get(i);
                if (Math.abs(cur.getX() - stand.getX()) >= 5 || Math.abs(cur.getY() - stand.getY()) >= 5) {
                    newPoints.add(points.get(i));
                    stand = points.get(i);
                }
            }
            //过滤单个点的笔画,可能是误点
            if (newPoints.size() > 1) {
                handList.getLineList().set(j, newPoints);
            } else {
                handList.getLineList().remove(j);
            }
        }
        return handList;
    }

    private static float getDeg(Point fromPoint,Point toPoint) {
        float deg = (float) Math.toDegrees(Math.atan2(toPoint.getY() - fromPoint.getY(), toPoint.getX() - fromPoint.getX()));
        if (deg < 0) {
            deg += 360;
        }
        return deg;
    }

    private static void setXY(Helper helper,List<Point> pointList){

        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;

        int i = 0;
        for (Point point : pointList) {
            if (i++ == 0) {
                minY = point.getY();
                maxY = point.getY();
                minX = point.getX();
                maxX = point.getX();
                continue;
            }
            if (point.getY() > maxY) {
                maxY = point.getY();
            }
            if (point.getY() < minY) {
                minY = point.getY();
            }
            if (point.getX() > maxX) {
                maxX = point.getX();
            }
            if (point.getX() < minX) {
                minX = point.getX();
            }
        }

        int xDistance = maxX - minX;
        int yDistance = maxY - minY;

        helper.setXDistance(xDistance==0?1:xDistance);
        helper.setYDistance(yDistance==0?1:yDistance);
    }
}







