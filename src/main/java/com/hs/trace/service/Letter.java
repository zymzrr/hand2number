package com.hs.trace.service;

import lombok.Data;

import java.util.List;

@Data
class Letter{
    private String letter;
    private int asc;
    private List<Line> lines;
}