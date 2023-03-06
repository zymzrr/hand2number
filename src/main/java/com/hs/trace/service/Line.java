package com.hs.trace.service;

import lombok.Data;

@Data
class Line{

    //笔迹
    private Range[] arrows;
    private Range[] degs;
    private Range curXPercent;
    private Range curYPercent;
    private Range diffXPercent;
    private Range diffYPercent;

    //用于判断
    //是否正在
    private boolean inState;
    private Point firstPoint;
    private Point lastPoint;
    private boolean pass;
}