# Android-Serialport
# Document

<p >
     <a href="https://github.com/guapop/ComSDKDemo/blob/master/README.md">中文</a>
    | <a href="https://github.com/guapop/ComSDKDemo/blob/master/README_EN.md">English</a>
</p>

# Usage
1. Copy the comSDK.aar file to the libs folder under the project's Module

    Dowload [comSDK.aar](https://github.com/guapop/ComSDKDemo/raw/master/comDemo/libs/comSDK.aar)

2. To add a dependency to your project, specify a dependency configuration such as implementation in the dependencies block of your module's build.gradle file.
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
# Function
## 1.List the serial port
```
serialPortFinder.getAllDevicesPath();
```
## 2.Serial port property settings
```
serialHelper.setPort(String sPort);      //set serial port
serialHelper.setBaudRate(int iBaud);     //set baud rate

```
[![](https://img.shields.io/badge/warning-%09%20admonition-yellow.svg)](https://github.com/guapop/ComSDKDemo)

Serial port property settings must be set before the function 'open()' is executed.
## 3. Open the serial port
```
serialHelper.open();
```
## 4.Close the serial port
```
serialHelper.close();
```
## 5.Send
```
serialHelper.send(byte[] bOutArray); // send byte[]
serialHelper.sendHex(String sHex);  // send Hex
serialHelper.sendTxt(String sTxt);  // send ASCII
```
## 6.Receiving
```
@Override
protected void onDataReceived(final ComBean comBean) {
       Toast.makeText(getBaseContext(), new String(comBean.bRec, "UTF-8"), Toast.LENGTH_SHORT).show();
}
```
## 7.How to send specific commands
```
//The scan head will sleep, and you need to send a wake-up command before sending a specific command
serialHelper.sendHex(Constants.ACTIVATION);
SystemClock.sleep(20);//20ms interval
serialHelper.sendHex(Constants.SCAN);//Send specific commands
```
## 8.Control the barcode scanning light
```
FuncUtil.open_Scan();//open the barcode scanning light
FuncUtil.close_Scan();//close the barcode scanning light
FuncUtil.read_Scan_State();//Read the status of the barcode scanning light
```
## 9.Control the QR code power
```
FuncUtil.open_QrPower();//open the QR code power
FuncUtil.close_QrPower();//close the QR code power
FuncUtil.read_QrPower_State();//Read the power status of the QR code
```
## 10.Customize key functions
```
//The middle Key is 19 scanCode=103
keyCode == KeyEvent.KEYCODE_DPAD_UP

//The right Key is 22 scanCode=106
keyCode == KeyEvent.KEYCODE_DPAD_RIGHT

//The left Key is 82 scanCode=139
keyCode == KeyEvent.KEYCODE_MENU

You can customize and implement related functions as needed
```

Demo apk: [comDemo.apk](https://github.com/guapop/ComSDKDemo/raw/master/comDemo/release/comDemo-release.apk)

