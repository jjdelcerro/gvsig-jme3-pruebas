package org.gvsig.jme3.app.mainplugin;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author jjdelcerro
 */
public class SceneSimple
        extends ViewerJme3.AbstractSceneMaker
    {

    @Override
    public void makeScene(SimpleApplication app, ViewerJme3 viewer) {
        app.getFlyByCamera().setDragToRotate(true);

        // Esto es la geometria 
        // https://javadoc.jmonkeyengine.org/v3.x/com/jme3/scene/Mesh.html
        Box b = new Box(1, 1, 1);

        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", app.getAssetManager().loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        app.getRootNode().attachChild(geom);
    }

}
