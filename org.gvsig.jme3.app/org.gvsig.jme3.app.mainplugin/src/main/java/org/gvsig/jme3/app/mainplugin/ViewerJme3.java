package org.gvsig.jme3.app.mainplugin;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JComponent;
import org.gvsig.tools.swing.api.Component;
import org.gvsig.tools.util.LabeledValue;
import org.gvsig.tools.util.LabeledValueImpl;

/**
 *
 * @author jjdelcerro
 */
public class ViewerJme3
        extends ViewerJme3view
        implements Component, AnalogListener {

    public interface SceneMaker {

        public void makeScene(SimpleApplication app, ViewerJme3 viewer);

        public void initialize();

    }
    
    public abstract static class AbstractSceneMaker
            implements SceneMaker {

        @Override
        public void initialize() {
        }
    
    }

    private Vector3f direction = new Vector3f();
    private SimpleApplication app;
    private SceneMaker sceneMaker;

    public ViewerJme3() {

    }

    public void setSceneMaker(SceneMaker sceneMaker) {
        this.sceneMaker = sceneMaker;
    }

    @Override
    public JComponent asJComponent() {
        if (this.app == null) {
            pnlScene.setPreferredSize(new Dimension(640, 480));
            Thread th = new Thread(new Runnable() {
                public void run() {
                    initializeJme3Application();
                }
            }, "ViewerJme3");
            th.setContextClassLoader(this.getClass().getClassLoader());
            th.start();
        }
        return this;
    }

    private void initializeJme3Application() {
        app = new SimpleApplication() {
            @Override
            public void initialize() {
                super.initialize();
                sceneMaker.initialize();
            }
            
            @Override
            public void simpleInitApp() {
                app.getInputManager().addMapping("lostFocus", new KeyTrigger(KeyInput.KEY_ESCAPE));
                app.getInputManager().addListener(new ActionListener() {
                    @Override
                    public void onAction(String string, boolean bln, float f) {
                        doLostFocus();
                    }
                }, "lostFocus");

                app.getInputManager().addMapping("moveForward", new KeyTrigger(KeyInput.KEY_I));
                app.getInputManager().addMapping("moveBackward", new KeyTrigger(KeyInput.KEY_K));
                app.getInputManager().addMapping("moveRight", new KeyTrigger(KeyInput.KEY_L));
                app.getInputManager().addMapping("moveLeft", new KeyTrigger(KeyInput.KEY_J));
                app.getInputManager().addMapping("moveUp", new KeyTrigger(KeyInput.KEY_Y));
                app.getInputManager().addMapping("moveDown", new KeyTrigger(KeyInput.KEY_H));
                app.getInputManager().addListener(ViewerJme3.this, "moveForward", "moveBackward", "moveRight", "moveLeft", "moveUp", "moveDown");

                sceneMaker.makeScene(app, ViewerJme3.this);

            }
        };

        AppSettings settings = new AppSettings(true);
        settings.setWidth(pnlScene.getWidth());
        settings.setHeight(pnlScene.getHeight());

        app.setPauseOnLostFocus(false);
        app.setSettings(settings);
        app.createCanvas();

        JmeCanvasContext context = (JmeCanvasContext) app.getContext();
        Canvas canvas = context.getCanvas();

        context.setSystemListener(app);
        canvas.setPreferredSize(new Dimension(640, 480));
        app.startCanvas();

        pnlScene.setLayout(new BorderLayout());
        pnlScene.add(canvas, BorderLayout.CENTER);
    }
    
    public void setMessageFocus() {
        this.cboElements.requestFocusInWindow();
    }

    public void setMessage(String msg) {
        this.txtMsg.setText(msg);
    }

    public Spatial getCurrentElement() {
        LabeledValue<Spatial> element = (LabeledValue<Spatial>) this.cboElements.getSelectedItem();
        if (element == null) {
            return null;
        }
        return element.getValue();
    }

    public void addElement(String name, Spatial element) {
        this.cboElements.addItem(new LabeledValueImpl<>(name, element));
    }

    public SimpleApplication getApplication() {
        return this.app;
    }

    public void onAnalog(String name, float value, float tpf) {
        Spatial element = this.getCurrentElement();
        Vector3f pos = element.getLocalTranslation();
        if (name.equals("moveForward")) {
            pos.addLocal(0.3f, 0, 0);
            element.setLocalTranslation(pos);
        }
        if (name.equals("moveBackward")) {
            pos.addLocal(-0.3f, 0, 0);
            element.setLocalTranslation(pos);
        }
        if (name.equals("moveRight")) {
            pos.addLocal(0, 0, 0.3f);
            element.setLocalTranslation(pos);
        }
        if (name.equals("moveLeft")) {
            pos.addLocal(0, 0, -0.3f);
            element.setLocalTranslation(pos);
        }
        if (name.equals("moveUp")) {
            pos.addLocal(0, 0.3f, 0);
            element.setLocalTranslation(pos);
        }
        if (name.equals("moveDown")) {
            pos.addLocal(0, -0.3f, 0);
            element.setLocalTranslation(pos);
        }

        this.setMessage(
                String.format("X:%f, Y:%f, Z:%f",
                        element.getLocalTranslation().getX(),
                        element.getLocalTranslation().getY(),
                        element.getLocalTranslation().getZ()
                )
        );
    }
    
    public void onAnalog0(String name, float value, float tpf) {
        Spatial element = this.getCurrentElement();
        this.direction.set(this.app.getCamera().getDirection()).normalizeLocal();
        if (name.equals("moveForward")) {
            direction.multLocal(5 * tpf);
            element.move(direction);
        }
        if (name.equals("moveBackward")) {
            direction.multLocal(-5 * tpf);
            element.move(direction);
        }
        if (name.equals("moveRight")) {
            direction.crossLocal(Vector3f.UNIT_Y).multLocal(5 * tpf);
            element.move(direction);
        }
        if (name.equals("moveLeft")) {
            direction.crossLocal(Vector3f.UNIT_Y).multLocal(-5 * tpf);
            element.move(direction);
        }
        if (name.equals("moveUp")) {
            direction.crossLocal(Vector3f.UNIT_Z).multLocal(-5 * tpf);
            element.move(direction);
        }
        if (name.equals("moveDown")) {
            direction.crossLocal(Vector3f.UNIT_Z).multLocal(5 * tpf);
            element.move(direction);
        }

        this.setMessage(
                String.format("X:%f, Y:%f, Z:%f",
                        element.getLocalTranslation().getX(),
                        element.getLocalTranslation().getY(),
                        element.getLocalTranslation().getZ()
                )
        );
    }
    
    private void doLostFocus() {
        this.setMessageFocus();
    }
}
