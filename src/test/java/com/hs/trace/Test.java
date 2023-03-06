package com.hs.trace;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.FileFilter;

public class Test {

  public static void main(String[] args) {
    File file = new File("C:\\Users\\Administrator\\Desktop\\web-site");
    File[] htmlList = file.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(".html");
      }
    });
    for(File html : htmlList){
      String content = FileUtil.readString(html,"UTF-8");
      
    }

  }

}
