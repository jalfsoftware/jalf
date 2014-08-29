package com.jalfsoftware.jalf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jalfsoftware.jalf.Jalf;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "to be named";
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new Jalf(), config);
	}
}
