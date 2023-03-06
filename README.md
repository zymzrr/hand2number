在手写识别方面，OCR一直都比较无能为力。
包括百度和阿里提供的sdk，识别率都比较低，有的识别甚至识别的非常离谱。

作者曾使用opencv+k最短路径,效果也不是很理想

后面使用自定义模板实现了手写识别，目前实现了纯数字的识别，后续正在开发英文数字的识别。中文识别暂未有计划

使用方法
获取笔迹，笔迹字段为数组，每个对象有3个值，x：笔迹的x值，y：笔迹的y值，action：动作（包含3个动作，down落笔,up抬笔，move移动）

提供2种方式
1.直接使用js，脱离服务器约束，比如小程序，和web，能更快的返回笔迹识别信息。
<script src="/js/number.js"></script>
引入js后，笔迹传入getNumber方法，返回数字

2.java服务端识别
可以通过restful识别。也可以拿到数据后服务端单独使用

restful接口地址/api/trace/mutinumber.json

例子：
服务启动后，访问
http://localhost:8080/muti-number.html
然后再书写区手写数字，系统自动识别成数字
识别结果
https://github.com/zymzrr/hand2number/blob/main/test.png


