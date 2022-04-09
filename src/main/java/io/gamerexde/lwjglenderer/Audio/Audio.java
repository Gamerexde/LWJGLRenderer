package io.gamerexde.lwjglenderer.Audio;

public class Audio {
    private AudioEngine audioEngine;

    public AudioSource getSource() {
        return source;
    }

    private final AudioSource source;

    public AudioTrack getTrack() {
        return track;
    }

    private final AudioTrack track;

    public Audio() {
        audioEngine = new AudioEngine();
        track = new AudioTrack("");
        source = new AudioSource();


        source.setBuffer(track.getBuffer());
    }



}
