package com.goumo.ingametips.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.goumo.ingametips.IngameTips;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class UnlockedTipManager {
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<String> visible;
    private List<String> hide;
    private List<List<String>> custom;

    public static final UnlockedTipManager manager = new UnlockedTipManager();
    public static String error = "";

    static {
        if (IngameTips.TIPS.mkdir()) {
            LOGGER.info("Config path created");
        }
        manager.loadFromFile();
    }

    private UnlockedTipManager() {
        reset();
    }

    public void loadFromFile() {
        if (!IngameTips.UNLCOKED_FILE.exists()) {
            createFile();
            return;
        }

        LOGGER.debug("Loading unlocked tips");
        try (FileReader reader = new FileReader(IngameTips.UNLCOKED_FILE)) {
            UnlockedTipManager fileManager = GSON.fromJson(reader, UnlockedTipManager.class);
            this.visible = fileManager.visible;
            this.hide = fileManager.hide;
            this.custom = fileManager.custom;

        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            error = "load";
            LOGGER.error("Unable to load file: '{}'", IngameTips.UNLCOKED_FILE);
            createFile();
        }
    }

    public void saveToFile() {
        try (FileWriter writer = new FileWriter(IngameTips.UNLCOKED_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
            error = "save";
            LOGGER.error("Unable to save file: '{}'", IngameTips.UNLCOKED_FILE);
        }
    }

    public void createFile() {
        if (IngameTips.UNLCOKED_FILE.exists()) {
            File backupFile = new File(IngameTips.UNLCOKED_FILE + ".bak");
            try {
                Files.copy(IngameTips.UNLCOKED_FILE.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                LOGGER.warn("Old file has been saved as '{}'", backupFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LOGGER.debug("Creating file: '{}'", IngameTips.UNLCOKED_FILE);
        try (FileWriter writer = new FileWriter(IngameTips.UNLCOKED_FILE)) {
            reset();
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getVisible() {
        return visible;
    }

    public List<String> getHide() {
        return hide;
    }

    public List<List<String>> getCustom() {
        return custom;
    }

    public void unlock(String ID, boolean hide) {
        if (isUnlocked(ID)) return;
        if (hide) {
            this.hide.add(ID);
        } else {
            this.visible.add(ID);
        }
        saveToFile();
    }

    public void unlockCustom(TipElement ele) {
        if (isUnlocked(ele.ID)) return;
        List<String> custom = new ArrayList<>();

        custom.add(ele.ID);
        custom.add(Integer.toString(ele.visibleTime));
        custom.add(ele.contents.get(0).getString());
        for (int i = 1; i < ele.contents.size(); i++) {
            custom.add(ele.contents.get(i).getString());
        }

        this.custom.add(custom);
        saveToFile();
    }

    public void removeUnlocked(String ID) {
        this.visible.remove(ID);
        this.hide.remove(ID);
        this.custom.removeIf((l) -> l.get(0).equals(ID));
        saveToFile();
    }

    public boolean isUnlocked(String ID) {
        if (visible.contains(ID) || hide.contains(ID)) {
            return true;
        }

        return custom.stream().anyMatch(l -> l.get(0).equals(ID));
    }

    public void reset() {
        this.visible = new ArrayList<>();
        this.hide = new ArrayList<>();
        this.custom = new ArrayList<>();
    }
}
