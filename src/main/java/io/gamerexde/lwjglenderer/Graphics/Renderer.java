package io.gamerexde.lwjglenderer.Graphics;

import io.gamerexde.lwjglenderer.Audio.Audio;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer implements AutoCloseable {
    private long windowPtr;
    private int windowWidth = 1280;
    private int windowHeight = 720;
    private Matrix4f projection = new Matrix4f();

    private Audio audio;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glVersion;
    private String glVendor;
    private boolean glLoaded = false;

    private Camera camera = new Camera(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

    public long getWindow() {
        return windowPtr;
    }
    public Matrix4f getProjection() {
        return projection;
    }

    public Renderer(Audio audio) {
        this.audio = audio;
    }


    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);

        windowPtr = glfwCreateWindow(windowWidth, windowHeight, "Bruh", NULL, NULL);
        if ( windowPtr == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(windowPtr, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        projection.setPerspective(90, (float) windowWidth / windowHeight, 0.01f, 1000f);

        glfwSetWindowSizeCallback(windowPtr, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {

                windowWidth = width;
                windowHeight = height;

                System.out.println(width + " " + height);

                if (glLoaded) {
                    glViewport(0, 0, windowWidth, windowHeight);
                }

                projection.setPerspective(90, (float) windowWidth / windowHeight, 0.01f, 1000f);
            }
        });

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);

            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowPtr, pWidth, pHeight);

            System.out.println(pWidth.get(0) + " " + pHeight.get(0));

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    windowPtr,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        ImGui.createContext();

        glfwShowWindow(windowPtr);
        imGuiGlfw.init(windowPtr, true);

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1);
    }

    public void start() {
        GL.createCapabilities();
        glLoaded = true;

        imGuiGl3.init("#version 400");

        glVersion = glGetString(GL_VERSION);
        glVendor = glGetString(GL_RENDERER);

        glClearColor(0f, 0f, 0f, 0.0f);
        glEnable(GL_DEPTH_TEST);

        audio.getSource().play();
        System.out.println("Source state: " + audio.getSource().getState());
        while ( !glfwWindowShouldClose(windowPtr) ) {
            glClearColor(0.1f, 0.09f, 0.1f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderGUI();

            glfwSwapBuffers(windowPtr);
            glfwPollEvents();
        }
    }

    private void renderGUI() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        ImGui.begin("Info");
        ImGui.text("OpenGL: " + glVersion);
        ImGui.text("Renderer: " + glVendor);
        ImGui.end();

        ImGui.begin("Player Controls");
        boolean playButton = ImGui.button("Play");
        boolean pauseButton = ImGui.button("Pause");
        boolean stopButton = ImGui.button("Stop");

        float[] volume = {audio.getSource().getGain()};
        ImGui.sliderFloat("Volume", volume, 0, 1);

        audio.getSource().setGain(volume[0]);


        if (playButton) {
            audio.getSource().play();
        }

        if (pauseButton) {
            audio.getSource().pause();
        }

        if (stopButton) {
            audio.getSource().stop();
        }


        ImGui.end();
        ImGui.begin("Now Playing");
        if (audio.getTrack() != null) {
            ImGui.text("Song: " + audio.getTrack().getFilename());
        } else {
            ImGui.text("Song: Nothing yet...");
        }
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    @Override
    public void close() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        //steve.dispose();
        glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
