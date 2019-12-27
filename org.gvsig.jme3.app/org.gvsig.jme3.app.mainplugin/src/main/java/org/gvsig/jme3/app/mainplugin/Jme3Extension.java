/**
 * gvSIG. Desktop Geographic Information System.
 *
 * Copyright (C) 2007-2013 gvSIG Association.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * For any additional information, do not hesitate to contact us
 * at info AT gvsig.com, or visit our website www.gvsig.com.
 */
package org.gvsig.jme3.app.mainplugin;

import org.apache.commons.lang3.StringUtils;
import org.gvsig.andami.IconThemeHelper;
import org.gvsig.andami.plugins.Extension;
import org.gvsig.tools.swing.api.ToolsSwingLocator;
import org.gvsig.tools.swing.api.windowmanager.WindowManager;

/**
 *
 * @author jjdelcerro
 */
public class Jme3Extension 
        extends Extension 
    {

    @Override
    public void initialize() {
        IconThemeHelper.registerIcon("action", "tools-jme3-test1", this);

    }

    @Override
    public void postInitialize() {
    }

    @Override
    public void execute(String action) {
        if (StringUtils.equalsIgnoreCase(action, "tools-jme3-test1")) {
            ViewerJme3 viewer = new ViewerJme3();
            viewer.setSceneMaker(new SceneTerrain());
            WindowManager windowsManager = ToolsSwingLocator.getWindowManager();
            windowsManager.showWindow(
                    viewer.asJComponent(),
                    "JMonkey 3",
                    WindowManager.MODE.WINDOW
            );
        }
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }
    
}
