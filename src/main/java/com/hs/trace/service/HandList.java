package com.hs.trace.service;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HandList {

    private List<List<Point>> lineList = new ArrayList<>();

    private String[] strokeList;
    private String[] wordList;
    private Helper[] helperList;
}