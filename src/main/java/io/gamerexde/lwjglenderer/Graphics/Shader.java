package io.gamerexde.lwjglenderer.Graphics;

import io.gamerexde.lwjglenderer.Utils.FileUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL41C.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Shader {

    int program;

    private Map<String, Integer> uniforms = new HashMap<String, Integer>();

    public Shader(String vertexShaderName, String fragmentShaderName) {

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        String vertexShaderSource = FileUtils.loadAsString(vertexShaderName);
        String fragmentShaderSource = FileUtils.loadAsString(fragmentShaderName);

        glShaderSource(vertexShader, vertexShaderSource);
        glShaderSource(fragmentShader, fragmentShaderSource);

        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);

        if (!shaderCompileStatus(vertexShader) || !shaderCompileStatus(fragmentShader)) {
            return;
        }

        program = glCreateProgram();

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        glValidateProgram(program);

        glDetachShader(program, vertexShader);
        glDetachShader(program, fragmentShader);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);


    }

    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(program, uniformName);

        System.out.println(uniformLocation);

        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniform, Matrix4f value) {
        try ( MemoryStack stack = stackPush() ) {
            glUniformMatrix4fv(uniforms.get(uniform), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniform, int value) {
        glUniform1i(uniforms.get(uniform), value);
    }

    public void setUniform(String uniform, Vector3f value) {
        glUniform3f(uniforms.get(uniform), value.x, value.y, value.z);
    }

    public void setUniform(String uniform, Vector4f value) {
        glUniform4f(uniforms.get(uniform), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniform, boolean value) {
        int bool = value ? 1 : 0;

        glUniform1f(uniforms.get(uniform), bool);
    }

    public void setUniform(String uniform, float value) {
        try ( MemoryStack stack = stackPush() ) {
            glUniform1f(uniforms.get(uniform), value);
        }
    }

    public void use() {
        glUseProgram(program);
    }

    public void dispose() {
        glDeleteProgram(program);
    }

    private boolean shaderCompileStatus(int shaderID) {
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile shader!");
            System.err.println(glGetShaderInfoLog(shaderID));
            return false;
        }

        System.out.println("Shader " + shaderID + " compiled successfully.");
        return true;
    }
}
