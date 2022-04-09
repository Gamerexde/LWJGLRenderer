package io.gamerexde.lwjglenderer.Models;

import io.gamerexde.lwjglenderer.Graphics.*;
import io.gamerexde.lwjglenderer.Utils.TransformationUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Plane {
    private final float[] vertices = {
            0.5f,  0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f
    };

    private final byte[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    private final float[] textureCoords = {
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };

    public Texture getTexture() {
        return texture;
    }

    public Shader getShader() {
        return shader;
    }

    public Entity getEntity() {
        return entity;
    }

    private final Texture texture;
    private final Shader shader;
    private final Entity entity;


    public Plane() {
        texture = new Texture("");
        shader = new Shader("shaders/texture.vert", "shaders/texture.frag");

        entity = new Entity(new Mesh(vertices, indices, textureCoords), new Vector3f(0, 0, -1), new Vector3f(0, 0, 0), 1);

        shader.use();
        shader.createUniform("model");
        shader.createUniform("view");
        shader.createUniform("projection");
    }

    public void draw(Matrix4f projection, Camera camera) {
        shader.use();
        shader.setUniform("model", TransformationUtils.createTransformationMatrix(entity));
        shader.setUniform("view", TransformationUtils.getViewMatrix(camera));
        shader.setUniform("projection", projection);

        texture.bind();
        entity.getMesh().draw();
    }


    public void dispose() {
        texture.dispose();
        shader.dispose();
        entity.getMesh().dispose();
    }
}
