package com.hs.trace.service;

import lombok.Data;

@Data
public class Point {
    private String action;
    private String actionPen;
    private int x;
    private int y;
}