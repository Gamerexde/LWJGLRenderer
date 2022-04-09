package io.gamerexde.lwjglenderer.Audio;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.*;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;

import static org.lwjgl.openal.AL10.*;

public class AudioTrack {
    private final ByteBuffer data;
    private boolean stereo;
    private int samplingFrequency;
    private final int buffer;

    private final String filename;

    public AudioTrack(String path) {
        Path dir = Path.of(path);

        filename = dir.getFileName().toString();

        buffer = alGenBuffers();

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        FFmpegResult fmpegResult = FFmpeg.atPath()
                .addInput(UrlInput.fromUrl(path))
                .addOutput(PipeOutput.pumpTo(os).setFormat("wav").setCodec(StreamType.AUDIO, "pcm_s16le"))
                .execute();

        AudioInputStream stream = null;

        try {
            stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(os.toByteArray()));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        byte[] b = new byte[0];
        try {
            b = stream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AudioFormat format = stream.getFormat();

        int alFormat = -1;
        switch(format.getChannels()) {
            case 1:
                switch (format.getSampleSizeInBits()) {
                    case 8 -> alFormat = AL_FORMAT_MONO8;
                    case 16 -> alFormat = AL_FORMAT_MONO16;
                }
                break;
            case 2:
                alFormat = switch (format.getSampleSizeInBits()) {
                    case 8 -> AL_FORMAT_STEREO8;
                    case 16 -> AL_FORMAT_STEREO16;
                    default -> alFormat;
                };
                break;
        }

        data = BufferUtils.createByteBuffer(b.length).put(b);
        data.flip();

        alBufferData(buffer, alFormat, data, (int) format.getSampleRate());
    }

    public String getFilename() {
        return filename;
    }

    public void dispose() {
        alDeleteBuffers(buffer);
    }

    public int getFormat() {
        return stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getSamplingFrequency() {
        return samplingFrequency;
    }
}
