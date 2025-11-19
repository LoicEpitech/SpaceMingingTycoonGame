package tools;

import entities.Mineral;
import game.GameState;
import screens.GameScene;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

public class SaveManager {

    private static final String SAVE_FILE = "save.json";

    public static void save(GameState state) {
        String json =
                "{\n" +
                        "  \"credits\": " + state.credits + ",\n" +
                        "  \"ship\": \"" + state.ship.getType() + "\",\n" +
                        "  \"minerals\": {\n" +
                        "\"gold\": " + state.storage.getOrDefault(Mineral.GOLD, 0) + ",\n" +
                        "\"diamond\": " + state.storage.getOrDefault(Mineral.DIAMOND, 0) + ",\n" +
                        "\"orichalcum\": " + state.storage.getOrDefault(Mineral.ORICHALCUM, 0) + "\n"+
                        "  }\n" +
                        "}";

        try (FileWriter fw = new FileWriter(SAVE_FILE)) {
            fw.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGame() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return null;

        try {
            String json = Files.readString(f.toPath());
            GameState state = new GameState();

            state.credits = extractInt(json, "\"credits\":");
            String shipType = extractString(json, "\"ship\":");

            int gold = extractInt(json, "\"gold\":");
            int diamond = extractInt(json, "\"diamond\":");
            int orichalcum = extractInt(json, "\"orichalcum\":");

          //  state.storage.put(Mineral.GOLD, gold);
            //state.storage.put(Mineral.DIAMOND, diamond);
            //state.storage.put(Mineral.ORICHALCUM, orichalcum);

            state.loadShip(shipType);

            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int extractInt(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return 0;
        idx += key.length();
        String num = json.substring(idx).replaceAll("[^0-9-]", "");
        return Integer.parseInt(num);
    }

    private static String extractString(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return "speed";
        idx += key.length();
        int firstQuote = json.indexOf("\"", idx);
        int secondQuote = json.indexOf("\"", firstQuote + 1);
        return json.substring(firstQuote + 1, secondQuote);
    }

    public static void loadGameInto(GameScene game) {
        GameState loaded = loadGame();
        if (loaded != null) game.setState(loaded);
    }

    public static String loadShipType() {
        GameState state = loadGame();
        if (state == null || state.ship == null) return "speed";
        return state.ship.getType();
    }
}
