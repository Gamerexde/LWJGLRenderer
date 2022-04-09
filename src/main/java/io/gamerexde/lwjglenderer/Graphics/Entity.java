package io.gamerexde.lwjglenderer.Graphics;

import org.joml.Vector3f;

public class Entity {
    private Mesh mesh;
    private Vector3f position, rotation;
    private float scale;

    public Entity(Mesh mesh, Vector3f position, Vector3f rotation, float scale) {
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void addToPos(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void subtractToPos(float x, float y, float z) {
        this.position.x -= x;
        this.position.y -= y;
        this.position.z -= z;
    }

    public void setPos(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void addToRot(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void subtractToRot(float x, float y, float z) {
        this.rotation.x -= x;
        this.rotation.y -= y;
        this.rotation.z -= z;
    }

    public void setRot(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

}
