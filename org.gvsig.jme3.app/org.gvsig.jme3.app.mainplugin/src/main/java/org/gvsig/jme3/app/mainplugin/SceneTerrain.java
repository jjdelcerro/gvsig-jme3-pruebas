package org.gvsig.jme3.app.mainplugin;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import org.gvsig.jme3.app.mainplugin.ViewerJme3.AbstractSceneMaker;

/**
 *
 * @author jjdelcerro
 */
public class SceneTerrain 
        extends AbstractSceneMaker
    {

    private ViewerJme3 panel;
    private SimpleApplication app;
    
    @Override
    public void makeScene(SimpleApplication app, ViewerJme3 viewer) {
                TerrainQuad terrain;
                Material matRock;
                Material matWire;
                float grassScale = 64;
                float dirtScale = 16;
                float rockScale = 128;

                app.getFlyByCamera().setMoveSpeed(50);
                
                // First, we load up our textures and the heightmap texture for the terrain

                // TERRAIN TEXTURE material
                matRock = new Material(app.getAssetManager(), "Common/MatDefs/Terrain/Terrain.j3md");
                matRock.setBoolean("useTriPlanarMapping", false);

                // ALPHA map (for splat textures)
                matRock.setTexture("Alpha", app.getAssetManager().loadTexture("Textures/Terrain/splat/alphamap.png"));

                // HEIGHTMAP image (for the terrain heightmap)
                Texture heightMapImage = app.getAssetManager().loadTexture("Textures/Terrain/splat/mountains512.png");

                // GRASS texture
                Texture grass = app.getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
                grass.setWrap(Texture.WrapMode.Repeat);
                matRock.setTexture("Tex1", grass);
                matRock.setFloat("Tex1Scale", grassScale);

                // DIRT texture
                Texture dirt = app.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
                dirt.setWrap(Texture.WrapMode.Repeat);
                matRock.setTexture("Tex2", dirt);
                matRock.setFloat("Tex2Scale", dirtScale);

                // ROCK texture
                Texture rock = app.getAssetManager().loadTexture("Textures/Terrain/splat/road.jpg");
                rock.setWrap(Texture.WrapMode.Repeat);
                matRock.setTexture("Tex3", rock);
                matRock.setFloat("Tex3Scale", rockScale);

                // WIREFRAME material
                matWire = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                matWire.getAdditionalRenderState().setWireframe(true);
                matWire.setColor("Color", ColorRGBA.Green);

                // CREATE HEIGHTMAP
                AbstractHeightMap heightmap = null;
                try {
                    //heightmap = new HillHeightMap(1025, 1000, 50, 100, (byte) 3);

                    heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
                    heightmap.load();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                 * Here we create the actual terrain. The tiles will be 65x65, and the total size of the
                 * terrain will be 513x513. It uses the heightmap we created to generate the height values.
                 */
                /**
                 * Optimal terrain patch size is 65 (64x64).
                 * The total size is up to you. At 1025 it ran fine for me (200+FPS), however at
                 * size=2049, it got really slow. But that is a jump from 2 million to 8 million triangles...
                 */
                terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
                TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
                control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
                terrain.addControl(control);
                terrain.setMaterial(matRock);
                terrain.setLocalTranslation(0, -100, 0);
                terrain.setLocalScale(2f, 0.5f, 2f);
                terrain.addControl(new RigidBodyControl(0));
                app.getRootNode().attachChild(terrain);

                Spatial elephant = (Spatial) app.getAssetManager().loadModel("Models/Elephant/Elephant.mesh.xml");
                elephant.setLocalTranslation(new Vector3f(0, 15, 5));
                elephant.setLocalTranslation(new Vector3f(-142, -66, 17));
                elephant.scale(0.05f, 0.05f, 0.05f);
                viewer.addElement("Elephant", elephant);
                app.getRootNode().attachChild(elephant);
                
                Spatial tank = (Spatial) app.getAssetManager().loadModel("Models/HoverTank/Tank2.mesh.xml");
                tank.setLocalTranslation(new Vector3f(0, 20, -1));
                tank.setLocalTranslation(new Vector3f(-142, -66, 17));
                tank.scale(0.05f, 0.05f, 0.05f);
                viewer.addElement("Tank", tank);
                app.getRootNode().attachChild(tank);
        
                DirectionalLight light = new DirectionalLight();
                light.setDirection((new Vector3f(-0.5f, -1f, -0.5f)).normalize());
                app.getRootNode().addLight(light);
                
                app.getCamera().setLocation(new Vector3f(0, 10, -10));
                app.getCamera().lookAtDirection(new Vector3f(0, -1.5f, -1).normalizeLocal(), Vector3f.UNIT_Y);
                
    }

}
