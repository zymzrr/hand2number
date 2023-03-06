package com.hs.trace;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hs.trace.service.Point;
import com.hs.trace.service.muti.TraceMutiOcr;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String ss = "[{\"action\":\"down\",\"pageserial\":\"1536.736.31.61\",\"x\":1175,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1175,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1173,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1173,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1173,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1175,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1175,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1172,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1172,\"y\":855},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1168,\"y\":863},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1165,\"y\":875},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1161,\"y\":888},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1161,\"y\":894},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1164,\"y\":901},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1166,\"y\":902},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1170,\"y\":902},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1176,\"y\":898},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1181,\"y\":891},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1186,\"y\":882},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1186,\"y\":874},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1186,\"y\":861},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1181,\"y\":856},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1177,\"y\":855},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1174,\"y\":854},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1171,\"y\":856},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1167,\"y\":859},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1167,\"y\":861},{\"action\":\"up\",\"pageserial\":\"1536.736.31.61\",\"x\":1167,\"y\":861}],\"10441\":[{\"action\":\"down\",\"pageserial\":\"1536.736.31.61\",\"x\":1514,\"y\":854},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1514,\"y\":854},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1516,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1517,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1515,\"y\":852},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1515,\"y\":853},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1514,\"y\":856},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1508,\"y\":871},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1503,\"y\":883},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1500,\"y\":899},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1499,\"y\":914},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1500,\"y\":916},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1508,\"y\":915},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1520,\"y\":902},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1522,\"y\":891},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1522,\"y\":887},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1518,\"y\":886},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1508,\"y\":890},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1500,\"y\":895},{\"action\":\"move\",\"pageserial\":\"1536.736.31.61\",\"x\":1499,\"y\":896},{\"action\":\"up\",\"pageserial\":\"1536.736.31.61\",\"x\":1499,\"y\":896}]";
        JSONArray array = JSONUtil.parseArray(ss);
        List<Point> pointList = array.toList(Point.class);

        TraceMutiOcr traceMutiOcr = new TraceMutiOcr("G:\\突发奇想\\trace\\model-number.txt");
        String result = traceMutiOcr.ocr(pointList);
        System.out.println(result);
    }

    private static String regex(String content ,String regex){
        Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
}
