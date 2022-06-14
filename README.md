# TankWar
CHD 2022 curriculum design - Integrated Practice of Basic Skills of Software Engineering

编译运行：需选择SDK为Java1.8，且设置项目语言级别为8，运行game.Main

程序结构：  
(1) stable：游戏中不动的元素  
0. Stable：抽象类，不动元素共有属性
1. 砖块Brick：可以被坦克摧毁
2. 草Grass：坦克可以隐藏在其中
3. 家Home：家被摧毁，游戏结束
4. 石头Stone：不能被摧毁
5. 水Water：坦克无法通过，子弹可以飞过

(2) ui：界面设计  
0. GameScene：抽象类，界面设计的共有属性
1. GameHud：
2. GameUI：游戏界面
3. LeadersScene：排行榜界面
4. OverScene：游戏失败界面
5. SoundManager：播放对应的音乐
6. StartScene：欢迎界面
7. WinScene：游戏胜利界面

(3) leaderboard：排行榜

(4) sprite：可移动的元素
0. Sprite：抽象类，可移动元素的共有属性
1. Direction：上下左右四个方向
2. EnemyTank：对手坦克
3. Missile：子弹
4. PlayerTank：玩家坦克
5. Tank：抽象类，坦克的共有属性

(5) map：地图
1. GameMap：生成地图
2. Map：地图属性
3. MapData：地图数据（关卡）

(6) game：游戏初始化