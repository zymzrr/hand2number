package com.hs.trace.service.formula;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.java.Log;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.*;


@Log
public class FormulaUtil {

  private static String ERROR_STR = "ERROR";
  private static int index = 0;

  public static void main(String[] args) throws Exception {
    String latex = mml2Latex("<math><?xml version=\"1.0\" encoding=\"UTF-16\"?><mml:math xmlns:mml=\"http://www.w3.org/1998/Math/MathML\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"><mml:mi mathvariant=\"normal\">f</mml:mi><mml:mo>(</mml:mo><mml:mi mathvariant=\"normal\">x</mml:mi><mml:mo>)</mml:mo><mml:mo>=</mml:mo><mml:mo>-</mml:mo><mml:msup><mml:mrow><mml:mi  >x</mml:mi><mml:mi >x</mml:mi></mml:mrow><mml:mrow><mml:mn>3</mml:mn></mml:mrow></mml:msup><mml:mo>+</mml:mo><mml:mn>3</mml:mn><mml:mi mathvariant=\"normal\">f</mml:mi><mml:mi mathvariant=\"normal\">'</mml:mi><mml:mo>(</mml:mo><mml:mn>2</mml:mn><mml:mo>)</mml:mo><mml:mi mathvariant=\"normal\">x</mml:mi><mml:mo>+</mml:mo><mml:mrow><mml:msubsup><mml:mo stretchy=\"false\">∫</mml:mo><mml:mrow><mml:mn>0</mml:mn></mml:mrow><mml:mrow><mml:mn>2</mml:mn></mml:mrow></mml:msubsup><mml:mrow><mml:mi mathvariant=\"normal\">f</mml:mi><mml:mo>(</mml:mo><mml:mi mathvariant=\"normal\">x</mml:mi><mml:mo>)</mml:mo><mml:mi mathvariant=\"normal\">d</mml:mi><mml:mi mathvariant=\"normal\">x</mml:mi></mml:mrow></mml:mrow></mml:math></math>");
    System.err.println(latex);

  }

  private static List<String> speWords = new ArrayList<>();

  static {
    speWords.add("(");
    speWords.add("[");
    speWords.add("\\{");
    speWords.add("|");
    speWords.add(")");
    speWords.add("]");
    speWords.add("\\}");
  }

  public static String mml2Latex(String mml) {
    log.info("index:" + index++);
    log.info("mml:"+mml);
    if (StrUtil.isBlank(mml)) {
      return ERROR_STR;
    }
    mml = preDo(mml);
    FormMulaHelper helper = new FormMulaHelper();
    try {
      SAXReader saxReader = new SAXReader();
      Document document = saxReader.read(new ByteArrayInputStream(mml.getBytes()));
      Element rootElement = document.getRootElement();
      Iterator it = rootElement.elementIterator();
      while (it.hasNext()) {
        Element item = (Element) it.next();
        parseElement(item, helper);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    log.info("latex:"+helper.getSb().toString());
    return helper.getSb().toString();
  }

  public static String preDo(String mml) {
    mml = mml.replaceAll("<\\?xml.*?>", "");
    mml = mml.trim();
    return mml;
  }

  public static void parseElement(Element element, FormMulaHelper helper) throws Exception {

    String elementName = element.getName();

    String[] symbols = new String[]{"mglyph", "mi", "mn", "mo", "ms", "mspace", "mtext"};
    String[] undos = new String[]{"maligngroup", "malignmark", "mlabeledtr", "mlongdiv",
        "mscarries", "mscarry", "msgroup", "msline", "msrow", "mstack", "none"};

    if (Arrays.asList(symbols).contains(elementName)) {
      parseSymbol(element, helper);
    }
    //布局
    else if (StrUtil.equals("menclose", elementName)) {
      // todo
      // https://developer.mozilla.org/zh-CN/docs/Web/MathML/Element/menclose
    } else if (StrUtil.equals("merror", elementName)) {
      // todo
      // https://developer.mozilla.org/zh-CN/docs/Web/MathML/Element/merror
    } else if (StrUtil.equals("mfenced", elementName)) {
      parseFenced(element, helper);
    } else if (StrUtil.equals("mfrac", elementName)) {
      parseMfrac(element, helper);
    } else if (StrUtil.equals("mpadded", elementName)) {
      parseMpadded(element, helper);
    } else if (StrUtil.equals("msup", elementName)) {
      parseMsup(element, helper);
    } else if (StrUtil.equals("msub", elementName)) {
      parseMsub(element, helper);
    } else if (StrUtil.equals("msubsup", elementName)) {
      parseMsubsup(element, helper);
    } else if (StrUtil.equals("menclose", elementName)) {
      parseMenclose(element, helper);
    } else if (StrUtil.equals("merror", elementName)) {
      parseMerror(element, helper);
    } else if (StrUtil.equals("mphantom", elementName)) {
      parseMphantom(element, helper);
    } else if (StrUtil.equals("mroot", elementName)) {
      parseMroot(element, helper);
    } else if (StrUtil.equals("mrow", elementName)) {
      parseMrow(element, helper);
    } else if (StrUtil.equals("msqrt", elementName)) {
      parseMsqrt(element, helper);
    } else if (StrUtil.equals("mstyle", elementName)) {
      parseMstyle(element, helper);
    } else if (StrUtil.equals("mmultiscripts", elementName)) {
      parseMmultiscripts(element, helper);
    } else if (StrUtil.equals("mover", elementName)) {
      parseMover(element, helper);
    } else if (StrUtil.equals("munder", elementName)) {
      parseMunder(element, helper);
    } else if (StrUtil.equals("munderover", elementName)) {
      parseMunderover(element, helper);
    } else if (StrUtil.equals("mtable", elementName)) {
      parseMtable(element, helper);
    }

//    else if (Arrays.asList(undos).contains(elementName)){
//      //undo
//    }
    else {
      Iterator it = element.elementIterator();
      while (it.hasNext()) {
        Element item = (Element) it.next();
        parseElement(item, helper);
      }
    }
  }


  public static void parseSymbol(Element element, FormMulaHelper helper) {
    String txt = element.getText();
    if(StrUtil.isBlank(txt)){
        return;
    }
    if(helper.isInText()){
        helper.append(txt);
        return;
    }
    boolean style = false;
    String m = element.attributeValue("mathvariant");
    if(StrUtil.isNotBlank(m)){
        if(m.equals("normal")){
            style = true;
            helper.append("\\text");
        }
        if(m.equals("bold")){
            style = true;
            helper.append("\\textbf");
        }
    }
    if (StrUtil.isNotBlank(txt)) {
      txt = txt.trim();
      boolean isCond = txt.length() > 1;
      if (isCond || style) {
        helper.append("{");
      }
      helper.append(key(txt));
      if (isCond || style) {
        helper.append("}");
      }
    }
  }


  public static void parseFenced(Element element, FormMulaHelper helper) throws Exception {

    String open = element.attributeValue("open");
    String close = element.attributeValue("close");
    if(open == null){
      open = "(";
    }
    if(close == null){
      close = ")";
    }
    open = key(open);
    close = key(close);

    boolean hasLeft = false;
    if(speWords.contains(open)){
      open = "\\left " + open;
      hasLeft = true;
    }

    if(close.equals("") && hasLeft){
      close = "\\right. ";
    }else if(speWords.contains(close) && hasLeft){
      close = "\\right "+close;
    }

    String separators = element.attributeValue("separators");
    char[] chars = new char[1];
    chars[0] = ' ';
    if(separators != null){
      chars = separators.toCharArray();;
    }

    helper.append(open + " ");
    Iterator it = element.elementIterator();
    int i = 0;
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
      if (it.hasNext()) {
        helper.append(i >= chars.length ? String.valueOf(chars[chars.length - 1]) : String.valueOf(chars[i]));
      }
      i++;
    }
    helper.append(" " + close);
  }

  public static void parseMsub(Element element, FormMulaHelper helper) throws Exception {

    Iterator it = element.elementIterator();
    int i = 0;
    while (it.hasNext()) {
      Element item = (Element) it.next();
      if (i == 0) {
        helper.append("{");
        parseElement(item, helper);
        helper.append("}");
      } else if (i == 1) {
        helper.append("_{");
        parseElement(item, helper);
        helper.append("}");
      }
      i++;
    }
  }

  public static void parseMsup(Element element, FormMulaHelper helper) throws Exception {

    Iterator it = element.elementIterator();
    int i = 0;
    while (it.hasNext()) {
      Element item = (Element) it.next();
      if (i == 0) {
        helper.append("{");
        parseElement(item, helper);
        helper.append("}");
      } else if (i == 1) {
        helper.append("^{");
        parseElement(item, helper);
        helper.append("}");
      }
      i++;
    }
  }

  public static void parseMsubsup(Element element, FormMulaHelper helper) throws Exception {

    Iterator it = element.elementIterator();
    int i = 0;
    while (it.hasNext()) {
      Element item = (Element) it.next();
      if (i == 0) {
        helper.append("{");
        parseElement(item, helper);
        helper.append("}");
      } else if (i == 1) {
        helper.append("_");
        parseElement(item, helper);
        helper.append("}");
      } else if (i == 2) {
        helper.append("^{");
        parseElement(item, helper);
        helper.append("}");
      }
      i++;
    }
  }

  public static void parseMfrac(Element element, FormMulaHelper helper) throws Exception {

    helper.append("\\frac");
    Iterator it = element.elementIterator();
    int i = 0;
    while (it.hasNext()) {
      Element item = (Element) it.next();
      if (i == 0) {
        helper.append("{");
        parseElement(item, helper);
        helper.append("}");
      } else if (i == 1) {
        helper.append("{");
        parseElement(item, helper);
        helper.append("}");
      }
      i++;
    }
  }

  public static void parseMenclose(Element element, FormMulaHelper helper) throws Exception {

    //应按照notation做不同的转化，实际过程，没有转化规则
    String notation = element.attributeValue("notation");
    helper.append("\\overline");
    Iterator it = element.elementIterator();
    int i = 0;
    helper.append("{");
    while (it.hasNext()) {
      Element item = (Element) it.next();
      if (i == 0) {
        helper.append(")");
      }
      parseElement(item, helper);
      i++;
    }
    helper.append("}");
  }

  public static void parseMerror(Element element, FormMulaHelper helper) throws Exception {
    //error latex没有对应的
    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
    }
  }

  public static void parseMpadded(Element element, FormMulaHelper helper) throws Exception {
    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
    }
  }

  public static void parseMphantom(Element element, FormMulaHelper helper) throws Exception {
    helper.append("\\phantom");
    Iterator it = element.elementIterator();
    helper.append("{");
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
    }
    helper.append("}");
  }

  public static void parseMroot(Element element, FormMulaHelper helper) throws Exception {
    helper.append("\\sqrt");
    int i = 0;
    Iterator it = element.elementIterator();

    Element item0 = null;
    Element item1 = null;
    while (it.hasNext()) {
      if (i == 0) {
        item0 = (Element) it.next();
      } else if (i == 1) {
        item1 = (Element) it.next();
      }
      i++;
    }

    if (item1 != null) {
      helper.append("[");
      parseElement(item1, helper);
      helper.append("]");
    }
    if (item0 != null) {
      parseElement(item0, helper);
    }
  }

  public static void parseMrow(Element element, FormMulaHelper helper) throws Exception {
    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
    }
  }

  public static void parseMsqrt(Element element, FormMulaHelper helper) throws Exception {
    helper.append("\\sqrt");

    helper.addTempData();
    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element tmp = (Element) it.next();
      parseElement(tmp, helper);
    }
    String tmpData = helper.getTempData();

    helper.append("{");
    helper.append(tmpData);
    helper.append("}");
  }

  public static void parseMstyle(Element element, FormMulaHelper helper) throws Exception {
    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
    }
  }

  public static void parseMmultiscripts(Element element, FormMulaHelper helper) throws Exception {
    Iterator it = element.elementIterator();
    boolean before = false;
    Element mainElement = null;
    List<Element> beforeList = new ArrayList<>();
    List<Element> afterList = new ArrayList<>();
    int i = 0;
    while (it.hasNext()) {
      Element ele = (Element) it.next();
      if(i++ == 0){
        mainElement = ele;
        continue;
      }
      if (StrUtil.equals(ele.getName(), "mprescripts")) {
        before = true;
        continue;
      }
      if(before){
        beforeList.add(ele);
      }else{
        afterList.add(ele);
      }
    }

    for(i = 0 ; i<beforeList.size() ;i++ ){
      if(i == 0){
        if(beforeList.get(i).getName().equals("none")){
          continue;
        }
        helper.addTempData();
        parseElement(beforeList.get(i), helper);
        String temp = helper.getTempData();
        if(!StrUtil.isBlank(temp.trim())){
          helper.append("_");
          helper.append(temp);
        }
      }
      if(i == 1){
        if(beforeList.get(i).getName().equals("none")){
          continue;
        }
        helper.addTempData();
        parseElement(beforeList.get(i), helper);
        String temp = helper.getTempData();
        if(!StrUtil.isBlank(temp.trim())){
          helper.append("^");
          helper.append(temp);
        }
      }
    }
//    helper.append("\\textrm{");
//    helper.setInText(true);
    parseElement(mainElement, helper);
//    helper.setInText(false);
//    helper.append("}");

    for(i = 0 ; i<afterList.size() ;i++ ){
      if(i == 0){
        if(afterList.get(i).getName().equals("none")){
          continue;
        }
        helper.addTempData();
        parseElement(afterList.get(i), helper);
        String temp = helper.getTempData();
        if(!StrUtil.isBlank(temp.trim())){
          helper.append("_");
          helper.append(temp);
        }
      }
      if(i == 1){
        if(afterList.get(i).getName().equals("none")){
          continue;
        }
        helper.addTempData();
        parseElement(afterList.get(i), helper);
        String temp = helper.getTempData();
        if(!StrUtil.isBlank(temp.trim())){
          helper.append("^");
          helper.append(temp);
        }
      }
    }

  }

  public static void parseMover(Element element, FormMulaHelper helper) throws Exception {

    int i = 0;
    Iterator it = element.elementIterator();

    Element item0 = null;
    Element item1 = null;
    while (it.hasNext()) {
      Element tmp = (Element) it.next();
      if (i == 0) {
        item0 = tmp;
      } else if (i == 1) {
        item1 = tmp;
      }
      i++;
    }

    if (item1 != null) {

      helper.addTempData();
      parseElement(item1, helper);
      String tmpData = helper.getTempData();
      String t = tmpData.trim();
      if ("~".equals(t)) {
        helper.append("\\widetilde");
      } else if ("^".equals(t)) {
        helper.append("\\widehat");
      } else if ("←".equals(t)) {
        helper.append("\\overleftarrow");
      } else if ("→".equals(t)) {
        helper.append("\\overrightarrow");
      } else if ("-".equals(t)) {
        helper.append("\\overline");
      } else if ("⏞".equals(t)) {
        helper.append("\\overbrace");
      } else {
        helper.append("\\overset{"+tmpData+"}");
      }
    }
    if (item0 != null) {
      helper.append("{");
      parseElement(item0, helper);
      helper.append("}");
    }
  }

  public static void parseMunder(Element element, FormMulaHelper helper) throws Exception {

    int i = 0;
    Iterator it = element.elementIterator();

    Element item0 = null;
    Element item1 = null;
    while (it.hasNext()) {
      Element tmp = (Element) it.next();
      if (i == 0) {
        item0 = tmp;
      } else if (i == 1) {
        item1 = tmp;
      }
      i++;
    }

    if (item1 != null) {

      helper.addTempData();
      parseElement(item1, helper);
      String tmpData = helper.getTempData();

      if ("⏟".equals(tmpData)) {
        helper.append("\\underbrace");
      } else {
        helper.append("\\underset{"+tmpData+"}");
      }
    }
    if (item0 != null) {
      helper.append("{");
      parseElement(item0, helper);
      helper.append("}");
    }
  }

  public static void parseMunderover(Element element, FormMulaHelper helper) throws Exception {

    int i = 0;
    Iterator it = element.elementIterator();

    Element item0 = null;
    Element item1 = null;
    Element item2 = null;
    while (it.hasNext()) {
      Element tmp = (Element) it.next();
      if (i == 0) {
        item0 = tmp;
      } else if (i == 1) {
        item1 = tmp;
      } else if (i == 2) {
        item2 = tmp;
      }
      i++;
    }

    if (item1 != null) {
      helper.addTempData();
      parseElement(item1, helper);
      String tmpData = helper.getTempData();

      if ("⏟".equals(tmpData)) {
        helper.append("\\underbrace");
      } else {
        helper.append("\\underset{"+tmpData+"}");
      }
    }
    helper.append("{");

    if (item2 != null) {
      helper.addTempData();
      parseElement(item2, helper);
      String tmpData = helper.getTempData();

      if ("~".equals(tmpData)) {
        helper.append("\\widetilde");
      } else if ("^".equals(tmpData)) {
        helper.append("\\widehat");
      } else if ("←".equals(tmpData)) {
        helper.append("\\overleftarrow");
      } else if ("→".equals(tmpData)) {
        helper.append("\\overrightarrow");
      } else if ("-".equals(tmpData)) {
        helper.append("\\overline");
      } else if ("⏞".equals(tmpData)) {
        helper.append("\\overbrace");
      } else {
        helper.append("\\overset");
          helper.append("{");
          helper.append(tmpData);
          helper.append("}");
      }
    }
    helper.append("{");
    parseElement(item0, helper);
    helper.append("}");
    helper.append("}");
  }

  public static void parseMtable(Element element, FormMulaHelper helper) throws Exception {

    helper.append("\\begin{array}{lcl}");

    Iterator it = element.elementIterator();
    while (it.hasNext()) {
      Element item = (Element) it.next();
      parseElement(item, helper);
      if (it.hasNext()) {
        helper.append(" \\\\ ");
      }
    }

    helper.append("\\end{array}");
  }


  private static String key(String key) {
    if (StrUtil.isBlank(key)) {
      return "";
    }
    if (key.equals("{")) {
      key = "\\{";
    }
    if (key.equals("}")) {
      key = "\\}";
    }
    if (key.equals("%")) {
      key = "\\%";
    }
    if (key.equals("_")) {
      key = "\\_";
    }

    if (key.equals("~")) {
      key = "\\sim";
    }

    key = key.replaceAll("[\\s\\u00A0]+", " ");
    return key;
  }




}

@Data
class FormMulaHelper {

  private StringBuffer sb = new StringBuffer();

  private List<StringBuffer> tempDataList = new LinkedList<>();

  public void append(String content) {
    if(CollectionUtils.isEmpty(tempDataList)){
      sb.append(content);
    }else{
      tempDataList.get(tempDataList.size()-1).append(content);
    }
  }

  public void addTempData(){
    tempDataList.add(new StringBuffer());
  }

  public String getTempData(){
    String data = tempDataList.get(tempDataList.size()-1 ).toString();
    tempDataList.remove(tempDataList.size()-1);
    return data;
  }


  private boolean inText = false;

}