package com.hs.trace.controller;

import com.hs.trace.common.Result;
import com.hs.trace.common.Results;
import com.hs.trace.service.Point;
import com.hs.trace.service.TraceOcr;
import com.hs.trace.service.muti.TraceMutiOcr;
import com.hs.trace.service.muti.TraceMutiOcrForVXNotCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试用模块
 * </p>
 *
 * @author yuanzong
 * @since 2020-02-19
 */
@Slf4j
@RestController
@RequestMapping("/api/trace")
public class TraceController {

    @Value("${file.model}")
    private String modelDir;

    static TraceMutiOcr ocr = null;

    @PostConstruct
    public void init(){
        ocr = new TraceMutiOcr(modelDir+"/model-number.txt");
    }


    @PostMapping("/reload.json")
    private Result reload() {
        ocr = new TraceMutiOcr(modelDir+"/model-number.txt");
        return Results.success();
    }


    @PostMapping("/bjocrnumber.json")
    private Result bjocrnumber(@RequestBody List<Point> objList) {
        long t1 = System.currentTimeMillis();
        String letter = TraceOcr.ocrNumber(objList);
        Map map = new HashMap();
        map.put("letter",letter);
        map.put("time",System.currentTimeMillis()-t1);
        return Results.success(map);
    }


    @PostMapping("/bjocrletter.json")
    private Result bjocrletter(@RequestBody List<Point> objList) {
        long t1 = System.currentTimeMillis();
        String letter = TraceOcr.ocrLetter(objList);
        Map map = new HashMap();
        map.put("letter",letter);
        map.put("time",System.currentTimeMillis()-t1);
        return Results.success(map);
    }

    @PostMapping("/bjocrraw.json")
    private Result bjocrraw(@RequestBody List<Point> objList) {
        long t1 = System.currentTimeMillis();
        String letter = TraceOcr.ocrRaw(objList);
        Map map = new HashMap();
        map.put("letter",letter);
        map.put("time",System.currentTimeMillis()-t1);
        return Results.success(map);
    }



    @PostMapping("/mutinumber.json")
    private Result mutinumber(@RequestBody List<Point> objList) {
        long t1 = System.currentTimeMillis();
        String letter = ocr.ocr(objList);
        Map map = new HashMap();
        map.put("letter",letter);
        map.put("time",System.currentTimeMillis()-t1);
        return Results.success(map);
    }

    @PostMapping("/mutiraw.json")
    private Result mutiraw(@RequestBody List<Point> objList) {
        long t1 = System.currentTimeMillis();
        String letter = TraceMutiOcrForVXNotCommon.ocrRaw(objList);
        Map map = new HashMap();
        map.put("letter",letter);
        map.put("time",System.currentTimeMillis()-t1);
        return Results.success(map);
    }

//    @PostMapping("/mutiletter.json")
//    private Result mutiletter(@RequestBody List<Point> objList) {
//        long t1 = System.currentTimeMillis();
//        String letter = TraceMutiOcr.ocr(objList,"letter");
//        Map map = new HashMap();
//        map.put("letter",letter);
//        map.put("time",System.currentTimeMillis()-t1);
//        return Results.success(map);
//    }

}