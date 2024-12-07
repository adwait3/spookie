package io.github.spookie;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
    public PerspectiveCamera cam;
    public Environment environment;
    public ModelBatch modelBatch;
    public Model floorModel, wallModel, leftWallModel, rightWallModel, backWallModel, frontWallModel;
    public ModelInstance floorInstance, wallInstance, leftWallInstance, rightWallInstance, backWallInstance, frontWallInstance;

    private float yaw = 0f;                  // Yaw for horizontal rotation
    private float movementSpeed = 5f;        // Speed of movement
    private Vector3 movement = new Vector3(); // Movement vector
    private float joystickRadius = 100f;     // Joystick base radius
    private float thumbRadius = 30f;         // Thumbstick radius
    private Vector2 joystickCenter = new Vector2(150f, 150f); // Joystick center
    private Vector2 thumbPosition = new Vector2(joystickCenter); // Thumbstick position
    private ShapeRenderer shapeRenderer;    // For drawing the joystick

    private float lastTouchX = 0;

    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 5f, 0f);
        cam.lookAt(0, 0, -1);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        modelBatch = new ModelBatch();
        shapeRenderer = new ShapeRenderer();

        ModelBuilder modelBuilder = new ModelBuilder();

        // Create floor and walls
        floorModel = modelBuilder.createBox(10f, 1f, 10f,
            new Material(ColorAttribute.createDiffuse(Color.GRAY)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        floorInstance = new ModelInstance(floorModel);

        wallModel = modelBuilder.createBox(1f, 5f, 11f,
            new Material(ColorAttribute.createDiffuse(Color.CYAN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        frontWallModel = wallModel;
        backWallModel = wallModel;
        leftWallModel = modelBuilder.createBox(11f, 5f, 1f,
            new Material(ColorAttribute.createDiffuse(Color.CYAN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        rightWallModel = leftWallModel;

        // Position walls
        frontWallInstance = new ModelInstance(frontWallModel);
        backWallInstance = new ModelInstance(backWallModel);
        leftWallInstance = new ModelInstance(leftWallModel);
        rightWallInstance = new ModelInstance(rightWallModel);

        frontWallInstance.transform.setToTranslation(5f, 2.5f, 0);
        backWallInstance.transform.setToTranslation(-5, 2.5f, 0);
        leftWallInstance.transform.setToTranslation(0, 2.5f, -5f);
        rightWallInstance.transform.setToTranslation(0f, 2.5f, 5f);
    }

    @Override
    public void render() {
        // Handle touch input for rotating the camera
        handleTouchLook();

        // Handle joystick-based movement
        handleJoystickInput();

        // Apply movement to the camera position
        cam.position.add(movement);
        cam.update();

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render the scene
        modelBatch.begin(cam);
        modelBatch.render(floorInstance, environment);
        modelBatch.render(frontWallInstance, environment);
        modelBatch.render(backWallInstance, environment);
        modelBatch.render(leftWallInstance, environment);
        modelBatch.render(rightWallInstance, environment);
        modelBatch.end();

        // Draw the joystick
        drawJoystick();
    }

    private void handleTouchLook() {
        if (Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
            float deltaX = Gdx.input.getX() - lastTouchX;
            yaw += deltaX * 0.2f; // Adjust rotation speed as needed
            lastTouchX = Gdx.input.getX();

            // Update camera's direction
            cam.direction.set((float) Math.cos(Math.toRadians(yaw)), 0, (float) Math.sin(Math.toRadians(yaw))).nor();
        } else {
            lastTouchX = Gdx.input.getX();
        }
    }

    private void handleJoystickInput() {
        if (Gdx.input.isTouched() && Gdx.input.getX() <= Gdx.graphics.getWidth() / 2) {
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            // Calculate thumbstick movement
            Vector2 direction = touchPosition.sub(joystickCenter);
            float distance = direction.len();
            if (distance > joystickRadius) {
                direction.nor().scl(joystickRadius); // Clamp thumbstick to base
            }
            thumbPosition.set(joystickCenter.x + direction.x, joystickCenter.y + direction.y);

            // Convert thumbstick movement to camera movement
            movement.set(direction.x / joystickRadius * movementSpeed, 0, -direction.y / joystickRadius * movementSpeed)
                .scl(Gdx.graphics.getDeltaTime());
        } else {
            thumbPosition.set(joystickCenter); // Reset thumbstick when not touched
            movement.setZero();
        }
    }

    private void drawJoystick() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw joystick base
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(joystickCenter.x, joystickCenter.y, joystickRadius);

        // Draw thumbstick
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(thumbPosition.x, thumbPosition.y, thumbRadius);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        floorModel.dispose();
        wallModel.dispose();
        leftWallModel.dispose();
        rightWallModel.dispose();
        backWallModel.dispose();
        frontWallModel.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
