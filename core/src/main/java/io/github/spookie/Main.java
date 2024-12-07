package io.github.spookie;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public PerspectiveCamera cam;
    public AssetManager assets;
    public Environment environment;
    public ModelBatch modelBatch;
    public ModelInstance alphabetsInstance;
    public Model floorModel, wallModel, leftWallModel, rightWallModel, backWallModel, frontWallModel;
    public ModelInstance floorInstance, wallInstance, leftWallInstance, rightWallInstance, backWallInstance, frontWallInstance;

    private float movementSpeed = 15f;
    private Vector3 movement = new Vector3();
    private ShapeRenderer shapeRenderer;
    public boolean loading;

    @Override
    public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 5f, 0f);

        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        assets = new AssetManager();
        modelBatch = new ModelBatch();
        shapeRenderer = new ShapeRenderer();

        ModelBuilder modelBuilder = new ModelBuilder();
        assets.load("alphabets.g3db", Model.class);
        loading = true;

        floorModel = modelBuilder.createBox(100f, 1f, 100f,
            new Material(ColorAttribute.createDiffuse(Color.GRAY)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        floorInstance = new ModelInstance(floorModel);

        wallModel = modelBuilder.createBox(1f, 20f, 101f,
            new Material(ColorAttribute.createDiffuse(Color.valueOf("#e4e6a8"))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        frontWallModel = wallModel;
        backWallModel = wallModel;
        leftWallModel = modelBuilder.createBox(101f, 20f, 1f,
            new Material(ColorAttribute.createDiffuse(Color.valueOf("#e4e6a8"))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        rightWallModel = leftWallModel;
        frontWallInstance = new ModelInstance(frontWallModel);
        backWallInstance = new ModelInstance(backWallModel);
        leftWallInstance = new ModelInstance(leftWallModel);
        rightWallInstance = new ModelInstance(rightWallModel);

        frontWallInstance.transform.setToTranslation(50f, 10f, 0);
        backWallInstance.transform.setToTranslation(-50f, 10f, 0);
        leftWallInstance.transform.setToTranslation(0, 10f, -50f);
        rightWallInstance.transform.setToTranslation(0f, 10f, 50f);

    }

    private void doneLoading() {
        loading = false;
        Model alphabets = assets.get("alphabets.g3db", Model.class);
        ModelInstance alphabetsInstance = new ModelInstance(alphabets);
        alphabetsInstance.transform.setToTranslation(10f, 10f, 0f);
        loading = false;
    }


    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        checkButtonDown();

        modelBatch.begin(cam);
        modelBatch.render(floorInstance, environment);
        modelBatch.render(frontWallInstance, environment);
        modelBatch.render(backWallInstance, environment);
        modelBatch.render(leftWallInstance, environment);
        modelBatch.render(rightWallInstance, environment);
        modelBatch.end();
        drawButtons();
    }

    private void drawButtons(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        //left look
        shapeRenderer.box(100f, 150f, 0f, 150f, 150f, 0f);
        //right look
        shapeRenderer.box(350f, 150f, 0f, 150f, 150f, 0f);
        //front
        shapeRenderer.box(1900f, 350f, 0f, 150f, 150f, 0f);
        //back
        shapeRenderer.box(1900f, 100f, 0f, 150f, 150f, 0f);
        shapeRenderer.end();
    }

    private void checkButtonDown(){
        if(Gdx.input.isTouched()){
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (screenX >= 100f && screenX <= 100f + 150f
                && screenY >= 150f && screenY <= 150f + 150f) {
                cam.rotate(Vector3.Y, 2.5f);  // Rotate 15 degrees left
                cam.update();
            }

            if (screenX >= 350f && screenX <= 350f + 150f
                && screenY >= 150f && screenY <= 150f + 150f) {
                cam.rotate(Vector3.Y, -2.5f);  // Rotate 15 degrees right
                cam.update();
            }

            if (screenX >= 1900f && screenX <= 2050f
                && screenY >= 350f && screenY <= 500f) {
                cam.position.add(cam.direction.cpy().scl(movementSpeed * Gdx.graphics.getDeltaTime()));
                cam.update();
            }

            if (screenX >= 1900 && screenX <= 2050
                && screenY >= 100 && screenY <= 250) {
                cam.position.sub(cam.direction.cpy().scl(movementSpeed * Gdx.graphics.getDeltaTime()));
                cam.update();
            }

        }
    }
    @Override
    public void dispose () {
        modelBatch.dispose();
        floorModel.dispose();
        wallModel.dispose();
        leftWallModel.dispose();
        rightWallModel.dispose();
        backWallModel.dispose();
        frontWallModel.dispose();
    }
    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }
}
