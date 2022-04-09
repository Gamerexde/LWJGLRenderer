package io.gamerexde.lwjglenderer.Audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class AudioSource {
    private final int source;

    private float gain = 1f, pitch = 1f;

    private Vector3f position = new Vector3f();

    public AudioSource() {
        source = alGenSources();

        alSourcef(source, AL_GAIN, 1);
        alSourcef(source, AL_PITCH, 1);
        alSource3f(source, AL_POSITION, 0, 0, 0);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        alSource3f(source, AL_POSITION, position.x, position.y, position.z);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        alSourcef(source, AL_PITCH, pitch);
    }

    public void setGain(float gain) {
        this.gain = gain;
        alSourcef(source, AL_GAIN, gain);
    }

    public float getPitch() {
        return pitch;
    }

    public float getGain() {
        return gain;
    }

    public int getState() {
        return AL10.alGetSourcei(source, AL_SOURCE_STATE);
    }

    public void play() {
        alSourcePlay(source);
    }

    public void pause() {
        alSourcePause(source);
    }

    public void stop() {
        alSourceStop(source);
    }


    public void setBuffer(int buffer) {
        stop();
        alSourcei(source, AL_BUFFER, buffer);
    }
}
