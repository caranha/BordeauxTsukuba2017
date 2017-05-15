package com.tskbdx.sumimasen.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tskbdx.sumimasen.Sumimasen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sumimasen";
		config.resizable = false;
		new LwjglApplication(new Sumimasen(), config);
	}
}
