package screens;

import entities.*;

import game.GameState;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import ships.Ship_Discount;
import ships.Ship_Speed;
import ships.Ship_Stock;

import ui.*;
import world.ZoneView;
import world.planet.Planet;
import world.Wall;
import world.Zone;

import java.util.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.InteractiveZone;
import world.asset.DecorData;
import javafx.scene.shape.Rectangle;

import static game.GameState.currentState;

import utils.InteractiveZone;
import world.asset.DecorData;
import javafx.scene.shape.Rectangle;

import static game.GameState.currentState;
import ui.HUD;
public class GameScene {
    //region --- Champs ---
    private String message = "";
    private long messageStartTime = 0;
    private final long MESSAGE_DURATION = 2000; // durée en millisecondes (ici 2 secondes)

    // --- Constantes d’affichage ---
    private static final double VIRTUAL_WIDTH = 1920;
    private static final double VIRTUAL_HEIGHT = 1080;

    // --- Composants principaux ---
    private final Scene scene;
    private final Canvas canvas;
    private final GraphicsContext gc;

    public final GameState state = currentState;
    private final Player player;
    private final Stage primaryStage;

    // --- Entrées et états ---
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private Pane uiLayer;
    private SellMenu sellMenu;
    private TravelMenu travelMenu;
    private VideoFirstGround travelVideo;
    private VideoBackground backgroundVideo;
    private boolean traveling = false;
    private Planet targetPlanet;
    private float travelProgress = 0.0F;
    private double travelDuration = 2.0;

    // Game states
    private boolean mining = false;
    private float miningProgress = 0;
    private float miningDuration = 2f;

    // --- Ressources ---
    private Image shipTexture;
    private Image shipContourTexture;
    private Image playerUp;
    private Image playerDown;
    private Image playerLeft;
    private Image playerRight;

    // Inputs
    private boolean mouseLeftPressed = false;
    private boolean mouseRightPressed = false;
    private double mouseX, mouseY;
    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction playerDirection = Direction.DOWN;


    private AnimationTimer loop;
    private Point2D savedShipPosition = null;
    private AnimationTimer planetLoop;

    private boolean escHandled = false;
    private boolean f2Handled = false;
    private HUD hud = new HUD();
    //endregion
    //region --- Constructeur ---


    public GameScene(Stage stage, String shipType) {
        this.primaryStage = stage;

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        canvas = new Canvas(screenBounds.getWidth(), screenBounds.getHeight());
        gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        this.backgroundVideo = new VideoBackground(root, "Mercatora.mp4");
        this.backgroundVideo.play();

        root.getChildren().add(this.canvas);

        scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        player = new Player(960, 540);
        player.setInputs(keysPressed);

        loadShip(shipType);
        loadAssets();
        createZones();
        currentState.currentPlanet = this.targetPlanet;
        createPlanets();
        createTools();
        setupUI(root);
        setupInput();
        setupResizeListener();
        startGameLoop();

    }

    public GameScene(Stage stage, String shipType, Boolean Save) {
        this(stage,shipType);
        if (Save) {
            tools.SaveManager.loadGame();
        }
    }

        private void loadShip(String type) {
        switch (type.toLowerCase()) {
            case "stock" -> state.ship = new Ship_Stock();
            case "discount" -> state.ship = new Ship_Discount();
            default -> state.ship = new Ship_Speed();
        }
    }

    private void loadAssets() {
        switch (state.interiorStyle) {
            case CLASSIC ->
                    shipTexture = new Image("assets/ship/spaceship.jpg");
            case INDUSTRIAL ->
                    shipTexture = new Image("assets/ship/spaceship1.jpg");
            case FUTURISTIC ->
                    shipTexture = new Image("assets/ship/spaceship2.jpg");
        }
        shipContourTexture = new Image("assets/contourSpaceship.png");
        playerUp = new Image("assets/astronautTop.png");
        playerDown = new Image("assets/astronaut.png");
        playerLeft = new Image("assets/astronautLeft.png");
        playerRight = new Image("assets/astronautRight.png");

    }

    private void createZones() {
        new Zone("Hublot", 870.0, 490.0, 180.0, 70.0, Zone.ZoneType.HUBLOT); // haut millieu
        new Zone("Terminal", 1279.0, 620.0, 70.0, 111.0, Zone.ZoneType.PILOT);//droite millieu
        new Zone("Door", 850.0, 808.0, 192.0, 70.0, Zone.ZoneType.DOOR); // bas millieu
    }

    private void createPlanets() {
        List<Object> decor = List.of(
                new DecorData("/assets/space.png", 400.0, 185.0, 1100, 700, false),
                new DecorData("/assets/images/planet/Solid/Arid/Arid_02-512x512.png", 425.0, 550.0, 1050, 800, false)
        );

        List<InteractiveZone> interactiveZones = List.of(
                new InteractiveZone(
                        new Rectangle(1050, 520, 150, 150),
                        () -> currentState.currentPlanet.getResource().startMining(hud)
                )
        );

        currentState.currentPlanet = new Planet("Mercatora", 1, 0, Planet.MoonType.MARKET, Mineral.MARKET, "/assets/hubloView.png", decor, "/assets/music/GameOST.mp3", interactiveZones);
        new Planet("Aurea", 1, 3, Planet.MoonType.FARM, Mineral.GOLD, "/assets/hubloView.png", decor, "/assets/music/GameOST.mp3", interactiveZones);
        new Planet("Crysalon", 2, 2, Planet.MoonType.FARM, Mineral.DIAMOND, "/assets/hubloView.png", decor, "/assets/music/GameOST.mp3", interactiveZones);
        new Planet("Orichara", 3, 1, Planet.MoonType.FARM, Mineral.ORICHALCUM, "/assets/hubloView.png", decor, "/assets/music/GameOST.mp3", interactiveZones);
        System.out.println(currentState.currentPlanet.getName());
    }


    //endregion
    //region --- Setup ---
    private void createTools() {
        currentState.ship.addTool(new Drill());
        new Laser();
        new Plasma();

        System.out.println("Liste totale des tools = ");
        for (Tool t : Tool.getTools()) {
            System.out.println(" - " + t.getName());
        }
    }
    //endregion
    //region --- Setup ---
    private void setupInput() {
        scene.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));

        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        scene.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            System.out.println("Clic à : X = " + x + " | Y = " + y);
        });

        scene.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) mouseLeftPressed = true;
            if (e.getButton() == MouseButton.SECONDARY) mouseRightPressed = true;
        });

        scene.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.PRIMARY) mouseLeftPressed = false;
            if (e.getButton() == MouseButton.SECONDARY) mouseRightPressed = false;
        });
    }

    private void setupResizeListener() {
        scene.widthProperty().addListener((obs, oldW, newW) -> canvas.setWidth(newW.doubleValue()));
        scene.heightProperty().addListener((obs, oldH, newH) -> canvas.setHeight(newH.doubleValue()));
    }

    private void setupUI(StackPane root) {
        this.uiLayer = new Pane();
        this.uiLayer.setPickOnBounds(false);

        this.sellMenu = new SellMenu(this.state.ship, state, new UpgradeMenu(state));
        this.sellMenu.setLayoutX(760);
        this.sellMenu.setLayoutY(300);
        this.sellMenu.setVisible(false);
        this.uiLayer.getChildren().add(this.sellMenu);

        this.travelMenu = new TravelMenu(this);
        this.travelMenu.setLayoutX(760);
        this.travelMenu.setLayoutY(200);
        this.travelMenu.setVisible(false);
        this.uiLayer.getChildren().add(this.travelMenu);

        root.getChildren().add(this.uiLayer);
    }
    private void updateBackgroundForPlanet(Planet planet) {

        String bgVideo = planet.getName() + ".mp4";
        if (this.travelVideo != null) {
            this.travelVideo.stop();
            this.travelVideo = null;
        }
        state.currentPlanet = planet;

        this.backgroundVideo.stop();
        this.backgroundVideo = new VideoBackground((StackPane) this.scene.getRoot(), bgVideo);
        this.backgroundVideo.play();
    }
    //endregion

    //region --- Boucle Principale ---

    private void update(double dt) {
        if (keysPressed.contains(KeyCode.F2) && !f2Handled) {
            f2Handled = true;
            tools.SaveManager.saveGame();
            Platform.runLater(() -> showMessage("Partie sauvegardée !"));
        } else if (!keysPressed.contains(KeyCode.F2)) {
            f2Handled = false;
        }

        if (keysPressed.contains(KeyCode.ESCAPE) && !escHandled) {
            tools.SaveManager.saveGame();
            escHandled = true;
            goToMainMenu();
            return;
        } else if (!keysPressed.contains(KeyCode.ESCAPE)) escHandled = false;
        if (keysPressed.contains(KeyCode.Z)) {
            playerDirection = Direction.UP;
        }
        if (keysPressed.contains(KeyCode.S)) {
            playerDirection = Direction.DOWN;
        }
        if (keysPressed.contains(KeyCode.Q)) {
            playerDirection = Direction.LEFT;
        }
        if (keysPressed.contains(KeyCode.D)) {
            playerDirection = Direction.RIGHT;
        }
        player.move(dt);

        Zone.checkPlayerHoverZone(player.getPosition().getX(), player.getPosition().getY()); // Verif hover zone \\

        if (keysPressed.contains(KeyCode.E) && ZoneView.getHoveredZone() != null) {
            switch (ZoneView.getHoveredZone().getType()) {
                case PILOT -> MenuTravel();
                case HUBLOT -> {
                    if (currentState.currentPlanet == null) {
                        System.out.println("Aucune planète actuelle définie. [LOG DEBUG HUBLOT OPEN]");
                        showMessage("Aucune planète actuelle définie.");
                        break;
                    }
                    if (currentState.currentPlanet.getMoonType() != Planet.MoonType.FARM) {
                        System.out.println("La planet doit etre de type FARM pour pouvoir accedé au hublo valeur actuel (" + currentState.currentPlanet.getMoonType() + "). [LOG DEBUG HUBLOT OPEN]");
                        showMessage("La planet doit etre de type FARM pour pouvoir accedé au hublo valeur actuel (" + currentState.currentPlanet.getMoonType() + ").");
                        break;
                    }

                    currentState.currentPlanet.loadAssets();
                    currentState.currentPlanet.setOnEnterPlanet(() -> {
                        primaryStage.setScene(currentState.currentPlanet.getPlanetScene());
                        primaryStage.setFullScreen(true);
                        primaryStage.setFullScreenExitHint("");
                        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                    });

                    currentState.currentPlanet.setOnExitPlanet(() -> {
                        primaryStage.setScene(GameScene.this.getScene());
                        primaryStage.setFullScreen(true);
                        primaryStage.setFullScreenExitHint("");
                        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                        if (savedShipPosition != null)
                            player.setPosition(savedShipPosition.getX(), savedShipPosition.getY());
                        if (loop != null) loop.start();
                    });

                    if (loop != null) loop.stop();
                    currentState.currentPlanet.enterPlanetView();
                }
                case DOOR -> {
                    if (currentState.currentPlanet == null) {
                        System.out.println("Aucune planète actuelle définie. [LOG DEBUG HUBLOT OPEN]");
                        showMessage("Aucune planète actuelle définie.");
                        break;
                    }
                    if (currentState.currentPlanet.getMoonType() != Planet.MoonType.MARKET) {
                        System.out.println("La planet doit etre de type MARKET pour pouvoir accedé au hublo valeur actuel (" + currentState.currentPlanet.getMoonType() + "). [LOG DEBUG MARKET OPEN]");
                        showMessage("La planet doit etre de type MARKET pour pouvoir accedé au hublo valeur actuel (" + currentState.currentPlanet.getMoonType() + ").");
                        break;
                    }
                    visitPlanet(currentState.currentPlanet);

                }
            }
            keysPressed.remove(KeyCode.E);
        }

        if (this.traveling) {

            this.travelDuration = (double) this.targetPlanet.getTravelTime()*(1.0 + (1.0 - currentState.ship.getSpeed())); // Ajuste la durée en fonction de la vitesse du vaisseau
            this.travelProgress += (float) (dt / this.travelDuration);
            if (this.travelProgress >= 1.0F) {
                this.traveling = false;
                this.travelProgress = 1.0F;
                if (this.travelVideo != null) {
                    this.travelVideo.stop();
                    this.travelVideo.removeFromParent((StackPane) this.scene.getRoot());
                    this.travelVideo = null;
                }

                this.updateBackgroundForPlanet(state.currentPlanet);
            }
        }

        // on peut faire un rapelle du minage ici au besoin
    }

    private void startGameLoop() {
        loop = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (last > 0) {
                    double dt = (now - last) / 1e9;
                    update(dt);
                    render();
                }
                last = now;
            }
        };
        loop.start();
    }
//endregion


    //endregion
    //region --- Travel ---
    private void MenuTravel() {
        this.travelMenu.refresh(this);
        this.travelMenu.show();
    }

    public void startTravelTo(Planet planet) {
        state.currentPlanet = planet;
        this.traveling = true;
        this.targetPlanet = planet;
        this.travelProgress = 0.0F;
        this.travelDuration = (double) planet.getTravelTime();
        this.travelVideo = new VideoFirstGround((StackPane) this.scene.getRoot(), "travel.mp4", false);
    }
    private void visitPlanet(Planet planet) {
        // Sauvegarde la position du vaisseau avant de quitter
        savedShipPosition = player.getPosition();

        // Stop la boucle du vaisseau
        if (loop != null) loop.stop();

        // Charge les assets de la planète si pas déjà fait
        if (planet.getPlanetScene() == null) {
            List<Object> decor = List.of(
                    new DecorData("/assets/Planet1.png", 400, 250, 280, 280),
                    new DecorData("/assets/Planet2.png", 950, 500, 250, 250)
            );
            planet.loadAssets("/assets/spaceMarcket.jpg", decor, "/assets/music/GameOST.mp3");

        }

        // Def les actions d entre et de sortie
        planet.setOnEnterPlanet(() -> {
            primaryStage.setScene(planet.getPlanetScene());
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        });

        planet.setOnExitPlanet(() -> {
            primaryStage.setScene(GameScene.this.scene);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            if (savedShipPosition != null) player.setPosition(savedShipPosition.getX(), savedShipPosition.getY());
            if (loop != null) loop.start();
        });

        planet.enterPlanetView(state);
    }
//endregion

    //region --- Graphique ---
    private void showMessage(String text) {
        //message = text;
        //messageStartTime = System.currentTimeMillis();
        hud.addNotification(text);
    }
    private void render() {
        double scaleX = canvas.getWidth() / VIRTUAL_WIDTH;
        double scaleY = canvas.getHeight() / VIRTUAL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        double offsetX = (canvas.getWidth() - VIRTUAL_WIDTH * scale) / 2;
        double offsetY = (canvas.getHeight() - VIRTUAL_HEIGHT * scale) / 2;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        gc.drawImage(shipTexture, 300, 400, 1300, 500);
        Image texture = switch (playerDirection) {
            case UP -> playerUp;
            case DOWN -> playerDown;
            case LEFT -> playerLeft;
            case RIGHT -> playerRight;
        };

        gc.drawImage(texture,
                player.getPosition().getX() - 32,
                player.getPosition().getY() - 32,
                90, 90);
        gc.drawImage(shipContourTexture, -145, -50, 2150, 1400);
        hud.render(gc); // render HUD \\
        Wall.displayWall(gc);
        ZoneView.render(gc, player); // Rendu visuel des zones interactives \\
        gc.restore();
    }

    private void goToMainMenu() {
        MainMenuScene menu = new MainMenuScene(primaryStage);
        primaryStage.setScene(menu.getScene());
    }

    public Scene getScene() {
        return scene;
    }
    //endregion

}

