/* Save3DDialog.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright Ⓒ 2015 Universiteit Gent
 * 
 * This file is part of the Equi application
 * 
 * Corresponding author (see also file AUTHORS)
 * 
 * Kris Coolsaet
 * Department of Applied Mathematics, Computer Science and Statistics
 * Ghent University 
 * Krijgslaan 281-S9
 * B-9000 GENT Belgium
 * 
 * The Equi application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Equi Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with the Equi Application (file LICENSE in the distribution).  If not,
 * see http://www.gnu.org/licenses/.
 */

package be.ugent.caagt.equi.gui;

import be.ugent.caagt.equi.EmbeddedPlanarGraph;
import be.ugent.caagt.equi.io.GraphOutputStream;
import be.ugent.caagt.equi.io.SpinputOutputStream;
import be.ugent.caagt.equi.io.WriteGraphOutputStream;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Provides a dialog for saving/reading a planar graph to a writegraph3D or a spinput file
 */
public class Save3DDialog {

    private FileChooser chooser = null;

    private File lastFile = null;

    private Window owner;

    public Save3DDialog(Window owner) {
        this.owner = owner;
    }

    public enum OutputType {
        WRITE_GRAPH("WriteGraph3D files", "*.w3d"),
        SPINPUT("Spinput files", "*.spinput");

        private String caption;

        private String wildcard;

        OutputType(String caption, String wildcard) {
            this.caption = caption;
            this.wildcard = wildcard;
        }

        public FileChooser.ExtensionFilter getFilter() {
            return new FileChooser.ExtensionFilter(caption, wildcard);
        }
    }

    /**
     * Lazily retrieves the associated file chooser
     */
    public FileChooser getChooser() {
        if (chooser == null) {
            chooser = new FileChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        return chooser;
    }

    public void save(EmbeddedPlanarGraph graph, OutputType type) {
        FileChooser chooser = getChooser();
        chooser.getExtensionFilters().clear();
        chooser.getExtensionFilters().add(type.getFilter());
        if (lastFile != null) {
            chooser.setInitialDirectory(lastFile.getParentFile());
        }
        File file = chooser.showSaveDialog(owner);
        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file);
                 GraphOutputStream gos =
                         type == OutputType.WRITE_GRAPH ? new WriteGraphOutputStream(out, 3)
                                 : new SpinputOutputStream(out)
            ) {
                gos.writeGraph(graph);
                lastFile = file;
            } catch (IOException e) {
                // should signal error
            }
        }
    }
}
