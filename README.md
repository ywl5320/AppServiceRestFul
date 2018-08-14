# AppServiceRestFul
## [我的视频课程（基础）：《（NDK）FFmpeg打造Android万能音频播放器》](https://edu.csdn.net/course/detail/6842)
## [我的视频课程（进阶）：《（NDK）FFmpeg打造Android视频播放器》](https://edu.csdn.net/course/detail/8036)
## [我的视频课程（编码直播推流）：《Android视频编码和直播推流》](https://edu.csdn.net/course/detail/8942)

### 博客地址：
#### [移动大脑-SpringMVc搭建RestFul后台服务（一）-环境搭建](http://blog.csdn.net/ywl5320/article/details/78152741)
#### [移动大脑-SpringMVc搭建RestFul后台服务（二）-配置mysql数据库](http://blog.csdn.net/ywl5320/article/details/78239008)
#### [移动大脑-SpringMVc搭建RestFul后台服务（三）-RestFul接口编写（模拟用户注册登录）](http://blog.csdn.net/ywl5320/article/details/78240855)
#### [移动大脑-SpringMVc搭建RestFul后台服务（四）-添加Token过滤器](http://blog.csdn.net/ywl5320/article/details/78250000)
#### [移动大脑-SpringMVc搭建RestFul后台服务（五）-支付宝支付](http://blog.csdn.net/ywl5320/article/details/78284477)
#### [移动大脑-SpringMVc搭建RestFul后台服务（六）-微信支付（Android）](http://blog.csdn.net/ywl5320/article/details/78294494)
#### [移动大脑-SpringMVc搭建RestFul后台服务（七）-增量更新](http://blog.csdn.net/ywl5320/article/details/78426756)

### 实例图：
![image](https://github.com/wanliyang1990/AppServiceRestFul/blob/master/imgs/update1.gif)
![image](https://github.com/wanliyang1990/AppServiceRestFul/blob/master/imgs/update2.gif)<br/>
![image](https://github.com/wanliyang1990/AppServiceRestFul/blob/master/imgs/2.png)
![image](https://github.com/wanliyang1990/AppServiceRestFul/blob/master/imgs/3.png)<br/>


## 使用说明：
### 服务端
#### 使用支付宝或微信支付功能时，需要在PayService.java中把相关的APPID等信息替换为自己的。然后微信支付包名还需要改成自己申请的，推荐一种快速修改微信包名的方式：在gradle中把applicationId改成自己的，然后创建相应的包名并添加wxapi文件夹用自己的keystore打包就可以了。
#### 在文件beans.xml中修改mysql数据库为自己的数据库，自己的mysql登录用户名和密码
### 客户端
#### 修改HttpMethod中的URL为自己服务器的URL和下载地址。

Create By: ywl5320
