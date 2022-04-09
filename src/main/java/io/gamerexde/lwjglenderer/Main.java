package io.gamerexde.lwjglenderer;

import io.gamerexde.lwjglenderer.Audio.Audio;
import io.gamerexde.lwjglenderer.Graphics.Renderer;

public class Main {

    private Renderer renderer;
    private Audio audio;

    public Main() {
        audio = new Audio();
        renderer = new Renderer(audio);

        Thread renderThread = new Thread(() -> {
            renderer.init();
            renderer.start();
            renderer.close();
        });

        renderThread.start();

        try {
            renderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { new Main(); }
}
