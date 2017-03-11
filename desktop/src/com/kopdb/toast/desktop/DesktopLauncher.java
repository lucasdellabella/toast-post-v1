package com.kopdb.toast.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kopdb.toast.ToastGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "toast";
		config.width = 540;
		config.height = 910;
		new LwjglApplication(new ToastGame(), config);
	}
}
