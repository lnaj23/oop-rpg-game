package repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import configuration.AppConfig;
import exceptions.LoadingAddedBugemonFailedException;
import exceptions.LoadingTeamFailedException;
import exceptions.SavingAddedBugemonFailedException;
import exceptions.SavingTeamFailedException;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class AddedBugemonRepository {

    private static AddedBugemonRepository instance;

    private static final Gson SAVE_GSON = new GsonBuilder().setPrettyPrinting().create();

    private static class JsonAddedBugemon {
        List<Bugemon> addedBugemons;
    }

    public static AddedBugemonRepository getInstance() throws IOException {
        if (instance == null) instance = new AddedBugemonRepository();
        return instance;
    }

    public List<Bugemon> loadAddedBugemon() throws LoadingAddedBugemonFailedException {
        File file = new File(AppConfig.ADDEDBUGEMON_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            JsonAddedBugemon wrapper = new Gson().fromJson(reader, JsonAddedBugemon.class);
            if (wrapper == null || wrapper.addedBugemons == null) return new ArrayList<>();
            return wrapper.addedBugemons;
        } catch (IOException e) {
            throw new LoadingAddedBugemonFailedException("Error loading Added Bugemon: " + e.getMessage(), e);
        }
    }

    public void saveAddedBugemons(Bugemon bugemonToSave) throws SavingAddedBugemonFailedException, LoadingAddedBugemonFailedException, IOException {
        File file = new File(AppConfig.ADDEDBUGEMON_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        List<Bugemon> addedBugemons = loadAddedBugemon();
        addedBugemons.add(bugemonToSave);
        JsonAddedBugemon wrapper = new JsonAddedBugemon();
        wrapper.addedBugemons = addedBugemons;
        try (Writer writer = new FileWriter(file)) {
            SAVE_GSON.toJson(wrapper, writer);
        } catch (IOException e) {
            throw new SavingAddedBugemonFailedException("Error saving teams: " + e.getMessage(), e);
        }
        System.out.println("Saved " + addedBugemons.size() + " bugemons to file");
    }

    public void saveSprite(File selectedFile) throws IOException {
        Path source = selectedFile.toPath();
        Path destination = Path.of(AppConfig.PNG_PREFIX_ADDEDBUEMON, selectedFile.getName());
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

}
