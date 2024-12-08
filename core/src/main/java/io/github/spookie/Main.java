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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public PerspectiveCamera cam;
    public Environment environment;
    public ModelBatch modelBatch;
    private List<Model> Models = new ArrayList<>();
    private List<ModelInstance> ModelInstances = new ArrayList<>();
    private AssetManager assetManager;
    public Model floorModel, wallModel, leftWallModel, rightWallModel, backWallModel, frontWallModel;
    public ModelInstance floorInstance, wallInstance, leftWallInstance, rightWallInstance, backWallInstance, frontWallInstance;

    private float movementSpeed = 500f;
    private Vector3 movement = new Vector3();
    private ShapeRenderer shapeRenderer;

    @Override
    public void create () {
        assetManager = new AssetManager();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        cam = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0f, 5f, 0f);
        cam.near=1f;
        cam.far = 5000f;
        cam.update();

        loadModels();

        modelBatch = new ModelBatch();
        shapeRenderer = new ShapeRenderer();

        ModelBuilder modelBuilder = new ModelBuilder();
        // modelBuilder.begin();
        floorModel = modelBuilder.createBox(2000f, 1f, 2000f,
            new Material(ColorAttribute.createDiffuse(Color.GRAY)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        floorInstance = new ModelInstance(floorModel);

        wallModel = modelBuilder.createBox(1f, 500f, 2001f,
            new Material(ColorAttribute.createDiffuse(Color.valueOf("#e4e6a8"))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        frontWallModel = wallModel;
        backWallModel = wallModel;
        leftWallModel = modelBuilder.createBox(2001f, 500f, 1f,
            new Material(ColorAttribute.createDiffuse(Color.valueOf("#e4e6a8"))),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        rightWallModel = leftWallModel;
        frontWallInstance = new ModelInstance(frontWallModel);
        backWallInstance = new ModelInstance(backWallModel);
        leftWallInstance = new ModelInstance(leftWallModel);
        rightWallInstance = new ModelInstance(rightWallModel);

        frontWallInstance.transform.setToTranslation(1000f, 250f, 0);
        backWallInstance.transform.setToTranslation(-1000f, 250f, 0);
        leftWallInstance.transform.setToTranslation(0, 250f, -1000f);
        rightWallInstance.transform.setToTranslation(0f, 250f, 1000f);

    }

    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        checkButtonDown();

        modelBatch.begin(cam);

        for (ModelInstance alphabetInstance : ModelInstances) {
            modelBatch.render(alphabetInstance, environment);
        }

        modelBatch.render(floorInstance, environment);
        modelBatch.render(frontWallInstance, environment);
        modelBatch.render(backWallInstance, environment);
        modelBatch.render(leftWallInstance, environment);
        modelBatch.render(rightWallInstance, environment);
        modelBatch.end();

        drawButtons();

    }
    private void loadModels() {
        // Load models for a-z
        for (char i = 0; i <= 187; i++) {
            String modelName = Integer.toString(i) + ".g3db";
            assetManager.load(modelName, Model.class);
        }

        // Finish loading all models
        assetManager.finishLoading();

        float xOffset = 0f; // Starting x position
        float yOffset = 0f;    // Fixed y position
        float zOffset = 0f;    // Fixed z position

        for (char i = 0; i <= 187; i++) {
            String modelName = Integer.toString(i) + ".g3db";
            Model model = assetManager.get(modelName, Model.class);
            ModelInstance instance = new ModelInstance(model);
            instance.transform.setToTranslation(xOffset, yOffset, zOffset);

            Models.add(model);
            ModelInstances.add(instance);

            xOffset += 100f;
        }

        ModelInstances.get(74).transform.setToTranslation(2360f, 750f, 610f);
        ModelInstances.get(75).transform.setToTranslation(-7270f, 360f, 1370f);
        ModelInstances.get(76).transform.setToTranslation(3750f, -590f, 7530f);
        ModelInstances.get(77).transform.setToTranslation(6730f, -360f, -540f);
        ModelInstances.get(78).transform.setToTranslation(3920f, -250f, -7350f);
        ModelInstances.get(79).transform.setToTranslation(6870f, -770f, -2270f);
        ModelInstances.get(80).transform.setToTranslation(4530f, -700f, 4640f);
        ModelInstances.get(81).transform.setToTranslation(4230f, -340f, -2370f);
        ModelInstances.get(82).transform.setToTranslation(4470f, -130f, 2040f);
        ModelInstances.get(83).transform.setToTranslation(-4110f, 270f, 3740f);
        ModelInstances.get(84).transform.setToTranslation(6360f, 720f, 6950f);

        ModelInstances.get(85).transform.setToTranslation(-2100f, 350f, 3640f);
        ModelInstances.get(86).transform.setToTranslation(560f, -390f, -7550f);
        ModelInstances.get(87).transform.setToTranslation(2070f, -640f, -2910f);
        ModelInstances.get(88).transform.setToTranslation(7860f, 380f, -3840f);
        ModelInstances.get(89).transform.setToTranslation(-4830f, -110f, 5130f);
        ModelInstances.get(90).transform.setToTranslation(-4510f, -560f, -5270f);
        ModelInstances.get(91).transform.setToTranslation(840f, 680f, 6550f);
        ModelInstances.get(92).transform.setToTranslation(240f, 340f, -2890f);
        ModelInstances.get(93).transform.setToTranslation(-4110f, 180f, 100f);
        ModelInstances.get(94).transform.setToTranslation(4470f, -70f, 4600f);
        ModelInstances.get(95).transform.setToTranslation(-3630f, 680f, -1310f);
        ModelInstances.get(96).transform.setToTranslation(-4660f, 710f, -6440f);
        ModelInstances.get(97).transform.setToTranslation(-2770f, 330f, 2490f);
        ModelInstances.get(98).transform.setToTranslation(1630f, -710f, -380f);
        ModelInstances.get(99).transform.setToTranslation(-6820f, 230f, -260f);
        ModelInstances.get(100).transform.setToTranslation(-4480f, 760f, -2330f);
        ModelInstances.get(101).transform.setToTranslation(-370f, -730f, -1740f);
        ModelInstances.get(102).transform.setToTranslation(2200f, 400f, 2460f);
        ModelInstances.get(103).transform.setToTranslation(1850f, -160f, 3400f);
        ModelInstances.get(104).transform.setToTranslation(-2840f, 20f, -3270f);
        ModelInstances.get(105).transform.setToTranslation(-7740f, 120f, 6150f);
        ModelInstances.get(106).transform.setToTranslation(3140f, 290f, -460f);
        ModelInstances.get(107).transform.setToTranslation(-1860f, 700f, 130f);
        ModelInstances.get(108).transform.setToTranslation(-6880f, 30f, 5600f);
        ModelInstances.get(109).transform.setToTranslation(5870f, -590f, -4930f);
        ModelInstances.get(110).transform.setToTranslation(2320f, 70f, 6870f);
        ModelInstances.get(111).transform.setToTranslation(-6280f, 670f, 4360f);
        ModelInstances.get(112).transform.setToTranslation(-1080f, -670f, -6050f);
        ModelInstances.get(113).transform.setToTranslation(-6210f, 730f, -5150f);
        ModelInstances.get(114).transform.setToTranslation(580f, -550f, -1990f);
        ModelInstances.get(115).transform.setToTranslation(-6690f, 140f, 1790f);
        ModelInstances.get(116).transform.setToTranslation(-2430f, -30f, 3870f);
        ModelInstances.get(117).transform.setToTranslation(6500f, 200f, -5970f);
        ModelInstances.get(118).transform.setToTranslation(3030f, 110f, 6160f);
        ModelInstances.get(119).transform.setToTranslation(-7780f, 170f, -7630f);
        ModelInstances.get(120).transform.setToTranslation(-6240f, -370f, 3390f);

        ModelInstances.get(32).transform.setToTranslation(-1200f,-200f,-3030f);
        ModelInstances.get(121).transform.setToTranslation(5670f, -160f, 6230f);
        ModelInstances.get(122).transform.setToTranslation(7020f, -460f, -5410f);
        ModelInstances.get(123).transform.setToTranslation(1920f, -540f, -2460f);
        ModelInstances.get(124).transform.setToTranslation(200f, -100f, 7120f);
        ModelInstances.get(125).transform.setToTranslation(6930f, 530f, 2000f);
        ModelInstances.get(126).transform.setToTranslation(840f, -60f, -1570f);
        ModelInstances.get(127).transform.setToTranslation(-5880f, -350f, 5520f);
        ModelInstances.get(128).transform.setToTranslation(-1340f, -430f, 1730f);
        ModelInstances.get(129).transform.setToTranslation(1580f, 100f, 1450f);
        ModelInstances.get(130).transform.setToTranslation(-2830f, -600f, -4530f);
        ModelInstances.get(131).transform.setToTranslation(-2960f, -580f, 3310f);
        ModelInstances.get(132).transform.setToTranslation(1240f, 470f, -2370f);
        ModelInstances.get(133).transform.setToTranslation(6440f, -610f, 5150f);
        ModelInstances.get(134).transform.setToTranslation(6150f, 760f, -4720f);
        ModelInstances.get(135).transform.setToTranslation(3070f, 360f, -5180f);
        ModelInstances.get(136).transform.setToTranslation(-4620f, -540f, 810f);

        ModelInstances.get(21).transform.setToTranslation(-1400f,-220f,-3050f);
        ModelInstances.get(137).transform.setToTranslation(5980f, 240f, -4140f);
        ModelInstances.get(138).transform.setToTranslation(3990f, 410f, -760f);
        ModelInstances.get(139).transform.setToTranslation(6620f, 550f, -1120f);
        ModelInstances.get(140).transform.setToTranslation(5040f, 120f, -3690f);
        ModelInstances.get(141).transform.setToTranslation(4280f, 300f, 5900f);


        ModelInstances.get(26).transform.setToTranslation(-1600f,-210f,-3030f);
        ModelInstances.get(142).transform.setToTranslation(5740f, 460f, 4470f);

        ModelInstances.get(143).transform.setToTranslation(610f, 740f, 7560f);
        ModelInstances.get(144).transform.setToTranslation(-5330f, 300f, -1870f);
        ModelInstances.get(145).transform.setToTranslation(5900f, -580f, 5490f);
        ModelInstances.get(146).transform.setToTranslation(-1210f, 660f, -7790f);
        ModelInstances.get(147).transform.setToTranslation(5480f, 80f, 3510f);
        ModelInstances.get(148).transform.setToTranslation(7990f, -600f, 5800f);
        ModelInstances.get(149).transform.setToTranslation(2950f, 770f, 7600f);



        ModelInstances.get(0).transform.setToTranslation(-400f,-200f,-3200f);
        ModelInstances.get(35).transform.setToTranslation(-200f,-210f,-3000f);
        ModelInstances.get(12).transform.setToTranslation(0f,-320f,-3500f);
        ModelInstances.get(33).transform.setToTranslation(200f,-210f,-3040f);
        ModelInstances.get(40).transform.setToTranslation(400f,-300f,-3200f);
        ModelInstances.get(15).transform.setToTranslation(600f,-210f,-3180f);
        ModelInstances.get(41).transform.setToTranslation(800f,-320f,-3050f);
        ModelInstances.get(25).transform.setToTranslation(1000f,-220f,-3120f);
        ModelInstances.get(3).transform.setToTranslation(1200f,-210f,-3200f);
        ModelInstances.get(10).transform.setToTranslation(1400f,-220f,-3000f);


        ModelInstances.get(158).transform.setToTranslation(800f, 0f, 830f);
        ModelInstances.get(159).transform.setToTranslation(500f, 0f, 120f);
        ModelInstances.get(160).transform.setToTranslation(250f, 0f, 370f);
        ModelInstances.get(161).transform.setToTranslation(-480f, 0f, 720f);
        ModelInstances.get(162).transform.setToTranslation(360f, 0f, -440f);

        ModelInstances.get(17).transform.setToTranslation(-1000f,-220f,-3090f);
        ModelInstances.get(157).transform.setToTranslation(560f, 0f, -690f);
        ModelInstances.get(163).transform.setToTranslation(750f, 0f, 800f);
        ModelInstances.get(164).transform.setToTranslation(710f, 0f, -880f);
        ModelInstances.get(165).transform.setToTranslation(720f, 0f, -640f);
        ModelInstances.get(166).transform.setToTranslation(180f, 0f, -440f);
        ModelInstances.get(167).transform.setToTranslation(780f, 0f, -130f);
        ModelInstances.get(168).transform.setToTranslation(620f, 0f, 210f);
        ModelInstances.get(169).transform.setToTranslation(-740f, 0f, 230f);
        ModelInstances.get(170).transform.setToTranslation(-110f, 0f, -300f);
        ModelInstances.get(171).transform.setToTranslation(-300f, 0f, 820f);

        ModelInstances.get(11).transform.setToTranslation(-800f,-210f,-3230f);
        ModelInstances.get(172).transform.setToTranslation(740f, 0f, -630f);
        ModelInstances.get(173).transform.setToTranslation(810f, 0f, -850f);
        ModelInstances.get(174).transform.setToTranslation(510f, 0f, 820f);
        ModelInstances.get(175).transform.setToTranslation(590f, 0f, -90f);
        ModelInstances.get(176).transform.setToTranslation(-590f, 0f, -580f);
        ModelInstances.get(177).transform.setToTranslation(-230f, 0f, -400f);
        ModelInstances.get(178).transform.setToTranslation(750f, 0f, -50f);
        ModelInstances.get(179).transform.setToTranslation(-410f, 0f, 660f);
        ModelInstances.get(180).transform.setToTranslation(-780f, 0f, 210f);

        ModelInstances.get(156).transform.setToTranslation(320f, 0f, 350f);
        ModelInstances.get(181).transform.setToTranslation(-40f, 0f, 140f);
        ModelInstances.get(182).transform.setToTranslation(570f, 0f, 330f);
        ModelInstances.get(183).transform.setToTranslation(-210f, 0f, -520f);
        ModelInstances.get(184).transform.setToTranslation(370f, 0f, 500f);
        ModelInstances.get(185).transform.setToTranslation(-210f, 0f, -670f);
        ModelInstances.get(186).transform.setToTranslation(760f, 0f, -500f);
        ModelInstances.get(187).transform.setToTranslation(-190f, 0f, 460f);


        ModelInstances.get(4).transform.setToTranslation(-1200f, -910f, -3250f);
        ModelInstances.get(5).transform.setToTranslation(-1100f, -940f, -3300f);
        ModelInstances.get(6).transform.setToTranslation(-1000f, 470f, -3400f);

        ModelInstances.get(39).transform.setToTranslation(-600f,-200f,-3230f);
        ModelInstances.get(7).transform.setToTranslation(-900f, -770f, -3450f);
        ModelInstances.get(8).transform.setToTranslation(-800f, -900f, -3500f);
        ModelInstances.get(9).transform.setToTranslation(-700f, 940f, -3550f);

        ModelInstances.get(13).transform.setToTranslation(-500f, -980f, -3600f);
        ModelInstances.get(14).transform.setToTranslation(-400f, 420f, -3650f);
        ModelInstances.get(16).transform.setToTranslation(-300f, 450f, -3700f);
        ModelInstances.get(18).transform.setToTranslation(9600f, -420f, -3800f);
        ModelInstances.get(19).transform.setToTranslation(2600f, -260f, -9750f);

        ModelInstances.get(1).transform.setToTranslation(-1500f, 350f, -3100f);
        ModelInstances.get(20).transform.setToTranslation(3600f, 490f, -3700f);
        ModelInstances.get(22).transform.setToTranslation(4600f, 520f, -3650f);

        ModelInstances.get(155).transform.setToTranslation(-430f, 0f, 270f);
        ModelInstances.get(24).transform.setToTranslation(6600f, -5220f, -3550f);
        ModelInstances.get(27).transform.setToTranslation(8700f, 60f, -3500f);
        ModelInstances.get(28).transform.setToTranslation(9700f, -480f, -3450f);
        ModelInstances.get(29).transform.setToTranslation(1000f, -500f, -3400f);

        ModelInstances.get(2).transform.setToTranslation(-1300f, 380f, -3150f);
        ModelInstances.get(30).transform.setToTranslation(1100f, 510f, -3350f);

        ModelInstances.get(42).transform.setToTranslation(2800f, -520f, -5570f);
        ModelInstances.get(31).transform.setToTranslation(1200f, -520f, -3300f);
        ModelInstances.get(34).transform.setToTranslation(1400f, 40f, -3250f);
        ModelInstances.get(36).transform.setToTranslation(1600f, -320f, -3200f);
        ModelInstances.get(37).transform.setToTranslation(1700f, 550f, -3150f);
        ModelInstances.get(38).transform.setToTranslation(1800f, 760f, -3100f);





        ModelInstances.get(43).transform.setToTranslation(970f, 320f, 5550f);
        ModelInstances.get(44).transform.setToTranslation(1460f, 410f, 1460f);
        ModelInstances.get(45).transform.setToTranslation(2180f, -300f, 6290f);
        ModelInstances.get(46).transform.setToTranslation(5080f, 540f, -6810f);
        ModelInstances.get(47).transform.setToTranslation(2910f, 350f, 580f);

        ModelInstances.get(23).transform.setToTranslation(9760f, -530f, -3600f);
        ModelInstances.get(48).transform.setToTranslation(6010f, 500f, -990f);
        ModelInstances.get(49).transform.setToTranslation(6290f, -320f, 7320f);
        ModelInstances.get(50).transform.setToTranslation(7630f, -450f, -4820f);
        ModelInstances.get(51).transform.setToTranslation(5090f, 770f, 6410f);
        ModelInstances.get(52).transform.setToTranslation(3680f, 50f, -3270f);
        ModelInstances.get(53).transform.setToTranslation(-5640f, -20f, -4470f);
        ModelInstances.get(54).transform.setToTranslation(4920f, -400f, -3190f);
        ModelInstances.get(55).transform.setToTranslation(-2110f, -650f, 5300f);
        ModelInstances.get(56).transform.setToTranslation(-5140f, -160f, -5860f);
        ModelInstances.get(57).transform.setToTranslation(2650f, -30f, -2380f);
        ModelInstances.get(58).transform.setToTranslation(1750f, 10f, 4190f);
        ModelInstances.get(59).transform.setToTranslation(-2520f, 130f, 7920f);
        ModelInstances.get(60).transform.setToTranslation(-4950f, 540f, -3150f);
        ModelInstances.get(61).transform.setToTranslation(-4600f, -350f, 4520f);
        ModelInstances.get(62).transform.setToTranslation(-400f, 100f, -4700f);
        ModelInstances.get(63).transform.setToTranslation(-4970f, -50f, -420f);
        ModelInstances.get(64).transform.setToTranslation(5020f, -640f, 6870f);

        ModelInstances.get(154).transform.setToTranslation(-160f, 0f, 460f);
        ModelInstances.get(65).transform.setToTranslation(-5520f, -740f, 5660f);
        ModelInstances.get(66).transform.setToTranslation(-6310f, 30f, -2150f);
        ModelInstances.get(67).transform.setToTranslation(5310f, -310f, 5120f);
        ModelInstances.get(68).transform.setToTranslation(1260f, -740f, -4230f);
        ModelInstances.get(69).transform.setToTranslation(4330f, -60f, 360f);
        ModelInstances.get(70).transform.setToTranslation(-4230f, 280f, -7100f);
        ModelInstances.get(71).transform.setToTranslation(2400f, -560f, -1340f);
        ModelInstances.get(72).transform.setToTranslation(-6140f, 390f, 7850f);
        ModelInstances.get(73).transform.setToTranslation(1290f, 740f, 2230f);

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
        for (Model model : Models) {
            model.dispose();
        }

        if (assetManager != null) {
            assetManager.dispose();
        }

//        if (alphabetsModel != null) {
//            alphabetsModel.dispose();
//        }
//        if (assetManager != null) {
//            assetManager.dispose();
//        }
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
