## 红岩最终考核  慧记账

加入红岩这大半年来，我真切的学到了许多知识。回首寒假考核写的那个记账小助手，到期中考核的仿音乐播放器，到现在的慧记账，不可否认这半年来技术的进步之明显，但我也深刻地明白这还远远不足：“革命尚未成功，同志仍需努力”。如果有幸能通过红岩最终考核，成为真正的红岩人，我将尽我所能，努力钻研知识，为红岩作出贡献，而从不减缓我的脚步。

#### **后端为阿里云服务器+servlet+tomcat纯手写（手写后端好耗费时间hhh早知道找接口能省下不少时间）**

![image-20200721234018259](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20200721234018259.png)



### 开发时

1. 使用的第三方库：

```
pullrefreshlayout（更美观的下拉加载框）
MPAndroidChart（图表控件）
roundedimageview（性能更优化的可以设置圆角的图片框）
Glide
Gson
Android-PickerView（仿ios日期选择框）
qiniu-java-sdk（七牛云图片上传库）
其他均为手写自定义view
当时开发没考虑用太多第三方库，故感觉ui稍稍有些不尽人意
```

2. 使用了图片资源：鲨鱼记账中的类目图片
3. 使用了阿里云字体

### 使用

1.登录注册：账号和密码长度必须大于4

![Screenshot_20200721_233445_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_233445_com.example.moneycount.jpg)

2. 主界面![Screenshot_20200721_230736_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230736_com.example.moneycount.jpg)

3. 点击下方加号可以增加一笔记账![Screenshot_20200721_230747_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230747_com.example.moneycount.jpg)

4. 选择完成类目后会提示输入金额
5. 类目下方有一个“设置类目按钮”可以增加和删除类目![Screenshot_20200721_230750_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230750_com.example.moneycount.jpg)
6. 增加类目时可以选择图标（共有200余个图片），输入类目名称![Screenshot_20200721_230753_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230753_com.example.moneycount.jpg)

7. 个人主页![Screenshot_20200721_230649_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230649_com.example.moneycount.jpg)

8. 点击他人头像可以看他人的主页![Screenshot_20200721_230657_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230657_com.example.moneycount.jpg)

9. 可以点赞和评论，帖子下方可以看到点赞的人的名字，点击可以进入他的个人主页![Screenshot_20200721_230642_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230642_com.example.moneycount.jpg)
10. 单击记账记录可以看详细信息

![Screenshot_20200721_230741_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230741_com.example.moneycount.jpg)

11. ![Screenshot_20200721_230729_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_230729_com.example.moneycount.jpg)图表功能，上方选择年份和收支类型即可看到图表，无记录的月份会被隐藏![Screenshot_20200721_210829_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_210829_com.example.moneycount.jpg)
12. 单击个人界面的齿轮，可以设置个人信息，上传头像等![Screenshot_20200721_235115_com.example.moneycount](C:\Users\lenovo\Documents\Tencent Files\1409618590\FileRecv\MobileFile\Screenshot_20200721_235115_com.example.moneycount.jpg)

后续readme可能会更新（服务器请求之类的），请看下次commit哈哈，

尽力了，希望考核能过~~嘿嘿

### 服务器请求（700多行代码累死我了hai）

1. 评论支持多级评论，每个评论下面还可以继续评论（套娃，数据结构类似于链表）

2. 点赞、取消点赞
3. 个性签名
4. 头像
5. 昵称
6. 性别
7. 发帖数
8. 获赞数
9. 简单登录
10. 简单注册