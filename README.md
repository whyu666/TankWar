# TankWar
CHD 2022 curriculum design - Integrated Practice of Basic Skills of Software Engineering

当前readme文档不够完善，后期再做调整，参考：https://www.jianshu.com/p/813b70d5b0de

> 开源许可协议：MIT License

编译运行：需选择SDK为Java1.8，设置项目语言级别为8，在IDEA的项目结构中设置src为源，设置images和sounds为资源，运行game.Main启动游戏

---

编写程序时注意：
- 新增的变量和函数，在此文档中说明（添加到程序扩展结构部分），修改README文档时，请使用规范的Markdown语言，具体参考：[Markdown官方教程](https://markdown.com.cn/basic-syntax/)
- 注意程序书写规范，包括括号位置、变量名函数名声明方式请保持和原程序一致
- 建议直接使用IDEA的推送功能，如果无法使用，也可使用Github网页、客户端、命令行完成推送
- 每次推送代码时，***请写清修改、增加内容***
- 有任何想法、问题，可以在Discussion中提出
- ......

---

## 程序基本结构：
### (1) stable：游戏中不动的元素
- Stable：抽象类，不动元素共有属性
- 砖块Brick：可以被坦克摧毁
- 草Grass：坦克可以隐藏在其中
- 家Home：家被摧毁，游戏结束
- 石头Stone：不能被摧毁
- 水Water：坦克无法通过，子弹可以飞过

### (2) ui：界面设计
- GameScene：抽象类，界面设计的共有属性
- GameHud：
- GameUI：游戏界面
- LeadersScene：排行榜界面
- OverScene：游戏失败界面
- SoundManager：播放对应的音乐
- StartScene：欢迎界面
- WinScene：游戏胜利界面

### (3) leaderboard：排行榜

### (4) sprite：可移动的元素
- Sprite：抽象类，可移动元素的共有属性
- Direction：上下左右四个方向
- EnemyTank：对手坦克
- Missile：子弹
- PlayerTank：玩家坦克
- Tank：抽象类，坦克的共有属性

### (5) map：地图
- GameMap：生成地图
- Map：地图属性
- MapData：地图数据（关卡）

### (6) game：游戏初始化

---

## 程序扩展结构：

### (1) 


### (2)


---

## 版本更新说明：
### ver0.1 最初始版本，该版本实现了包括欢迎页面，坦克大战基本功能（使用已经设计好的地图、家的设计、敌人的设计、关卡的设计），输赢界面，排行榜，作弊处理等
### ver0.2 
- 在排行榜、输赢界面增加了返回主界面按钮
- 在主界面增加作弊方法的说明
