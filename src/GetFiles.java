import java.io.File;
import funnytrees.JOsu.OsuData.OsuBeatmap;


public class GetFiles {
	public static void main(String args[]) {
		File songf = new File("Songs");
		File[] songs = songf.listFiles();
		String[] files;
		OsuBeatmap map = new OsuBeatmap();
		
		for (int i=0; i<songs.length; i++) {
			if (songs[i].isDirectory()) {
				files = songs[i].list();
				for (int j=0; j<files.length; j++) {
					System.out.println(songs[i].toString() + "\\" + files[j]);
				}
			}
		}
	}
}
