package ui;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

	//多线程解决音效卡顿
	class PlaySound extends Thread{
		String file;
		public PlaySound(String filepath){file = filepath;}
		private void play(String filepath) {
			Media hit = new Media(Paths.get(filepath).toUri().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(hit);
			mediaPlayer.play();
		}
		@Override
		public void run(){	//线程要执行的任务写在 run() 方法中
			play(file);	//线程理应自动结束
		}
	}

	//数据统计,每局清空
	public int nMissile = 0;	//子弹发射数
	public int nKill = 0;		//杀敌数
	public int nSerial = 0;		//连杀数

	//全局的音效
	public static void playVictory()		{play("sounds/victory.mp3");}
	public static void playDefeat() 		{play("sounds/defeat.mp3");}

	//不要直接用，请用onXxx()
	public PlaySound playDominating = new PlaySound("sounds/dominating.mp3");
	public PlaySound playFirst = new PlaySound("sounds/first.mp3");
	public PlaySound playLegendary = new PlaySound("sounds/legendary.mp3");
	public PlaySound playMissile = new PlaySound("sounds/missile.mp3");
	public PlaySound playShutdown = new PlaySound("sounds/shutdown.mp3");
	public PlaySound playSlain = new PlaySound("sounds/slain.mp3");

	//onXxx:动作发生时调用
	//TODO: 改为监听器,解决播放中断
	public void onNew(){	//开局初始化
		nMissile = 0;
		nKill = 0;
		nSerial = 0;
	}
	public void onShoot(){	//发射子弹
		nMissile++;
		playMissile.run();
	}
	public void onKill(){	//杀敌
		if(nKill==0) playFirst.run();
		else if(nSerial<3) playShutdown.run();
		else if(nSerial<5) playDominating.run();
		else playLegendary.run();

		nKill++;
		nSerial++;
	}
	public void onSlain(){	//被杀
		playSlain.run();
		nSerial=0;
	}

	private static void play(String filepath) {
		Media hit = new Media(Paths.get(filepath).toUri().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

}