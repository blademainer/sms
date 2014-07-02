#SMS
使用smslib写的发短信项目, [smslib](https://github.com/smslib/smslib)
#使用方法
---
* 将javax.comm.properties拷贝到%jre_home%\lib\目录下
* 将win32com.dll拷贝到%jre_home%\bin\目录下
* 如果使用tomcat作为web服务器，则将Log4j.xml文件和log4j-1.2.17.jar拷入到%tomcat_home%/lib下
* 修改`device.properties`，这个是配置设备信息的配置文件；`GateWays=device1,device2`表示有两个设备，以下是配置device1的设备信息：
device1.comPort=COM9
device1.baudRate=9600
device1.manufacturer=SIEMENS
device1.model=MC39i