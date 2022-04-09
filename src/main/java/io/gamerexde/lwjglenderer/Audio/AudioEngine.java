package io.gamerexde.lwjglenderer.Audio;

import org.lwjgl.openal.*;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.NULL;

public class AudioEngine {
    public long getAlDevice() {
        return alDevice;
    }

    public ALCCapabilities getAlcCapabilities() {
        return alcCapabilities;
    }

    public long getAlContext() {
        return alContext;
    }

    public ALCapabilities getAlCapabilities() {
        return alCapabilities;
    }

    private final long alDevice;
    private final ALCCapabilities alcCapabilities;
    private final long alContext;
    private final ALCapabilities alCapabilities;


    public AudioEngine() {
        String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        alDevice = ALC10.alcOpenDevice(defaultDeviceName);
        if (alDevice == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        alcCapabilities = ALC.createCapabilities(alDevice);
        alContext = ALC10.alcCreateContext(alDevice, (IntBuffer) null);
        if (alContext == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        ALC10.alcMakeContextCurrent(alContext);
        alCapabilities = AL.createCapabilities(alcCapabilities);
    }
}
