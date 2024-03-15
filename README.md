# Android-Serialport
# 文档

<p >
    <a href="https://github.com/guapop/ComSDKDemo/blob/master/README.md">中文</a>
    | <a href="https://github.com/guapop/ComSDKDemo/blob/master/README_EN.md">English</a>
</p>

# 使用依赖
1. 把 comSDK.aar 文件复制到项目Module下的libs文件夹中：

    下载 [comSDK.aar](https://github.com/guapop/ComSDKDemo/raw/master/comDemo/libs/ComSDK.aar)

2. 在项目Module下的`build.gradle`文件中添加：
```
android{

    repositories {
        flatDir {
            dirs 'libs'
        }
    }

}

 dependencies {
    implementation(name: 'ComSDK', ext: 'aar')
 }
    
```
# 代码功能
## 1.列出串口列表
```
serialPortFinder.getAllDevicesPath();
```
## 2.串口属性设置
```
serialHelper.setPort(String sPort);      //设置串口
serialHelper.setBaudRate(int iBaud);     //设置波特率

```
[![](https://img.shields.io/badge/warning-%09%20admonition-yellow.svg)](https://github.com/guapop/ComSDKDemo)

串口属性设置需在执行`open()`函数之前才能设置生效
## 3.打开串口
```
serialHelper.open();
```
## 4.关闭串口
```
serialHelper.close();
```
## 5.发送
```
serialHelper.send(byte[] bOutArray); // 发送byte[]
serialHelper.sendHex(String sHex);  // 发送Hex
serialHelper.sendTxt(String sTxt);  // 发送ASCII
```
## 6.接收
```
@Override
protected void onDataReceived(final ComBean comBean) {
       Toast.makeText(getBaseContext(), new String(comBean.bRec, "UTF-8"), Toast.LENGTH_SHORT).show();
}
```
## 7.如何发送具体命令
```
//扫描头会休眠,发送具体命令前,需先发送唤醒命令
serialHelper.sendHex(Constants.ACTIVATION);
SystemClock.sleep(20);//间隔20ms
serialHelper.sendHex(Constants.SCAN);//发送具体命令
```
## 8.控制扫码灯
```
FuncUtil.open_Scan();//打开扫码灯
FuncUtil.close_Scan();//关闭扫码灯
FuncUtil.read_Scan_State();//读取扫码灯状态
```
## 9.控制二维码电源
```
FuncUtil.open_QrPower();//打开二维码电源
FuncUtil.close_QrPower();//关闭二维码电源
FuncUtil.read_QrPower_State();//读取二维码电源状态
```
## 10.自定义按键功能
```
//中间按键为19 scanCode=103
keyCode == KeyEvent.KEYCODE_DPAD_UP

//右按键为22 scanCode=106
keyCode == KeyEvent.KEYCODE_DPAD_RIGHT

//左按键为82 scanCode=139
keyCode == KeyEvent.KEYCODE_MENU

根据需要可自定义实现相关的功能

```



示例apk: [comDemo.apk](https://github.com/guapop/ComSDKDemo/raw/master/comDemo/release/comDemo-release.apk)
