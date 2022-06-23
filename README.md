# TankWar
CHD 2022 curriculum design - Integrated Practice of Basic Skills of Software Engineering

---

项目介绍：
TankWar是基于JavaFX开发的一款坦克大战小游戏
> 开源许可协议：MIT License

---

运行方法：
- 编译运行：在IDE中选择SDK为Java1.8，设置项目语言级别为8，在项目结构中设置src为源，设置images和sounds为资源，运行game.Main启动游戏  
- 直接运行：在配置好Java环境后，直接下载Release中的TankWar.jar运行

---

## 项目结构：
### (1) game：游戏核心部分
- Game：单人游戏模式核心
- Game2：双人游戏模式核心
- KeyMon：新构建的游戏输入处理类
- Main：游戏主程序，启动游戏

### (2) leader：排行榜
- Leader：存储leaders的属性
- LeaderBoard：实现排行榜功能（数据库的实现）

### (3) map：地图
- GameMap：生成单人游戏模式地图
- GameMap2：生成多人游戏模式地图
- Map：地图属性
- MapData：单人游戏模式地图数据（多关卡）
- MapData2：双人游戏模式地图数据（单关卡）

### (4) misc：杂项
- LengthLimitedTextField：限制文本框输入字符数

### (5) sprite：可移动的元素
- Sprite：抽象类，可移动元素的共有属性
- Direction：上下左右四个方向
- EnemyTank：对手坦克
- Missile：子弹
- PlayerTank：玩家坦克
- PlayerTank2：多人模式下另一个玩家坦克
- Tank：抽象类，坦克的共有属性

### (6) stable：游戏中不动的元素
- Stable：抽象类，不动元素共有属性
- 砖块Brick：可以被坦克摧毁
- 草Grass：坦克可以隐藏在其中
- 家Home：家被摧毁，游戏结束
- 石头Stone：不能被摧毁
- 水Water：坦克无法通过，子弹可以飞过

### (7) ui：界面设计
- GameScene：抽象类，界面设计的共有属性
- GameHud：单人游戏模式生命值、时间、关卡界面
- GameHud2：多人游戏模式生命值、时间、关卡界面
- GameUI：游戏主界面
- LeadersScene：排行榜界面
- OverScene：游戏失败界面
- OverScene2：多人模式游戏结束界面
- SoundManager：播放对应的音乐
- StartScene：欢迎界面
- WinScene：游戏胜利界面

---

## 版本更新说明：
### ver0.1 初始版本
- 欢迎页面
- 使用已经设计好的地图进行游戏
- 游戏中家、敌人和关卡的设计
- 输赢界面
- 排行榜
- 作弊处理
- 胜利、失败音效
### ver0.2 改进版本
- 在排行榜、输赢界面增加了返回主界面按钮
- 在主界面增加作弊方法的说明
- 修改了人机对战模块，使其追踪玩家
### ver0.2.1
- 新增排行榜名字输入长度限制
### ver1.0 全新升级版本
- 增加双人模式
- 修改欢迎界面，将游戏规则添加到另一个界面中
### ver1.0.1
- 解决了双人模式中控制问题
- 修复了双人模式中记分出现的问题
- 增加最终得分页面
### ver1.1 增加数据库功能
- 增加数据库功能，记录排行榜
### ver1.1.1
- 修正数据库
- 增加双人模式结束功能，双人模式按键优化、生命值显示修正
### ver1.2
- 重新构建游戏输入处理类
- 加入发射、得分、被毁音效
### ver2.0 Release版本
- 解决了大部分bug，生成release版本