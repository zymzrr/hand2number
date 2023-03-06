package com.hs.trace.service.muti;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hs.trace.service.HandList;
import com.hs.trace.service.Helper;
import com.hs.trace.service.Point;

import java.util.*;

public class TraceMutiOcrForVXNotCommon {

    public static void main(String[] args) {

        String s = "[  {\"pen_type\":2,\"x\":1083,\"y\":2650,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_down\",\"time\":1655091243048,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1084,\"y\":2654,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243072,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1110,\"y\":2714,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243112,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1194,\"y\":2736,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243144,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1265,\"y\":2715,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243160,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1333,\"y\":2684,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243176,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1476,\"y\":2608,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243224,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1493,\"y\":2602,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243240,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1494,\"y\":2601,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243264,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1469,\"y\":2626,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_move\",\"time\":1655091243284,\"sortNo\":\"绗�1棰�\"},{\"pen_type\":2,\"x\":1384,\"y\":2669,\"force\":-1,\"page\":\"1536.717.10.75\",\"isButtonDown\":true,\"actionPen\":\"pen_up\",\"time\":1655091243300,\"sortNo\":\"绗�1棰�\"}]";
        List<Point> objList =  JSONUtil.toList(JSONUtil.parseArray(s),Point.class);
        System.out.println(ocrRaw(objList));

    }

    static List<Stroke> strokes = new ArrayList<>();
    static Map<String,String> singles = new HashMap<>();
    static Map<String,List<Compose>> composes = new HashMap<>();

    static {
        init();
    }

    public static void init() {

        List<String> lines = FileUtil.readLines("G:\\突发奇想\\trace\\modelvx.txt", "UTF-8");
        String mode = null;
        for (String line : lines) {
            if(line.startsWith("--")){
                continue;
            }
            if ("[stroke]".equals(line)) {
                mode = "stroke";
                continue;
            } else if ("[single]".equals(line)) {
                mode = "single";
                continue;
            } else if ("[compose]".equals(line)) {
                mode = "compose";
                continue;
            }
            if (mode == null || line.equals("")) {
                continue;
            } else if ("stroke".equals(mode)) {
                Stroke letter = JSONUtil.toBean(line, Stroke.class);
                strokes.add(letter);
            } else if ("single".equals(mode)) {
                JSONObject obj = JSONUtil.parseObj(line);
                String letter = obj.getStr("letter");
                JSONArray arr = obj.getJSONArray("strokes");
                for (int i = 0;i<arr.size() ;i++) {
                    singles.put(arr.getJSONObject(i).getStr("stroke"),letter);
                }
            } else if ("compose".equals(mode)) {
                Compose compose = JSONUtil.toBean(line, Compose.class);
                //查询最后一笔的名称
                String stroke = compose.getStrokes().get(compose.getStrokes().size()-1).getStroke();
                List<Compose> list = composes.get(stroke);
                if(list == null){
                    list = new ArrayList<>();
                }
                list.add(compose);
                composes.put(stroke,list);
            }
        }
        strokes.sort(new Comparator<Stroke>() {
            @Override
            public int compare(Stroke o1, Stroke o2) {
                return o1.getAsc() - o2.getAsc();
            }
        });
    }



    //识别单个数字
    public static String ocrRaw(List<Point> objList) {
        if (objList.size() < 4) {
            return null;
        }
        HandList handList = preDoPoint(objList);
        if(handList.getLineList().size() > 3){
            return "";
        }
        return getNumber(handList);
    }


    public static String getNumber(HandList handList) {

        for (int i = 0; i< handList.getLineList().size();i++) {

            List<Point> userLine = handList.getLineList().get(i);
            Helper helper = new Helper();
            setXY(helper, userLine);
            handList.getHelperList()[i] = helper;

            for (Stroke stroke : strokes) {
                //
                if (fuckByTrace(stroke, userLine, helper)) {

                    handList.getStrokeList()[i] = stroke.getStroke();
                    //先从组合判断
                    boolean find = fuckByCompose(stroke.getStroke(),handList,i);
                    if(!find){
                        //组合里找不到，然后从单个里判断
                        fuckBySingle(stroke.getStroke(),handList,i);
                    }
                    break;
                }else{
                    handList.getStrokeList()[i] = null;
                }
            }
        }
        return getWords(handList);
    }

    private static String getWords(HandList handList){
        String sb = null;
        for (int i = 0;i<handList.getWordList().length;i++) {
            if(i < 2 && handList.getWordList()[i] != null){
                sb = handList.getWordList()[i];
            }
        }
        return sb == null?"":sb;
    }

    private static boolean fuckByCompose(String strokeName,HandList handList,int i){
        List<Compose> componse = composes.get(strokeName);
        if(componse == null || componse.size() == 0){
            return false;
        }

        for (Compose compose : componse) {
            int size = compose.getStrokes().size();
            if(i < size-1){
                continue;
            }

            Helper base = null;
            //查找base的数据
            for (int j = 0;j< compose.getStrokes().size();j++) {
                int handIndex = i - size + 1 + j;
                StrokeComposeVo strokeComposeVo = compose.getStrokes().get(j);
                if("base".equals(strokeComposeVo.getRole())){
                    base = handList.getHelperList()[handIndex];
                    break;
                }
            }
            if(base == null){
                return false;
            }

            boolean allPass = true;
            for (int j = 0;j< compose.getStrokes().size();j++) {

                StrokeComposeVo strokeComposeVo = compose.getStrokes().get(j);
                int handIndex = i - size + 1 + j;
                String oriStroke = handList.getStrokeList()[handIndex];
                //先看笔画对不对
                if(!strokeComposeVo.getStroke().equals(oriStroke)){
                    allPass = false;
                    break;
                }

                if("base".equals(strokeComposeVo.getRole())){
                    continue;
                }

                //开始判断是否符合位置信息
                //那笔的位置信息是否符合
                Helper current = handList.getHelperList()[handIndex];
                //标准

                if(fuckComposePostion(base,current,strokeComposeVo)){
                    continue;
                }else{
                    allPass = false;
                    break;
                }
            }

            //如果某个组合通过了，不再往下匹配了，
            if(allPass){
                for (int j = 0;j< compose.getStrokes().size();j++) {
                    handList.getWordList()[i - size + 1 + j] = null;
                }
                handList.getWordList()[i] = compose.getLetter();
                return true;
            }
        }

        return false;
    }

    private static boolean fuckComposePostion(Helper base,Helper current,StrokeComposeVo stand){

        float xMin = base.getMinX() + base.getXDistance() * stand.getX().getRange().getMin();
        float xMax = base.getMinX() + base.getXDistance() * stand.getX().getRange().getMax();
        float yMin = base.getMinY() + base.getYDistance() * stand.getY().getRange().getMin();
        float yMax = base.getMinY() + base.getYDistance() * stand.getY().getRange().getMax();

        if("in".equals(stand.getX().getMode())){

            if(current.getMinX() >= xMin && current.getMaxX() <= xMax ){

            }else{
                return false;
            }
        }
        else if("contain".equals(stand.getX().getMode())){

            if(current.getMinX() <= xMin && current.getMaxX() >= xMax ){

            }else{
                return false;
            }
        }else if("touch".equals(stand.getX().getMode())){

            if(current.getMaxX() < xMin || current.getMinX() > xMax ){
                return false;
            }
        }

        if("in".equals(stand.getY().getMode())){

            if(current.getMinY() >= yMin && current.getMaxY() <= yMax ){

            }else{
                return false;
            }
        }
        else if("contain".equals(stand.getY().getMode())){

            if(current.getMinY() <= yMin && current.getMaxY() >= yMax ){

            }else{
                return false;
            }
        }
        else if("touch".equals(stand.getY().getMode())){

            if(current.getMaxY() < yMin || current.getMinY() > yMax ){
                return false;
            }
        }

        return true;
    }


    private static void fuckBySingle(String strokeName,HandList handList,int i){
        String letter = singles.get(strokeName);
        if(letter != null){
            handList.getWordList()[i] = letter;
        }
    }



    private static boolean fuckByTrace(Stroke stroke, List<Point> userLines, Helper helper) {

        System.out.println("正在识别笔画stroke:" + stroke.getStroke());
        int i = 0;
        int j = 0;
        for (Line condLine : stroke.getLines()) {
            System.out.println("正在识别line:" + j);
            for (; i < userLines.size(); i++) {
                if (i == 0) {
                    continue;
                }
                //2个点间的角度
                Point prePoint = userLines.get(i - 1);
                Point curPoint = userLines.get(i);
                boolean inStatus = isStart(condLine, prePoint, curPoint);

                //4种状态，之前在不在，现在在不在
                //之前在
                if (inStatus) {
                    if (!condLine.isInState()) {
                        condLine.setInState(true);
                        condLine.setFirstPoint(prePoint);
                    }
                    if (i == userLines.size() - 1) {
                        condLine.setLastPoint(curPoint);
                        if (fuckByLine(condLine, helper)) {
                            System.out.println("正在识别line:" + j + "success");
                            condLine.setPass(true);
                            break;
                        } else {
                            condLine.setInState(false);
                        }
                    }
                } else {
                    if (condLine.isInState()) {
                        //判断这大段是否符合condLine
                        condLine.setLastPoint(prePoint);
                        if (fuckByLine(condLine, helper)) {
                            System.out.println("正在识别line:" + j + "success");
                            condLine.setPass(true);
                            condLine.setFirstPoint(null);
                            condLine.setLastPoint(null);
                            condLine.setInState(false);
                            break;
                        } else {
                            condLine.setInState(false);
                        }
                    }
                }
            }
            j++;
        }
        if (isAllPass(stroke)) {
            return true;
        }
        return false;
    }

    private static boolean isAllPass(Stroke stroke) {
        boolean r = true;
        for (Line line : stroke.getLines()) {
            r = r && line.isPass();
            line.setPass(false);
            line.setInState(false);
            line.setFirstPoint(null);
            line.setLastPoint(null);
        }
        return r;
    }




    private static boolean fuckByLine(Line condLine, Helper helper) {

        float deg = getDeg(condLine.getFirstPoint(), condLine.getLastPoint());
        //先判断是否符合首尾角度
        if (!inRange(deg, condLine.getDegs())) {
            System.out.println("deg...cur:" + deg + ";expect:" + JSONUtil.toJsonStr(condLine.getDegs()));
            return false;
        }
        if (!inRangeByDistance(condLine, helper)) {
            return false;
        }

        return true;
    }


    private static boolean isStart(Line condLine, Point prePoint, Point curPoint) {

        Float deg = getDeg(prePoint, curPoint);
        return inRange(deg, condLine.getArrows());
    }

    private static boolean inRange(float deg, Range[] ranges) {
        for (Range range : ranges) {
            if (deg >= range.getMin() && deg <= range.getMax()) {
                return true;
            }
        }
        return false;
    }

    private static boolean inRange(float deg, Range range) {
        if (deg >= range.getMin() && deg <= range.getMax()) {
            return true;
        }
        return false;
    }

        private static boolean inRangeByDistance(Line line, Helper helper){

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
//
    //预处理点信息
    private static HandList preDoPoint(List<Point> objList) {
        HandList handList = new HandList();
        List<Point> lineList = null;
        for (Point point : objList) {
            if ("pen_down".equals(point.getActionPen())) {
                lineList = new ArrayList<>();
            }
            lineList.add(point);
            if ("pen_up".equals(point.getActionPen())) {
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

        handList.setWordList(new String[handList.getLineList().size()]);
        handList.setHelperList(new Helper[handList.getLineList().size()]);
        handList.setStrokeList(new String[handList.getLineList().size()]);
        return handList;
    }

    private static float getDeg(Point fromPoint, Point toPoint) {
        float deg = (float) Math.toDegrees(Math.atan2(toPoint.getY() - fromPoint.getY(), toPoint.getX() - fromPoint.getX()));
        if (deg < 0) {
            deg += 360;
        }
        return deg;
    }

    private static void setXY(Helper helper, List<Point> pointList) {

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

        helper.setMinX(minX);
        helper.setMaxX(maxX);
        helper.setMinY(minY);
        helper.setMaxY(maxY);

        helper.setXDistance(xDistance == 0 ? 1 : xDistance);
        helper.setYDistance(yDistance == 0 ? 1 : yDistance);
    }
}







