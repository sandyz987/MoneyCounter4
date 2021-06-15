## 记账鸭 —— 一款轻量级简约的记账app

<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/1.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/2.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/3.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/4.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/5.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/6.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/7.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/8.jpg" width="200" height="400"/>
<img src="https://github.com/sandyz987/MoneyCounter4/blob/master/pic/9.jpg" width="200" height="400"/>


### 项目

1. 语言大部分使用kotlin
2. 使用的第三方库：

```
pullrefreshlayout（更美观的下拉加载框）
MPAndroidChart（图表控件）
Glide
Gson
Retrofit
Rxjava
CalculatorJNI（原创 计算字符串表达式值）
Android-PickerView（仿ios日期选择框）
```

3. 使用了图片资源：鲨鱼记账中的类目图片

4. 使用了阿里云字体



### 功能与使用

1. 登录注册：账号和密码长度必须大于4
2. 主界面
3. 点击右下方加号可以增加一笔记账
4. 选择完成类目后会提示输入金额
5. 类目下方有一个“设置类目按钮”可以增加和删除类目
6. 增加类目时可以选择图标（共有200余个图片），输入类目名称
7. 首页内容自定义（长按拖动控件）
8. 筛选自己的收支记录
9. 添加自己的账本
10. 个人主页
11. 点击他人头像可以看他人的主页
12. 可以点赞和评论，帖子下方可以看到点赞的人的名字，点击可以进入他的个人主页
13. 单击记账记录可以看详细信息
14. 如果是自己发的帖子可以删除



### 服务器请求

1. 服务器API接口说明：[Counter4Sql server (getpostman.com)](https://documenter.getpostman.com/view/10049826/TzY6AujB)
2. 评论支持多级评论，每个评论下面还可以继续评论（套娃，数据结构类似链表）
3. 点赞、取消点赞
4. 个性签名
5. 头像
6. 昵称
7. 性别
8. 发帖数
9. 获赞数
10. 简单登录
11. 简单注册
12. 发帖
13. 删除自己帖子



### 使用

1. 登录注册：账号和密码长度必须大于4
2. 主界面
3. 点击下方加号可以增加一笔记账
4. 选择完成类目后会提示输入金额
5. 类目下方有一个“设置类目按钮”可以增加和删除类目
6. 增加类目时可以选择图标（共有200余个图片），输入类目名称
7. 个人主页
8. 点击他人头像可以看他人的主页
9. 可以点赞和评论，帖子下方可以看到点赞的人的名字，点击可以进入他的个人主页
10. 单击记账记录可以看详细信息
11. 图表功能，上方选择年份和收支类型即可看到图表，无记录的月份会被隐击个人界面的齿轮，可以设置个人信息，上传头像等
12. 如果是自己发的帖子可以删除
