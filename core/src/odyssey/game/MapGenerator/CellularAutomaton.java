package odyssey.game.MapGenerator;

import java.io.FileWriter;
import java.io.IOException;

public class CellularAutomaton {

    public CellularAutomaton(String TypeOfGenerator) {
        if(TypeOfGenerator.equals("Cave")) {
            GenerateCave();
        }
    }

    private int width;
    private int height;
    private static int[][] map;

    public CellularAutomaton(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new int[width][height];
    }

    // Initialize the map with random values
    public void initializeMap() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Set initial state (you may use random values)
                map[i][j] = Math.random() < 0.42 ? 0 : 1;
            }
        }
    }

    // Run simulation steps (apply cellular automaton rules)
    public void runSimulation(int steps) {
        for (int step = 0; step < steps; step++) {
            int[][] newMap = new int[width][height];

            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < height - 1; j++) {
                    int neighbors = countAliveNeighbors(i, j);

                    // Apply cellular automaton rules
                    if (map[i][j] == 1) {
                        newMap[i][j] = (neighbors >= 4) ? 1 : 0;
                    } else {
                        newMap[i][j] = (neighbors >= 5) ? 1 : 0;
                    }
                }
            }

            map = newMap;
        }
    }

    private int countAliveNeighbors(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += map[x + i][y + j];
            }
        }
        count -= map[x][y];
        return count;
    }

    // Generate TMX format and save it to a file
    public void generateTMX(String filename, int tileZero, int tileOne, int tileOne01, int tileOne02, int tileZeroUP, int tileZeroDOWN, int tileZeroLEFT, int tileZeroRIGHT, int tileZeroTOPLEFT, int tileZeroTOPRIGHT, int tileZeroBOTTOMLEFT, int tileZeroBOTTOMRIGHT, int tileZeroInternalTOPLEFT, int tileZeroInternalTOPRIGHT, int tileZeroInternalBOTTOMLEFT, int tileZeroInternalBOTTOMRIGHT) {
        try (FileWriter writer = new FileWriter("assets/Maps/" + filename + ".tmx")) {
            // Write TMX file content
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<map version=\"1.10\" tiledversion=\"1.10.2\" orientation=\"orthogonal\" " +
                    "renderorder=\"right-down\" width=\"" + width + "\" height=\"" + height + "\" " +
                    "tilewidth=\"32\" tileheight=\"32\" infinite=\"0\" nextlayerid=\"4\" nextobjectid=\"11\">\n");

            // Write editor settings
            writer.write(" <editorsettings>\n");
            writer.write("  <export target=\"Maps/" + filename + ".tmx\" format=\"tmx\"/>\n");
            writer.write(" </editorsettings>\n");

            // Write tileset information
            writer.write(" <tileset firstgid=\"1\" source=\"TileSetOdyssey.tsx\"/>\n");

            // Write layer information
            writer.write(" <layer id=\"2\" name=\"BaseLayer\" width=\"" + width + "\" height=\"" + height + "\" locked=\"0\">\n");
            writer.write("  <data encoding=\"csv\">\n");

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    writer.write(tileOne + (i == width - 1 && j == height - 1 ? "" : ","));
                }
                writer.write("\n");
            }

            writer.write("  </data>\n");
            writer.write(" </layer>\n");

            // Write second layer information
            writer.write(" <layer id=\"1\" name=\"Walls\" width=\"" + width + "\" height=\"" + height + "\" locked=\"0\">");
            writer.write("  <data encoding=\"csv\">\n");

            // Second layer
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int tileValue = map[i][j];

                    // Base Map in Two Tiles
                    if(tileValue == 1) {
                        tileValue = tileOne;
                    } else {
                        tileValue = tileZero;
                    }

                    // External Borders
                    if(tileValue == tileOne && map[i + 1][j] == 1 && map[i][j + 1] == 1 && map[i - 1][j] == 0 && map[i][j - 1] == 0) {
                        tileValue = tileZeroTOPLEFT;
                    }
                    if(tileValue == tileOne && map[i + 1][j] == 0 && map[i][j + 1] == 1 && map[i - 1][j] == 1 && map[i][j - 1] == 0) {
                        tileValue = tileZeroTOPRIGHT;
                    }
                    if(tileValue == tileOne && map[i + 1][j] == 1 && map[i][j + 1] == 0 && map[i - 1][j] == 0 && map[i][j - 1] == 1) {
                        tileValue = tileZeroBOTTOMLEFT;
                    }
                    if(tileValue == tileOne && map[i + 1][j] == 0 && map[i][j + 1] == 0 && map[i - 1][j] == 1 && map[i][j - 1] == 1) {
                        tileValue = tileZeroBOTTOMRIGHT;
                    }

                    // Internal Borders
                    if(tileValue == tileOne && map[i + 1][j] == 1 && map[i][j + 1] == 1 && map[i - 1][j] == 1 && map[i][j - 1] == 1) {
                        if(map[i + 1][j + 1] == 0) {
                            tileValue = tileZeroInternalTOPLEFT;
                        }

                        if(map[i - 1][j + 1] == 0) {
                            tileValue = tileZeroInternalTOPRIGHT;
                        }

                        if(map[i + 1][j - 1] == 0) {
                            tileValue = tileZeroInternalBOTTOMLEFT;
                        }

                        if(map[i - 1][j - 1] == 0) {
                            tileValue = tileZeroInternalBOTTOMRIGHT;
                        }
                    }

                    // Cardinal sizes
                    if(tileValue == tileOne && map[i][j + 1] == 0) {
                        tileValue = tileZeroUP;
                    }
                    if(tileValue == tileOne && map[i][j - 1] == 0) {
                        tileValue = tileZeroDOWN;
                    }
                    if(tileValue == tileOne && map[i + 1][j] == 0) {
                        tileValue = tileZeroLEFT;
                    }
                    if(tileValue == tileOne && map[i - 1][j] == 0) {
                        tileValue = tileZeroRIGHT;
                    }

                    // TileOneChange
                    if(tileValue == tileOne) {
                        tileValue = Math.random() < 1.0 / 3.0 ? tileOne01 : Math.random() < 2.0 / 3.0 ? tileOne02 : tileOne;
                    }

                    writer.write(tileValue + (i == width - 1 && j == height - 1 ? "" : ","));
                }
                writer.write("\n");
            }

            writer.write("  </data>\n");
            writer.write(" </layer>\n");

            // Close the map tag
            writer.write("</map>\n");

            System.out.println("TMX file generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateCave() {
        int width = 100;
        int height = 80;
        int simulationSteps = 10;

        int tileZero = 1137;
        int tileOne = 1206;
        int tileOne01 = 1207;
        int tileOne02 = 1208;
        int tileZeroUP = 1105;
        int tileZeroDOWN = 1169;
        int tileZeroLEFT = 1136;
        int tileZeroRIGHT = 1138;
        int tileZeroTOPLEFT = 1041;
        int tileZeroTOPRIGHT = 1042;
        int tileZeroBOTTOMLEFT = 1073;
        int tileZeroBOTTOMRIGHT = 1074;
        int tileZeroInternalTOPLEFT = 1104;
        int tileZeroInternalTOPRIGHT = 1106;
        int tileZeroInternalBOTTOMLEFT = 1168;
        int tileZeroInternalBOTTOMRIGHT = 1170;

        CellularAutomaton cellularAutomaton = new CellularAutomaton(width, height);
        cellularAutomaton.initializeMap();
        cellularAutomaton.runSimulation(simulationSteps);
        cellularAutomaton.generateTMX("GeneratedMap", tileZero, tileOne, tileOne01, tileOne02, tileZeroUP, tileZeroDOWN, tileZeroLEFT, tileZeroRIGHT, tileZeroTOPLEFT, tileZeroTOPRIGHT, tileZeroBOTTOMLEFT, tileZeroBOTTOMRIGHT, tileZeroInternalTOPLEFT, tileZeroInternalTOPRIGHT, tileZeroInternalBOTTOMLEFT, tileZeroInternalBOTTOMRIGHT);
    }

    public static void main(String[] args) {
        int width = 100; // 30
        int height = 80; // 20
        int simulationSteps = 10; // 10
//        int tileZero = 1134; // 1358
//        int tileOne = 1203; // 1125
//        int tileZeroUP = 1102; // 1326
//        int tileZeroDOWN = 1166; // 1390
//        int tileZeroLEFT = 1133; // 1357
//        int tileZeroRIGHT = 1135; // 1359
//        int tileZeroTOPLEFT = 1038; // 1262
//        int tileZeroTOPRIGHT = 1039; // 1263
//        int tileZeroBOTTOMLEFT = 1070; // 1294
//        int tileZeroBOTTOMRIGHT = 1071; // 1295
//        int tileZeroInternalTOPLEFT = 1101; // 1325
//        int tileZeroInternalTOPRIGHT = 1103; // 1327
//        int tileZeroInternalBOTTOMLEFT = 1165; // 1389
//        int tileZeroInternalBOTTOMRIGHT = 1167; // 1391
        int tileZero = 1137; // 1358
        int tileOne = 1206; // 1125
        int tileOne01 = 1207;
        int tileOne02 = 1208;
        int tileZeroUP = 1105; // 1326
        int tileZeroDOWN = 1169; // 1390
        int tileZeroLEFT = 1136; // 1357
        int tileZeroRIGHT = 1138; // 1359
        int tileZeroTOPLEFT = 1041; // 1262
        int tileZeroTOPRIGHT = 1042; // 1263
        int tileZeroBOTTOMLEFT = 1073; // 1294
        int tileZeroBOTTOMRIGHT = 1074; // 1295
        int tileZeroInternalTOPLEFT = 1104; // 1325
        int tileZeroInternalTOPRIGHT = 1106; // 1327
        int tileZeroInternalBOTTOMLEFT = 1168; // 1389
        int tileZeroInternalBOTTOMRIGHT = 1170; // 1391

        CellularAutomaton cellularAutomaton = new CellularAutomaton(width, height);
        cellularAutomaton.initializeMap();
        cellularAutomaton.runSimulation(simulationSteps);
        cellularAutomaton.generateTMX("GeneratedMap", tileZero, tileOne, tileOne01, tileOne02, tileZeroUP, tileZeroDOWN, tileZeroLEFT, tileZeroRIGHT, tileZeroTOPLEFT, tileZeroTOPRIGHT, tileZeroBOTTOMLEFT, tileZeroBOTTOMRIGHT, tileZeroInternalTOPLEFT, tileZeroInternalTOPRIGHT, tileZeroInternalBOTTOMLEFT, tileZeroInternalBOTTOMRIGHT);
    }
}
