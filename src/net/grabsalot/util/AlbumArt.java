/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.grabsalot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import net.grabsalot.business.Cacher;
import net.grabsalot.business.Logger;
import net.grabsalot.dao.local.LocalAlbum;
import net.grabsalot.gui.MainFrame;
import net.grabsalot.gui.progress.ProgressWindow;


/**
 *
 * @author madboyka
 */
public class AlbumArt {

	public static void grab(LocalAlbum album) {
		try {
			album.saveCover();
		} catch (Exception ex) {
			Logger.getLogger(AlbumArt.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void grabAll(LocalAlbum[] albums) {
		AlbumArtGrabber aag = new AlbumArtGrabber();
		ProgressWindow progress = MainFrame.getInstance().createProgressWindow("Grabbing Albums...");
		progress.display();
		aag.setProgressWindow(progress);
		aag.addAlbums(albums);

		aag.start();
	}

	private static class AlbumArtGrabber extends Thread {

		private ArrayList<LocalAlbum> albums;
		private boolean useProgressWindow;
		private ProgressWindow progress;

		public AlbumArtGrabber() {
			albums = new ArrayList<LocalAlbum>();
		}

		@Override
		public void run() {
			logProgress("Starting album art grabber...");
			int saved = 0;
			int errors = 0;
			for (LocalAlbum album : albums) {
				try {
					album.loadTracks();
					File coverFile = album.saveCover();

					logProgress("Cover art saved as :" + coverFile.getAbsolutePath());
					saved++;
				} catch (Exception ex) {
					logProgress("Failed to save cover art :" + ex.getMessage());
					errors++;
				}
			}
			logProgress("Saved " + saved + " covers, with " + errors + " errors.");
		}

		private void logProgress(Object message) {
			if (useProgressWindow) {
				progress.showMessage(message.toString());
			} else {
				Logger._().log(message.toString());
			}
		}

		private void addAlbums(LocalAlbum[] albums) {
			this.albums.addAll(Arrays.asList(albums));
		}

		private void setProgressWindow(ProgressWindow progress) {
			this.progress = progress;
			useProgressWindow = this.progress != null;
		}
	}
}
