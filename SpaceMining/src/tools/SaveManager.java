package tools;

import entities.Ressource;
import entities.Tool;
import game.GameState;

import java.io.*;
import java.util.List;
import java.util.Map;

public class SaveManager {

    private static final String SAVE_FILE = "savegame.json";

    public static void saveGame() {
        GameState state = GameState.getCurrentState();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");

            // Credits
            json.append("\"credits\":").append(state.credits).append(",\n");

            // Interior style
            json.append("\"interiorStyle\":\"").append(state.interiorStyle.name()).append("\",\n");

            // Ship type
            json.append("\"shipType\":\"").append(state.ship.getClass().getSimpleName()).append("\",\n");

            // Ship upgrade level
            json.append("\"shipUpgradeLevel\":").append(state.ship.getUpgradeLevel()).append(",\n");

            // --- Save inventory ---
            Map<Ressource, Integer> inventory = state.ship.getInventory();
            json.append("\"shipInventory\":{\n");
            int invCount = 0;
            for (Map.Entry<Ressource, Integer> entry : inventory.entrySet()) {
                json.append("\"").append(entry.getKey().getName()).append("\":").append(entry.getValue());
                invCount++;
                if (invCount < inventory.size()) json.append(",");
                json.append("\n");
            }
            json.append("},\n");

            // --- Save tools ---
            List<Tool> tools = state.ship.getTools();
            json.append("\"shipTools\":{\n");
            int toolCount = 0;
            for (Tool tool : tools) {
                json.append("\"").append(tool.getName()).append("\":").append(tool.getLevel());
                toolCount++;
                if (toolCount < tools.size()) json.append(",");
                json.append("\n");
            }
            json.append("},\n");

            // Key bindings
            json.append("\"keyBindings\":{\n");
            int count = 0;
            for (Map.Entry<String, javafx.scene.input.KeyCode> entry : state.keyBindings.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().getName()).append("\"");
                count++;
                if (count < state.keyBindings.size()) json.append(",");
                json.append("\n");
            }
            json.append("}\n");


            json.append("}");
            writer.write(json.toString());

            System.out.println("Game saved successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("Aucune sauvegarde trouvée.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            String data = json.toString();

            GameState current = GameState.getCurrentState();

            // Credits
            String creditsStr = data.replaceAll(".*\"credits\":([0-9\\.]+).*", "$1");
            current.credits = Double.parseDouble(creditsStr);

            // Interior style
            String styleStr = data.replaceAll(".*\"interiorStyle\":\"([A-Z]+)\".*", "$1");
            current.interiorStyle = GameState.ShipInteriorStyle.valueOf(styleStr);

            // Ship type
            String shipStr = data.replaceAll(".*\"shipType\":\"([A-Za-z_]+)\".*", "$1");
            current.loadShip(shipStr.replace("Ship_", "").toLowerCase());

            // Ship upgrade level
            String upgradeStr = data.replaceAll(".*\"shipUpgradeLevel\":([0-9]+).*", "$1");
            current.ship.setUpgradeLevel(Integer.parseInt(upgradeStr));

            // --- Load inventory ---
            String invStr = data.replaceAll(".*\"shipInventory\":\\{(.*?)\\}.*", "$1");
            current.ship.getInventory().clear();
            if (!invStr.isEmpty()) {
                String[] entries = invStr.split(",");
                for (String e : entries) {
                    String[] pair = e.split(":");
                    if (pair.length == 2) {
                        String name = pair[0].replaceAll("\"", "").trim();
                        int value = Integer.parseInt(pair[1].trim());
                        for (Ressource r : current.mineralList) {
                            if (r.getName().equals(name)) {
                                System.out.println(r + " x" + value);
                                current.ship.addRessource(r, value);
                                break;
                            }
                        }
                    }
                }
            }

            // --- Load tools ---
            String toolsStr = data.replaceAll("(?s).*?\"shipTools\":\\{(.*?)\\}.*", "$1").trim();

            if (!toolsStr.isEmpty()) {
                String[] entries = toolsStr.split(",");

                for (String e : entries) {
                    String[] pair = e.split(":");
                    if (pair.length == 2) {
                        String name = pair[0].replaceAll("\"", "").trim();
                        int level = Integer.parseInt(pair[1].trim());
                        System.out.println(name+ " x " + level);
                        for (Tool t : Tool.getTools()) {
                            System.out.println(name+ " x " + t.getName());
                            if (t.getName().equals(name)) {
                                t.setLevel(level);
                                current.ship.addTool(t);
                                break;
                            }
                        }
                    }
                }
            }



            // Key bindings
            for (Map.Entry<String, javafx.scene.input.KeyCode> entry : current.keyBindings.entrySet()) {
                String regex = "\"" + entry.getKey() + "\":\"([A-Z]+)\"";
                String keyStr = data.replaceAll(".*" + regex + ".*", "$1");
                try {
                    entry.setValue(javafx.scene.input.KeyCode.getKeyCode(keyStr));
                } catch (IllegalArgumentException ignored) {}
            }

            System.out.println("Game loaded successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
