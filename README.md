# 简介
这是一个WebUI自动化测试框架，基于java + maven + selenium + poi + extentreports开发，使用关键字驱动，将元素定位及操作写进Excel，实现脚本与代码分离，支持本地与远程(多线程)启动，自动生成html报告。

[Chrome驱动下载地址](http://chromedriver.storage.googleapis.com/index.html)

[远程启动客户端jar包](http://selenium-release.storage.googleapis.com/index.html)

[selenium wiki](https://github.com/SeleniumHQ/selenium/wiki)

[查看API](http://www.webdriver.org/doc/patatiumwebui/api/)

# 目录结构
- cfg/config.properties：启动配置项；
- cfg/RemoteServerInfo：远程启动配置项；
- script：启动脚本；
- logs：运行日志；
- pic：截图；
- lib：浏览器驱动；
- report：测试报告；
- upload：上传的文件；

# 脚本格式
- 参照script/baidu.xls；
- 支持xls和xlsx，支持多sheet；
- B1：填写浏览器类型，1:火狐，2:IE，3:Chrome；（已删除火狐IE功能）;
- B2：填写地址，以http(s)开头；
- 第五行开始填写脚本内容：

列|说明
-|-|
A列 | 步骤序号，不填或填写end停止脚本；
B列 | 元素定位方法；
C列 | 元素定位标识；
D列 | 元素操作；
E列 | 元素值；
F列 | 截图(y)；
G列 | 步骤说明；

# 详细说明
## 元素定位方法：
- id：根据元素id查找；
- xpath：根据xpath查找；
- linkText：根据元素文本内容精确匹配查找，例如文本超链接；
- className：根据元素的class查找，复合类则只取其中一个；
- name：根据元素的name查找；

## 元素定位标识
- 根据元素获取方法，填写id/xpath/linkText/className/name；

## 元素操作

操作方法|说明
-|-|
get | 打开url地址，元素值填url；
frame | 定位到iframe框架，填写defaultContent定位到主窗口，其他通过name或xpath定位；
newblank|切换到最新窗口；
click | 鼠标左键单击；
doubleClick | 鼠标左键双击；
contextClick | 鼠标右键单击；
mouseover | 鼠标移动到元素上；
clear | 清除元素值；
input | 设置元素值，填写{var}，读取 sheet2 的对应的列，当读到end时，跳到goto的下一步；
equals | 用于比较元素的内容，C列填写要比较的元素，E列填写要比较的内容；
select | 下拉列表单选，E列填写所选值；
sleep | 休眠，E列填写时间，单位毫秒；
uploadBy | 文件上传，E列填写文件名，文件放在upload文件夹；
uploadFile | 文件上传，文件路径写在配置项uploadFile，且必须为绝对路径，脚本E列可不填；
upload | 文件上传，E列填写文件绝对路径；

## 键盘操作
操作方法|说明
-|-|
enter | 按下Enter键；
delete | 按下delete键；
esc | 按下esc键；

## 拖拽操作
操作方法|说明
-|-|
dragDown | 纵向拖拽N个像素单位，E列填值，正数：向下，负数：向上；
dragHor | 横向拖拽N个像素单位，E列填值，正数：向右，负数：向左；
dragBy | 自由拖动，E列填值 “x,y” ，a为横向，b为纵向，英文逗号分隔，以要拖动的元素的左上角为基准(0,0)，x向右填正数，向左填负数，y向下填正数，向上填负数；
drag | 拖动至目标元素，E列填目标元素的xpath；
moveBy | 以鼠标当前位置或者 (0,0) 为中心开始移动到 (x,y) 坐标轴，E列填值 “x,y”，x向右填正数，向左填负数，y向下填正数，向上填负数，如果这两个值大于当前屏幕的大小，鼠标只能移到屏幕最边界的位置同时抛出异常MoveTargetOutOfBoundsExecption；

## 其他操作
操作方法|说明
-|-|
alertAccept | 浏览器弹窗，点击确定；
alertDismiss | 浏览器弹窗，点击取消；
scrollToTop | 移动滚动条直到该元素与当前窗口的“顶部”对齐；
scrollToBottom | 移动滚动条直到该元素与当前窗口的“底部”对齐；
executeJs | 执行不带参数的 js 代码，E列填 js；

# 远程启动
- 远程启动只支持Chrome；
1. 客户端运行jar包：
- Windows：
`java -Dwebdriver.chrome.driver="D:\chromedriver.exe" -jar D:\selenium-server-standalone-3.141.59.jar`
- Mac：
`java -Dwebdriver.chrome.driver="/work/chromedriver" -jar /work/selenium-server-standalone-3.141.59.jar`
- 默认端口4444，-port xxxx 修改端口；
- 启动后，http://localhost:4444/wd/hub，点击Create Session，创建浏览器会话，有弹出浏览器窗口，即启动成功；
2. 修改配置项useRemote=true；
3. 配置RemoteServerInfo.xml，远程地址填写http://ip:port/wd/hub；
4. 服务端启动程序即可；