package com.hs.trace.service.muti;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hs.trace.service.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

public class TraceMutiOcr {


    public TraceMutiOcr(String modelFilePath){
        init(modelFilePath);
    }

    List<Stroke> strokes = new ArrayList<>();
    Map<String,String> singles = new HashMap<>();
    Map<String,List<Compose>> composes = new HashMap<>();

    public TraceMutiOcr init(String modelFilePath) {

        List<String> lines = FileUtil.readLines(modelFilePath, "UTF-8");
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
                return o1.getAsc()- o2.getAsc();
            }
        });

        System.out.println(strokes);
        return this;
    }

    public String ocr(List<Point> objList) {
        //少于4个点的pass
        if (objList.size() < 4) {
            return null;
        }
        HandList handList = preDoPoint(objList);
        String letter = getResult(handList);

        return letter;
    }


    public String getResult(HandList handList) {

        for (int i = 0; i< handList.getLineList().size();i++) {

            List<Point> userLine = handList.getLineList().get(i);
            Helper helper = new Helper();
            setXY(helper, userLine);
            handList.getHelperList()[i] = helper;

            for (Stroke stroke : strokes) {

                if (fuckByTrace(stroke, userLine, helper)) {
                    //change-1
                    if(handList.getStrokeList().get(i) == null){
                        List<String> strokeList = new ArrayList<>();
                        strokeList.add(stroke.getStroke());
                        handList.getStrokeList().add(strokeList);
                    }else{
                        handList.getStrokeList().get(i).add(stroke.getStroke());
                    }

                    //先从组合判断
                    boolean find = fuckByCompose(stroke.getStroke(),handList,i);
                    if(!find){
                        //组合里找不到，然后从单个里判断
                        fuckBySingle(stroke.getStroke(),handList,i);
                    }
                    //change-4
                    if(find){
                        break;
                    }
                    //change-1
                    //break;
                }

                //change-5
                if(handList.getWordList()[i] != null){
                    break;
                }
//                else{
//                    handList.getStrokeList()[i] = null;
//                }
            }

            System.out.println("=====================================");
        }
        return getWords(handList);
    }

    private String getWords(HandList handList){
        StringBuffer sb = new StringBuffer();
        for (String s : handList.getWordList()) {
            sb.append(s == null?"":s);
        }
        return sb.toString();
    }

    private boolean fuckByCompose(String strokeName,HandList handList,int i){

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
                //change-1
                List<String> oriStrokes = handList.getStrokeList().get(handIndex);
                //先看笔画对不对
                if(!oriStrokes.contains(strokeComposeVo.getStroke())){
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
                //change-2
                String w = handList.getWordList()[i];
                //change-3
                if(w == null || "single".equals(handList.getWordFromList()[i])){
                    for (int j = 0;j< compose.getStrokes().size();j++) {
                        handList.getWordList()[i - size + 1 + j] = null;
                    }
                    handList.getWordList()[i] = compose.getLetter();
                }
                //change-5 //如果找到了 要看之前的笔画有没有被用到，如果用到了就要再往上查询
                handList.getStrokeUsedList()[i] = compose.getStrokes().size();
                for (int t=1; t< handList.getStrokeUsedList()[i]; t++){
                    int y = i - t ;
                    for (int x=1;x < handList.getStrokeUsedList()[y]; x++){
                        for (String sn : handList.getStrokeList().get(y-x)) {
                            if(fuckByCompose(sn,handList,y-x)){
                                break;
                            }
                        }
                    }
                    handList.getStrokeUsedList()[y] = 0;
                }
                //
                return true;
            }
        }

        return false;
    }

    private static boolean fuckComposePostion(Helper base,Helper current,StrokeComposeVo stand){

        System.out.println("组合识别："+stand.getStroke());

        float xMin = base.getMinX() + base.getXDistance() * stand.getX().getRange().getMin();
        float xMax = base.getMinX() + base.getXDistance() * stand.getX().getRange().getMax();
        float yMin = base.getMinY() + base.getYDistance() * stand.getY().getRange().getMin();
        float yMax = base.getMinY() + base.getYDistance() * stand.getY().getRange().getMax();

        if("in".equals(stand.getX().getMode())){

            if(current.getMinX() >= xMin && current.getMaxX() <= xMax ){

            }else{
                System.out.println("组合识别in：cur x "+JSONUtil.toJsonStr(current)+"expect xMin"+xMin+",xMax"+xMax);
                return false;
            }
        }
        else if("contain".equals(stand.getX().getMode())){

            if(current.getMinX() <= xMin && current.getMaxX() >= xMax ){

            }else{
                System.out.println("组合识别contain：cur x "+JSONUtil.toJsonStr(current)+"expect xMin"+xMin+",xMax"+xMax);
                return false;
            }
        }else if("touch".equals(stand.getX().getMode())){

            if(current.getMaxX() < xMin || current.getMinX() > xMax ){
                System.out.println("组合识别touch：cur x "+JSONUtil.toJsonStr(current)+"expect xMin"+xMin+",xMax"+xMax);
                return false;
            }
        }

        if("in".equals(stand.getY().getMode())){

            if(current.getMinY() >= yMin && current.getMaxY() <= yMax ){

            }else{
                System.out.println("组合识别in：cur y "+JSONUtil.toJsonStr(current)+"expect yMin"+yMin+",yMax"+yMax);
                return false;
            }
        }
        else if("contain".equals(stand.getY().getMode())){

            if(current.getMinY() <= yMin && current.getMaxY() >= yMax ){

            }else{
                System.out.println("组合识别contain：cur y "+JSONUtil.toJsonStr(current)+"expect yMin"+yMin+",yMax"+yMax);
                return false;
            }
        }
        else if("touch".equals(stand.getY().getMode())){

            if(current.getMaxY() < yMin || current.getMinY() > yMax ){
                System.out.println("组合识别touch：cur y "+JSONUtil.toJsonStr(current)+"expect yMin"+yMin+",yMax"+yMax);
                return false;
            }
        }

        return true;
    }


    private void fuckBySingle(String strokeName,HandList handList,int i){
        String letter = singles.get(strokeName);
        if(letter != null){
            String w = handList.getWordList()[i];
            if(w == null){
                //change-3
                handList.getWordFromList()[i] = "single";
                handList.getWordList()[i] = letter;
            }
        }
    }


    private static boolean fuckByTrace(Stroke stroke, List<Point> userLines, Helper helper) {

        System.out.println("正在识别笔画stroke:" + stroke.getStroke());
        int i = 0;
        int j = 0;
        for (Line condLine : stroke.getLines()) {
            System.out.println("正在识别line:" + j);

            //change-6
            condLine.setLinePoints(new ArrayList<>());

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
                        //change-6
                        condLine.getLinePoints().add(prePoint);
                    }
                    //change-6
                    condLine.getLinePoints().add(curPoint);
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

    private static void clearCondLine(Line line) {
        line.setPass(false);
        line.setInState(false);
        line.setFirstPoint(null);
        line.setLastPoint(null);
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

        //change-6
        if (!inRangeByArea(condLine, helper)) {
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


    //change-6 整个方法都是新加的
    private static boolean inRangeByArea(Line line, Helper helper){

        if(line.getArea() == null){
            return true;
        }

        float xMin = helper.getMinX() + helper.getXDistance() * line.getArea().getXRange().getMin();
        float xMax = helper.getMinX() + helper.getXDistance() * line.getArea().getXRange().getMax();
        float yMin = helper.getMinY() + helper.getYDistance() * line.getArea().getYRange().getMin();
        float yMax = helper.getMinY() + helper.getYDistance() * line.getArea().getYRange().getMax();

        for (Point point : line.getLinePoints()) {
            if(point.getX() >= xMin && point.getX() <= xMax
                && point.getY() >= yMin && point.getY() <= yMax ){
                return true;
            }
        }

        System.out.println("线条识别touch："+JSONUtil.toJsonStr(line.getArea())+"expect xMin"+xMin+",xMax"+xMax);

        return false;
    }
//
    //预处理点信息
    private static HandList preDoPoint(List<Point> objList) {
        HandList handList = new HandList();
        List<Point> lineList = null;
        for (Point point : objList) {
            if ("down".equals(point.getAction())) {
                lineList = new ArrayList<>();
            }
            lineList.add(point);
            if ("up".equals(point.getAction())) {
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
        //change-3
        handList.setWordFromList(new String[handList.getLineList().size()]);
        handList.setHelperList(new Helper[handList.getLineList().size()]);

        //change-5
        handList.setStrokeUsedList(new int[handList.getLineList().size()]);

        //change-1
        List<List<String>> strokeList = new ArrayList<>();
        for (List<Point> points : handList.getLineList()) {
            strokeList.add(new ArrayList<>());
        }
        handList.setStrokeList(strokeList);
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

@Data
class Compose {
    private String letter;
    private List<StrokeComposeVo> strokes;
}

@Data
class Line {
    //笔迹
    private Range[] arrows;
    private Range[] degs;
    private Range curXPercent;
    private Range curYPercent;
    private Range diffXPercent;
    private Range diffYPercent;
    //change-6
    private LineArea area;

    //用于判断
    //是否正在
    private boolean inState;
    private Point firstPoint;
    private Point lastPoint;
    private boolean pass;
    //change-6
    private List<Point> linePoints;
}

@Data
class Helper {

    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    private int xDistance;
    private int yDistance;
}

@Data
class HandList {
    private List<List<Point>> lineList = new ArrayList<>();
    //change-1
    private List<List<String>> strokeList;
    private String[] wordList;
    //change-3
    private String[] wordFromList;
    private Helper[] helperList;
    //change-5
    private int[] strokeUsedList;
}

@Data
class StrokeComposeVo {

    private String stroke;
    private String role;
    private Position x;
    private Position y;
}

@Data
class Position{
    private String mode;
    private Range range;
}

@Data
class Stroke {
    private String stroke;
    private Integer asc;
    private List<Line> lines;
}

@AllArgsConstructor
@Data
class Range {
    float min;
    float max;
}

@Data
class LineArea{
    private String mode;
    private Range xRange;
    private Range yRange;
}

