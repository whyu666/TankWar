package ui;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundManager {

	static class Sfx{
		MediaPlayer mediaPlayer;
		public Sfx(String filepath){
			Media hit = new Media(Paths.get(filepath).toUri().toString());
			mediaPlayer = new MediaPlayer(hit);
		}
		public void play(){
			mediaPlayer.stop();
			mediaPlayer.play();
		}
	}

	//数据统计,每局清空
	public int nMissile = 0;	//子弹发射数
	public int nKill = 0;		//杀敌数
	public int nSerial = 0;		//连杀数

	//音效单例集
	public static Sfx sVictory = new Sfx("sounds/victory.mp3");
	public static Sfx sDefeat = new Sfx("sounds/defeat.mp3");
	public static Sfx sDominating = new Sfx("sounds/dominating.mp3");
	public static Sfx sFirst = new Sfx("sounds/first.mp3");
	public static Sfx sLegendary = new Sfx("sounds/legendary.mp3");
	public static Sfx sMissile = new Sfx("sounds/missile.mp3");
	public static Sfx sShutdown = new Sfx("sounds/shutdown.mp3");
	public static Sfx sSlain = new Sfx("sounds/slain.mp3");

	//全局音效
	public void playVictory()		{sVictory.play();}
	public void playDefeat() 		{sDefeat.play();}

	//自动音效 onXxx:动作发生时调用
	//TODO: 改为监听器,解决播放中断
	public void onNew(){	//开局初始化
		nMissile = 0;
		nKill = 0;
		nSerial = 0;
	}
	public void onShoot(){	//发射子弹
		nMissile++;
		if (sMissile.mediaPlayer.getStatus()== MediaPlayer.Status.PLAYING){
			sMissile.mediaPlayer.stop();
			sMissile.mediaPlayer.setStartTime(Duration.millis(60));
			sMissile.mediaPlayer.play();
		}
		else{
			sMissile.play();
		}
	}
	public void onKill(){	//杀敌
		if(nKill==0) sFirst.play();
		else if(nSerial<3) sShutdown.play();
		else if(nSerial<5) sDominating.play();
		else sLegendary.play();

		nKill++;
		nSerial++;
	}
	public void onSlain(){	//被杀
		sSlain.play();
		nSerial=0;
	}

	private static void play(String filepath) {
		Media hit = new Media(Paths.get(filepath).toUri().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

}