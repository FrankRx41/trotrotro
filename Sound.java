import java.io.*;
import javax.sound.sampled.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JSlider;

/* Copy: http://www.codejava.net/coding/java-audio-player-sample-application-in-swing */

class Sound{
	class AudioPlayer implements LineListener {
		private boolean playCompleted;
		private boolean isStopped;
		private boolean isPaused;

		private Clip audioClip;
		
		private void controlByLinearScalar(FloatControl control, double linearScalar) {
			control.setValue((float)Math.log10(linearScalar) * 20);
		}
		public void load(String audioFilePath)throws UnsupportedAudioFileException, IOException,LineUnavailableException {
			File audioFile = new File(audioFilePath);

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.addLineListener(this);
			audioClip.open(audioStream);
			FloatControl volume = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(2);
		}

		void play() throws IOException {
			audioClip.start();
			playCompleted = false;

			while (!playCompleted) {
				try {
					Thread.sleep(100);
				}catch (InterruptedException ex) {
					ex.printStackTrace();
					if (isStopped) {
						audioClip.stop();
						break;
					}
					if (isPaused) {
						audioClip.stop();
					} else {
						//System.out.println("!!!!");
						audioClip.start();
					}
				}
			}

			audioClip.close();

		}

		public void stop() {
			isStopped = true;
		}

		public void pause() {
			isPaused = true;
		}

		public void resume() {
			isPaused = false;
		}

		@Override
		public void update(LineEvent event) {
			LineEvent.Type type = event.getType();
			if (type == LineEvent.Type.STOP) {
				//System.out.println("STOP EVENT");
				if (isStopped || !isPaused) {
					playCompleted = true;
				}
			}
		}
		public Clip getAudioClip() {
			return audioClip;
		}
	}

	private AudioPlayer player = new AudioPlayer();
	Thread playbackThread;

	Sound(String filename){
		playbackThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					player.load(filename);
					player.play();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		playbackThread.start();
		//System.out.println("play bgm now");
	}
	void pause(){
		player.pause();
		playbackThread.interrupt();
	}
	void resume(){
		player.resume();
		playbackThread.interrupt();
	}
	void stop(){
		player.stop();
		playbackThread.interrupt();
		//System.out.println("stop bgm now");
	}
}

class GameMusic{
	GameEngine.GameOption go;
	Sound bgm;
	GameMusic(GameEngine.GameOption go){
		this.go = go;
	}
	void play(String filename){
		if(!go.playSound)return;
		new Sound(filename);
	}
	void playBGM(String filename){
		bgm = new Sound(filename);
		if(!go.playSound)bgm.pause();
	}
	void toggleBGM(){
		if(bgm == null)return;
		if(go.playSound)	bgm.resume();
		else 				bgm.pause();
		System.out.println("toggle");
	}
	void stopBGM(){
		if(bgm != null)bgm.stop();
	}
}

