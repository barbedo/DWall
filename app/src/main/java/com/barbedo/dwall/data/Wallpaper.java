/**
 * Copyright 2016 Ricardo Barbedo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.barbedo.dwall.data;

/**
 * Representation of the Wallpaper data.
 *
 * @author Ricardo Barbedo
 */
public class Wallpaper {
    int position;
    String name;
    String mode;
    String info;
    String filename;

    /**
     * Default constructor with empty fields and position 0.
     */
    public Wallpaper() {
        this.position = 0;
        this.name = "";
        this.mode = "";
        this.info = "";
        this.filename = "";
    };

    /**
     * Constructor with the specified members.
     *
     * @param position Position on the list and on the database.
     * @param name     Name selected by the user.
     * @param mode     Mode selected by the user.
     * @param info     Information related to the selected mode.
     * @param filename Name of the corresponding wallpaper saved in the internal storage.
     */
    public Wallpaper(int position, String name, String mode, String info, String filename) {
        this.position = position;
        this.name = name;
        this.mode = mode;
        this.info = info;
        this.filename = filename;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public String getInfo() {
        return info;
    }

    public String getFilename() {
        return filename;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void decrementPosition() {
        position--;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String toString() {
        return position + " " + name + " " + mode + " " + info + " " + filename;
    }

}
