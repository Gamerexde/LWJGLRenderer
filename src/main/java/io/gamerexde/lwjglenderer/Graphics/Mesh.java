package io.gamerexde.lwjglenderer.Graphics;

import io.gamerexde.lwjglenderer.Utils.BufferUtils;

import static org.lwjgl.opengl.GL41C.*;

public class Mesh {
    int vbo, vao, ibo, tbo;

    private float[] vertices;
    private float[] texCoords;
    private byte[] indicies;

    public Mesh(float[] vertices, byte[] indicies, float[] texCoords) {
        this.vertices = vertices;
        this.indicies = indicies;
        this.texCoords = texCoords;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(this.vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(0);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(this.texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(this.indicies), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void dispose() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
        glDeleteBuffers(tbo);
        glDeleteBuffers(vao);
    }

    public void draw() {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indicies.length, GL_UNSIGNED_BYTE, 0);
    }

    public int getVao() {
        return vao;
    }
}
