# 麻烦点下Star哦(>▽<)

## 如果需要参考请不要忘记修改一下变量名，这可不是什么困难的事情.....

## 由于判定平台问题此项目并不能通过,仅供参考提供思路.

### 已实现：
* 双击列表切歌；
* 音量控制(有缺陷)；
* 下一首/上一首；
* 开始/暂停；

### 未实现：
* 拖动进度条控制播放进度；
* 快进/快退；

_经过测试，Codecode的OnlineJudge似乎有些问题._

* 1.在Play.java中的run方法，原生所给的使用了sleep，在没有修改的情况下提交会显示线程存在锁死风险，因而改用wait避免此问题
**（已经说明了它自己的代码过不了它自己的测试）**

* 2.在修改为wait之后，另一个判定fail是出现在pom.xml中的系统环境设置，判定fail的理由是，存在系统版本与变量，会影响兼容性 

_官方说明_

>system dependencies are sought at a specific, specified path. This drastically reduces portability because if you deploy
 your artifact in an environment that's not configured just like yours is, your code won't work..
 
平台的建议是remove Systempath，因此remove之后再次提交却出现了code analysis无法通过的问题.

~~这个平台太屑了~~
