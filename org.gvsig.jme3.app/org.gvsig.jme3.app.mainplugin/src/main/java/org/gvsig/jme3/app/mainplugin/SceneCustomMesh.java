package org.gvsig.jme3.app.mainplugin;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author jjdelcerro
 */
public class SceneCustomMesh
        extends ViewerJme3.AbstractSceneMaker
    {

    @Override
    public void makeScene(SimpleApplication app, ViewerJme3 viewer) {
        Mesh m = new Mesh();

        // Vertex positions in space
        Vector3f[] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0, 0, 0);
        vertices[1] = new Vector3f(3, 0, 0);
        vertices[2] = new Vector3f(0, 3, 0);
        vertices[3] = new Vector3f(3, 3, 0);

        // Texture coordinates
        Vector2f[] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0, 0);
        texCoord[1] = new Vector2f(1, 0);
        texCoord[2] = new Vector2f(0, 1);
        texCoord[3] = new Vector2f(1, 1);

        // Indexes. We define the order in which mesh should be constructed
        short[] indexes = {2, 0, 1, 1, 3, 2};

        // Setting buffers
        m.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        m.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        m.updateBound();

        // *************************************************************************
        // First mesh uses one solid color
        // *************************************************************************
        // Creating a geometry, and apply a single color material to it
        Geometry geom = new Geometry("OurMesh", m);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        // Attaching our geometry to the root node.
        app.getRootNode().attachChild(geom);

        // *************************************************************************
        // Second mesh uses vertex colors to color each vertex
        // *************************************************************************
        Mesh cMesh = m.clone();
        Geometry coloredMesh = new Geometry("ColoredMesh", cMesh);
        Material matVC = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matVC.setBoolean("VertexColor", true);

        //We have 4 vertices and 4 color values for each of them.
        //If you have more vertices, you need 'new float[yourVertexCount * 4]' here!
        float[] colorArray = new float[4 * 4];
        int colorIndex = 0;

        //Set custom RGBA value for each Vertex. Values range from 0.0f to 1.0f
        for (int i = 0; i < 4; i++) {
            // Red value (is increased by .2 on each next vertex here)
            colorArray[colorIndex++] = 0.1f + (.2f * i);
            // Green value (is reduced by .2 on each next vertex)
            colorArray[colorIndex++] = 0.9f - (0.2f * i);
            // Blue value (remains the same in our case)
            colorArray[colorIndex++] = 0.5f;
            // Alpha value (no transparency set here)
            colorArray[colorIndex++] = 1.0f;
        }
        // Set the color buffer
        cMesh.setBuffer(VertexBuffer.Type.Color, 4, colorArray);
        coloredMesh.setMaterial(matVC);
        // move mesh a bit so that it doesn't intersect with the first one
        coloredMesh.setLocalTranslation(4, 0, 0);
        app.getRootNode().attachChild(coloredMesh);

        // *************************************************************************
        // Third mesh will use a wireframe shader to show wireframe
        // *************************************************************************
        Mesh wfMesh = m.clone();
        Geometry wfGeom = new Geometry("wireframeGeometry", wfMesh);
        Material matWireframe = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matWireframe.setColor("Color", ColorRGBA.Green);
        matWireframe.getAdditionalRenderState().setWireframe(true);
        wfGeom.setMaterial(matWireframe);
        wfGeom.setLocalTranslation(4, 4, 0);
        app.getRootNode().attachChild(wfGeom);

    }

}
