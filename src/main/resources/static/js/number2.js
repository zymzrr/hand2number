
let strokes = [];
let singles = [];
let composes = [];

init();
function init() {
    strokes = [
        {"stroke":"3","asc":2,"lines":[{"degs":[{"max":360,"min":315},{"max":75,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":180,"min":90}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":360,"min":300},{"max":90,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.01}},{"degs":[{"max":210,"min":105}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.2}}]},
        {"stroke":"8-1","asc":3,"lines":[{"degs":[{"max":270,"min":95}],"arrows":[{"max":270,"min":95}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":90,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":270,"min":90}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":360,"min":270}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.2}}]},
        {"stroke":"8-2","asc":3,"lines":[{"degs":[{"max":360,"min":315},{"max":75,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":180,"min":90}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":360,"min":300},{"max":45,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":270,"min":180}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}}]},
        {"stroke":"8-3","asc":3,"lines":[{"degs":[{"max":"90","min":"45"}],"arrows":[{"max":"90","min":"45"}],"curYPercent":{"max":1,"min":"0.3"},"curXPercent":{"max":1,"min":"0"}},{"degs":[{"max":"225","min":"135"}],"arrows":[{"max":"225","min":"135"}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":"0"}},{"degs":[{"max":360,"min":"270"}],"arrows":[{"max":360,"min":270}],"curYPercent":{"max":1,"min":"0.3"},"curXPercent":{"max":1,"min":"0"}},{"degs":[{"max":"225","min":"135"}],"arrows":[{"max":"225","min":"135"}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":"0"}}]},
        {"stroke":"4","asc":4,"lines":[{"degs":[{"max":180,"min":0}],"arrows":[{"max":150,"min":45}],"curYPercent":{"max":1,"min":0.2},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":360,"min":315},{"max":45,"min":0}],"arrows":[{"max":360,"min":315},{"max":45,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.2}},{"degs":[{"max":270,"min":180}],"arrows":[{"max":360,"min":180}],"curYPercent":{"max":1,"min":0.02},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":120,"min":45}],"arrows":[{"max":135,"min":45}],"curYPercent":{"max":1,"min":0.4},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"2","asc":5,"lines":[{"degs":[{"max":360,"min":315},{"max":80,"min":0}],"arrows":[{"max":360,"min":270},{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":210,"min":90}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":360,"min":300},{"max":30,"min":0}],"arrows":[{"max":360,"min":315},{"max":30,"min":0}],"curYPercent":{"max":0.6,"min":0},"curXPercent":{"max":1,"min":0.2}}]},
        {"stroke":"6","asc":6,"lines":[{"degs":[{"max":135,"min":45}],"arrows":[{"max":180,"min":45}],"curYPercent":{"max":1,"min":0.6},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":360,"min":180}],"arrows":[{"max":360,"min":180}],"curYPercent":{"max":0.7,"min":0.05},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":225,"min":90}],"arrows":[{"max":225,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"9-1","asc":7,"lines":[{"degs":[{"max":225,"min":105}],"arrows":[{"max":270,"min":90}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":360,"min":315},{"max":45,"min":0}],"arrows":[{"max":360,"min":270},{"max":45,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":135,"min":75}],"arrows":[{"max":135,"min":75}],"curYPercent":{"max":1,"min":0.3},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"9-2","asc":7,"lines":[{"degs":[{"max":360,"min":270}],"arrows":[{"max":360,"min":270}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.3}},{"degs":[{"max":270,"min":135}],"arrows":[{"max":270,"min":135}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":135,"min":45}],"arrows":[{"max":135,"min":45}],"curYPercent":{"max":1,"min":0.5},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"7","asc":9,"lines":[{"degs":[{"max":360,"min":315},{"max":50,"min":0}],"arrows":[{"max":360,"min":270},{"max":50,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.2},"diffXPercent":{"max":1,"min":0},"diffYPercent":{"max":3,"min":0.1}},{"degs":[{"max":135,"min":75}],"arrows":[{"max":135,"min":60}],"curYPercent":{"max":1,"min":0.6},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"0-1","asc":10,"lines":[{"degs":[{"max":180,"min":45}],"arrows":[{"max":180,"min":45}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":315,"min":225}],"arrows":[{"max":315,"min":225}],"curYPercent":{"max":1,"min":0.3},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":260,"min":135}],"arrows":[{"max":260,"min":135}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"0-2","asc":10,"lines":[{"degs":[{"max":"225","min":"135"}],"arrows":[{"max":"225","min":"135"}],"curYPercent":{"max":1,"min":"0"},"curXPercent":{"max":1,"min":"0.1"}},{"degs":[{"max":"135","min":"45"}],"arrows":[{"max":"135","min":"45"}],"curYPercent":{"max":1,"min":0.3},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":"360","min":"270"}],"arrows":[{"max":"360","min":"270"}],"curYPercent":{"max":1,"min":"0.1"},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"5-2-1","asc":90,"lines":[{"degs":[{"max":135,"min":75}],"arrows":[{"max":135,"min":75}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":270,"min":120}],"arrows":[{"max":270,"min":120}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.03}}]},
        {"stroke":"1","asc":110,"lines":[{"degs":[{"max":135,"min":75}],"arrows":[{"max":120,"min":60}],"curYPercent":{"max":1,"min":0.8},"curXPercent":{"max":1,"min":0}}]},
        {"stroke":"4-2-1","asc":100,"lines":[{"degs":[{"max":180,"min":0}],"arrows":[{"max":150,"min":45}],"curYPercent":{"max":1,"min":0.2},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":360,"min":315},{"max":45,"min":0}],"arrows":[{"max":360,"min":315},{"max":45,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.2}}]},
        {"stroke":"5-2-2","asc":120,"lines":[{"degs":[{"max":360,"min":315},{"max":45,"min":0}],"arrows":[{"max":360,"min":315},{"max":45,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.5}}]},
        {"stroke":"X-2-1","asc":130,"lines":[{"degs":[{"max":90,"min":0}],"arrows":[{"max":90,"min":0}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0.1}}]},
        {"stroke":"X-2-2","asc":130,"lines":[{"degs":[{"max":180,"min":90}],"arrows":[{"max":180,"min":90}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0.1}}]}
    ];
    singles = {
        "1" : "1",
        "2" : "2",
        "3" : "3",
        "4" : "4",
        "6" : "6",
        "7" : "7",
        "8-1" : "8",
        "8-2" : "8",
        "8-3" : "8",
        "9-1" : "9",
        "9-2" : "9",
        "0-1" : "0",
        "0-2" : "0"
    };

    composes = {
        "1":[
            {"letter":"4","strokes":[{"stroke":"4-2-1", "role":"base"},{"stroke":"1","role":"end", "x":{"mode":"in",range:{"max":1,"min":0}},"y":{"mode":"contain",range:{"max":1.01,"min":0.99}}}]},
            {"letter":"X","strokes":[{"stroke":"5-2-1", "role":"base"},{"stroke":"1","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}}}]}
        ],
        "5-2-2":[
            {"letter":"5","strokes":[{"stroke":"5-2-1", "role":"base"},{"stroke":"5-2-2","role":"end", "x":{"mode":"touch",range:{"max":0.9,"min":0.1}},"y":{"mode":"in",range:{"max":0.6,"min":-1}}}]}
        ],
        "5-2-1":[
            {"letter":"5","strokes":[{"stroke":"5-2-2","role":"end", "x":{"mode":"touch",range:{"max":0.9,"min":0.1}},"y":{"mode":"in",range:{"max":0.6,"min":-1}}},{"stroke":"5-2-1", "role":"base"}]}
        ],
        "X-2-2": [
            {"letter":"X","strokes":[{"stroke":"X-2-1", "role":"base"},{"stroke":"X-2-2","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}}}]}
        ],
        "X-2-1": [
            {"letter":"X","strokes":[{"stroke":"X-2-2", "role":"base"},{"stroke":"X-2-1","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}}}]}
        ]
    };

}

function checkRaw(objList){
    let t1 = new Date().getTime();
    let letter = ocrRaw(objList);
    let map = {
        letter : letter,
        time : new Date().getTime() - t1
    };
    return map;
}


//识别单个vx
function ocrRaw(objList) {

    if (objList.length < 4) {
        return null;
    }
    let handList = preDoPoint(objList);


    handList.wordList  = [];
    handList.helperList  = [];

    return getNumber(handList);
}


function getNumber(handList) {

    for (let i = 0; i< handList.lineList.length;i++) {

        let userLine = handList.lineList[i];
        let helper = {};
        setXY(helper, userLine);
        handList.helperList[i] = helper;

        for (let stroke of strokes) {
            //
            if (fuckByTrace(stroke, userLine, helper)) {

                handList.strokeList[i].push(stroke.stroke);

                //先从组合判断
                let find = fuckByCompose(stroke.stroke,handList,i);
                if(!find){
                    //组合里找不到，然后从单个里判断
                    fuckBySingle(stroke.stroke,handList,i);
                }
            }

            // else{
            //     handList.strokeList[i] = null;
            // }
        }
    }
    return getWords(handList);
}

function getWords(handList){

    let sb = '';
    for (let s of handList.wordList) {
        if(s != null){
           sb = sb+ s
        }
    }
    return sb;
}

function fuckByCompose(strokeName,handList,i){

    let componse = composes[strokeName];
    console.log("查询尾笔"+strokeName+"的组合"+componse);
    console.log("handList",handList);
    if(componse == null || componse.length == 0){
        return false;
    }

    for (let compose of componse) {
        let size = compose.strokes.length;
        if(i < size-1){
            continue;
        }

        let base = null;
        //查找base的数据
        for (let j = 0;j< compose.strokes.length;j++) {
            let handIndex = i - size + 1 + j;
            let strokeComposeVo = compose.strokes[j];
            if("base" == (strokeComposeVo.role)){
                base = handList.helperList[handIndex];
                break;
            }
        }
        if(base == null){
            return false;
        }

        let allPass = true;
        for (let j = 0;j< compose.strokes.length;j++) {

            let strokeComposeVo = compose.strokes[j];
            let handIndex = i - size + 1 + j;

            let oriStrokes = handList.strokeList[handIndex];
            //先看笔画对不对
            if(oriStrokes.indexOf(strokeComposeVo.stroke) == -1){
                allPass = false;
                break;
            }

            if("base" == strokeComposeVo.role){
                continue;
            }

            //开始判断是否符合位置信息
            //那笔的位置信息是否符合
            let current = handList.helperList[handIndex];
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
            let w = handList.wordList[i];
            if(w == null){
                for (let j = 0;j< compose.strokes.length;j++) {
                    handList.wordList[i - size + 1 + j] = null;
                }
                handList.wordList[i] = compose.letter;
            }
            return true;
        }
    }

    return false;
}

function fuckComposePostion(base,current,stand){

    let xMin = base.minX + base.xDistance * stand.x.range.min;
    let xMax = base.minX + base.xDistance * stand.x.range.max;
    let yMin = base.minY + base.yDistance * stand.y.range.min;
    let yMax = base.minY + base.yDistance * stand.y.range.max;

    if("in" == (stand.x.mode)){

        if(current.minX >= xMin && current.maxX <= xMax ){

        }else{
            return false;
        }
    }
    else if("contain" == (stand.x.mode)){

        if(current.minX <= xMin && current.maxX >= xMax ){

        }else{
            return false;
        }
    }else if("touch" == (stand.x.mode)){

        if(current.maxX < xMin || current.minX > xMax ){
            return false;
        }
    }

    if("in" == (stand.y.mode)){

        if(current.minY >= yMin && current.maxY <= yMax ){

        }else{
            return false;
        }
    }
    else if("contain" == (stand.y.mode)){

        if(current.minY <= yMin && current.maxY >= yMax ){

        }else{
            return false;
        }
    }
    else if("touch" == (stand.y.mode)){

        if(current.maxY < yMin || current.minY > yMax ){
            return false;
        }
    }

    return true;
}


function fuckBySingle(strokeName,handList,i){
    let letter = singles[strokeName];
    if(letter != null){
        let w = handList.wordList[i];
        if(w == null){
            console.log("赋值["+i+"]",letter)
            handList.wordList[i] = letter;
            console.log(handList)
        }
    }
}



function fuckByTrace(stroke, userLines, helper) {

    console.log("正在识别笔画stroke:" + stroke.stroke);
    let i = 0;
    let j = 0;
    for (let condLine of stroke.lines) {
        console.log("正在识别line:" + j);
        for (; i < userLines.length; i++) {
            if (i == 0) {
                continue;
            }
            //2个点间的角度
            let prePoint = userLines[i - 1];
            let curPoint = userLines[i];
            let inStatus = isStart(condLine, prePoint, curPoint);

            //4种状态，之前在不在，现在在不在
            //之前在
            if (inStatus) {
                if (!condLine.inState) {
                    condLine.inState = (true);
                    condLine.firstPoint = (prePoint);
                }
                if (i == userLines.length - 1) {
                    condLine.lastPoint = (curPoint);
                    if (fuckByLine(condLine, helper)) {
                        console.log("正在识别line:" + j + "success");
                        condLine.pass = (true);
                        break;
                    } else {
                        condLine.inState = (false);
                    }
                }
            } else {
                if (condLine.inState) {
                    //判断这大段是否符合condLine
                    condLine.lastPoint = (prePoint);
                    if (fuckByLine(condLine, helper)) {
                        console.log("正在识别line:" + j + "success");
                        condLine.pass = (true);
                        condLine.firstPoint = (null);
                        condLine.lastPoint = (null);
                        condLine.inState = (false);
                        break;
                    } else {
                        condLine.inState = (false);
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

function isAllPass(stroke) {
    let r = true;
    for (let line of stroke.lines) {
        r = r && line.pass;
        line.pass = (false);
        line.inState = (false);
        line.firstPoint = (null);
        line.lastPoint = (null);
    }
    return r;
}


function fuckByLine(condLine,helper){

    let deg = getDeg(condLine.firstPoint,condLine.lastPoint);
    //先判断是否符合首尾角度
    if(!inRanges(deg,condLine.degs)){
        console.log("deg...cur:"+deg+";expect:"+ JSON.stringify(condLine.degs));
        return false;
    }
    if(!inRangeByDistance(condLine,helper)){
        return false;
    }

    return true;
}


function isStart(condLine, prePoint,curPoint) {

    let deg = getDeg(prePoint,curPoint);
    return inRanges(deg,condLine.arrows);
}

function inRange(deg,range){
    if(deg >= range.min && deg <= range.max){
        return true;
    }
    return false;
}

function inRanges(deg,ranges){
    for (let range of ranges) {
        if(deg >= range.min && deg <= range.max){
            return true;
        }
    }
    return false;
}



function inRangeByDistance(line,helper){

    let x = Math.abs(line.lastPoint.x - line.firstPoint.x);
    let y = Math.abs(line.lastPoint.y - line.firstPoint.y);
    if(!inRange(x / helper.xDistance , line.curXPercent)){
        console.log("比例:cur x:"+x / helper.xDistance+":"+ JSON.stringify(line.curXPercent));
        return false;
    }
    if(!inRange(y / helper.yDistance , line.curYPercent)){
        console.log("比例:cur y:"+y / helper.yDistance+":"+ JSON.stringify(line.curYPercent));
        return false;
    }

    if(line.diffYPercent != null){
        if(!inRange(x / helper.yDistance , line.diffYPercent)){
            console.log("异轴比例:cur x:"+x / helper.yDistance+":"+ JSON.stringify(line.diffYPercent));
            return false;
        }
    }
    if(line.diffXPercent != null){
        if(!inRange(y / helper.xDistance , line.diffXPercent)){
            console.log("异轴比例2:cur y:"+y / helper.xDistance+":"+ JSON.stringify(line.diffXPercent));
            return false;
        }
    }
    return true;
}

//
//预处理点信息
function preDoPoint(objList) {
    let handList = { lineList : [] , strokeList :[] };
   var lineList = [];
    for (let point of objList) {
        if ("down" == point.action) {
            lineList = [];
        }
        lineList.push(point);
        if ("up"  ==  point.action) {
            handList.lineList.push(lineList);
        }
    }


    //预处理,去除一个地方多点的情况,排除干扰
    for (let j = 0; j < handList.lineList.length; j++) {
        let points = handList.lineList[j];
        let newPoints = [];
        let stand = null;
        for (let i = 0; i < points.length; i++) {
            if (i == 0) {
                stand = points[i];
                newPoints.push(points[i]);
                continue;
            }
            let cur = points[i];
            if (Math.abs(cur.x - stand.x) >= 5 || Math.abs(cur.y - stand.y) >= 5) {
                newPoints.push(points[i]);
                stand = points[i];
            }
        }
        //过滤单个点的笔画,可能是误点
        if (newPoints.length > 1) {
            handList.lineList[j] = newPoints;
        } else {
            handList.lineList = handList.lineList.splice(j,1);
        }
    }

    for (let index in handList.lineList) {
        handList.strokeList.push([]);
    }
    return handList;
}

function getDeg(fromPoint,toPoint) {
    let deg = (Math.atan2(toPoint.y - fromPoint.y, toPoint.x - fromPoint.x)) * 180/Math.PI;
    if (deg < 0) {
        deg += 360;
    }
    return deg;
}

function setXY(helper,pointList){

    let minX = 0;
    let maxX = 0;
    let minY = 0;
    let maxY = 0;

    let i = 0;
    for (let point of pointList) {
        if (i++ == 0) {
            minY = point.y;
            maxY = point.y;
            minX = point.x;
            maxX = point.x;
            continue;
        }
        if (point.y > maxY) {
            maxY = point.y;
        }
        if (point.y < minY) {
            minY = point.y;
        }
        if (point.x > maxX) {
            maxX = point.x;
        }
        if (point.x < minX) {
            minX = point.x;
        }
    }

    let xDistance = maxX - minX;
    let yDistance = maxY - minY;

    helper.minX = minX;
    helper.maxX = maxX;
    helper.minY = minY;
    helper.maxY = maxY;

    helper.xDistance = (xDistance==0?1:xDistance);
    helper.yDistance = (yDistance==0?1:yDistance);
}








