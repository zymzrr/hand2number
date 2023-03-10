
let strokes = [];
let singles = [];
let composes = [];

init();
function init() {
    strokes = [
        {"stroke":"X-1","asc":1,"lines":[{"degs":[{"max":90,"min":0}],"arrows":[{"max":90,"min":0}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0.1}},{"degs":[{"max":360,"min":225}],"arrows":[{"max":360,"min":225}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":180,"min":90}],"arrows":[{"max":180,"min":90}],"curYPercent":{"max":1,"min":0.2},"curXPercent":{"max":1,"min":0.3}}]},
        {"stroke":"X-2","asc":2,"lines":[{"degs":[{"max":180,"min":90}],"arrows":[{"max":180,"min":90}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":315,"min":225}],"arrows":[{"max":315,"min":225}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":90,"min":0}],"arrows":[{"max":90,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}}]},
        {"stroke":"V","asc":3,"lines":[{"degs":[{"max":110,"min":0}],"arrows":[{"max":110,"min":0}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0}},{"degs":[{"max":360,"min":270}],"arrows":[{"max":360,"min":270}],"curYPercent":{"max":1,"min":0},"curXPercent":{"max":1,"min":0.1}}]},
        {"stroke":"X-2-1","asc":4,"lines":[{"degs":[{"max":90,"min":0}],"arrows":[{"max":90,"min":0}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0.1}}]},
        {"stroke":"X-2-2","asc":5,"lines":[{"degs":[{"max":180,"min":90}],"arrows":[{"max":180,"min":90}],"curYPercent":{"max":1,"min":0.1},"curXPercent":{"max":1,"min":0.1}}]}
    ];
    singles = {
        "X-1" : "X",
        "X-2" : "X",
        "V" : "V"
    };

    composes = {
        "X-2-2" : [{"letter":"X","strokes":[{"stroke":"X-2-1", "role":"base"},{"stroke":"X-2-2","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}}}]}],
        "X-2-1" : [
            {"letter":"X","strokes":[{"stroke":"X-2-2", "role":"base"},{"stroke":"X-2-1","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}},}]},
            {"letter":"X","strokes":[{"stroke":"V", "role":"base"},{"stroke":"X-2-1","role":"end", "x":{"mode":"touch",range:{"max":0.75,"min":0.25}},"y":{"mode":"touch",range:{"max":0.75,"min":0.25}}}]}
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


//????????????vx
function ocrRaw(objList) {

    if (objList.length < 4) {
        return null;
    }
    let handList = preDoPoint(objList);

    // if(handList.lineList.length > 3){
    //     return "";
    // }
    handList.strokeList  = [];
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

                handList.strokeList[i] = stroke.stroke;
                //??????????????????
                let find = fuckByCompose(stroke.stroke,handList,i);
                if(!find){
                    //?????????????????????????????????????????????
                    fuckBySingle(stroke.stroke,handList,i);
                }
                break;
            }else{
                handList.strokeList[i] = null;
            }
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
    if(componse == null || componse.length == 0){
        return false;
    }

    for (let compose of componse) {
        let size = compose.strokes.length;
        if(i < size-1){
            continue;
        }

        let base = null;
        //??????base?????????
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
            let oriStroke = handList.strokeList[handIndex];
            //?????????????????????
            if(!strokeComposeVo.stroke == oriStroke){
                allPass = false;
                break;
            }

            if("base" == strokeComposeVo.role){
                continue;
            }

            //????????????????????????????????????
            //?????????????????????????????????
            let current = handList.helperList[handIndex];
            //??????

            if(fuckComposePostion(base,current,strokeComposeVo)){
                continue;
            }else{
                allPass = false;
                break;
            }
        }

        //??????????????????????????????????????????????????????
        if(allPass){
            for (let j = 0;j< compose.strokes.length;j++) {
                handList.wordList[i - size + 1 + j] = null;
            }
            handList.wordList[i] = compose.letter;
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
        handList.wordList[i] = letter;
    }
}



function fuckByTrace(stroke, userLines, helper) {

    console.log("??????????????????stroke:" + stroke.stroke);
    let i = 0;
    let j = 0;
    for (let condLine of stroke.lines) {
        console.log("????????????line:" + j);
        for (; i < userLines.length; i++) {
            if (i == 0) {
                continue;
            }
            //2??????????????????
            let prePoint = userLines[i - 1];
            let curPoint = userLines[i];
            let inStatus = isStart(condLine, prePoint, curPoint);

            //4?????????????????????????????????????????????
            //?????????
            if (inStatus) {
                if (!condLine.inState) {
                    condLine.inState = (true);
                    condLine.firstPoint = (prePoint);
                }
                if (i == userLines.length - 1) {
                    condLine.lastPoint = (curPoint);
                    if (fuckByLine(condLine, helper)) {
                        console.log("????????????line:" + j + "success");
                        condLine.pass = (true);
                        break;
                    } else {
                        condLine.inState = (false);
                    }
                }
            } else {
                if (condLine.inState) {
                    //???????????????????????????condLine
                    condLine.lastPoint = (prePoint);
                    if (fuckByLine(condLine, helper)) {
                        console.log("????????????line:" + j + "success");
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
    //?????????????????????????????????
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
        console.log("??????:cur x:"+x / helper.xDistance+":"+ JSON.stringify(line.curXPercent));
        return false;
    }
    if(!inRange(y / helper.yDistance , line.curYPercent)){
        console.log("??????:cur y:"+y / helper.yDistance+":"+ JSON.stringify(line.curYPercent));
        return false;
    }

    if(line.diffYPercent != null){
        if(!inRange(x / helper.yDistance , line.diffYPercent)){
            console.log("????????????:cur x:"+x / helper.yDistance+":"+ JSON.stringify(line.diffYPercent));
            return false;
        }
    }
    if(line.diffXPercent != null){
        if(!inRange(y / helper.xDistance , line.diffXPercent)){
            console.log("????????????2:cur y:"+y / helper.xDistance+":"+ JSON.stringify(line.diffXPercent));
            return false;
        }
    }
    return true;
}

//
//??????????????????
function preDoPoint(objList) {
    let handList = { lineList : []};
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


    //?????????,?????????????????????????????????,????????????
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
        //????????????????????????,???????????????
        if (newPoints.length > 1) {
            handList.lineList[j] = newPoints;
        } else {
            handList.lineList = handList.lineList.splice(j,1);
        }
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








