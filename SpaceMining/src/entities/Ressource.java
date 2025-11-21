package entities;

import ui.HUD;

import java.util.Random;

import static game.GameState.currentState;

/**
 * Représente une ressource exploitable dans le jeu.
 * Une ressource possède un nom, une fourchette de prix, un poids et
 * des contraintes d'extraction liées à l'outillage nécessaire.
 *
 * Cette classe implémente l'interface {@link IRessource} et centralise
 * les informations permettant de manipuler la ressource dans les systèmes
 * de commerce, extraction ou usure des outils.
 */
public class Ressource implements IRessource {
    //region Champs

    /**
     * Compteur global utilisé pour attribuer un identifiant unique à chaque ressource.
     */
    private static int count = 0;

    /**
     * Identifiant unique de la ressource.
     */
    protected final int id = count++;

    /**
     * Nom de la ressource (ex : "Or", "Fer", "Cristal").
     */
    protected String name;

    /**
     * Prix minimal possible de la ressource.
     */
    protected float priceMin;

    /**
     * Prix maximal possible de la ressource.
     */
    protected float priceMax;

    /**
     * Poids unitaire de la ressource (impacte la capacité du vaisseau).
     */
    protected float weight;

    /**
     * Outil nécessaire pour extraire cette ressource.
     * Peut-être null si aucun outil spécifique n'est requis.
     */
    protected String necessaryTool;

    /**
     * Impact sur le rendement de l'extraction.
     */
    protected float wearingImpact;
    protected int rendementMax = 3;

    // Mining element \\
    private static boolean isMining = false;
    private static float miningProgress = 0;
    private static float miningSpeed = 0.1f; // Progression par appel de update
    private static float miningDuration;
    private static Tool matchingTool;
    Random random = new Random();
    // ---->


    //endregion
    //region Constructeur

    /**
     * Constructeur minimal, utilisé lorsque seul le nom est connu.
     *
     * @param name nom de la ressource
     */
    public Ressource(String name){
        this.name = name;
    }

    /**
     * Constructeur définissant une fourchette de prix.
     *
     * @param name nom de la ressource
     * @param priceMin prix minimal
     * @param priceMax prix maximal
     */
    public Ressource(String name,float priceMin,float priceMax){
        this(name);
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    /**
     * Constructeur avec fourchette de prix et poids.
     *
     * @param name nom de la ressource
     * @param priceMin prix minimal
     * @param priceMax prix maximal
     * @param weight poids de la ressource
     */
    public Ressource(String name,float priceMin,float priceMax,float weight){
        this(name, priceMin, priceMax);
        this.weight = weight;
    }

    /**
     * Constructeur ajoutant l'outil nécessaire pour extraire la ressource.
     *
     * @param name nom de la ressource
     * @param priceMin prix minimal
     * @param priceMax prix maximal
     * @param weight poids de la ressource
     * @param necessaryTool outil requis
     */
    public Ressource(String name,float priceMin,float priceMax,float weight,String necessaryTool){
        this(name, priceMin, priceMax, weight);
        this.necessaryTool = necessaryTool;
    }

    /**
     * Constructeur complet incluant l'usure causée à l'outil.
     *
     * @param name nom de la ressource
     * @param priceMin prix minimal
     * @param priceMax prix maximal
     * @param weight poids unitaire
     * @param necessaryTool outil nécessaire pour l'extraction
     * @param wearingImpact impact d'usure sur l'outil
     */
    public Ressource(String name,float priceMin,float priceMax,float weight,String necessaryTool,float wearingImpact){
        this(name, priceMin, priceMax, weight, necessaryTool);
        this.wearingImpact = wearingImpact;
    }
    //endregion

    //region getteur setteur
    /** {@inheritDoc} */
    @Override
    public String getName(){
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public void setName(String name){
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public float getPriceMin(){
        return priceMin;
    }

    /** {@inheritDoc} */
    @Override
    public void setPriceMin(float priceMin){
        this.priceMin = priceMin;
    }

    /** {@inheritDoc} */
    @Override
    public float getPriceMax(){
        return priceMax;
    }

    /** {@inheritDoc} */
    @Override
    public void setPriceMax(float priceMax){
        this.priceMax = priceMax;
    }
    /** {@inheritDoc} */
    @Override
    public float getPrice(){
        return  priceMin + priceMax;
    }

    /** {@inheritDoc} */
    @Override
    public float getWeight(){
        return weight;
    }

    /** {@inheritDoc} */
    @Override
    public void setWeight(float weight){
        this.weight = weight;
    }

    /** {@inheritDoc} */
    @Override
    public String getNecessaryTool(){
        return necessaryTool;
    }

    /** {@inheritDoc} */
    @Override
    public void setNecessaryTool(String necessaryTool){
        this.necessaryTool = necessaryTool;
    }

    /** {@inheritDoc} */
    @Override
    public float getWearingImpact(){
        return wearingImpact;
    }

    /** {@inheritDoc} */
    @Override
    public void setWearingImpact(float wearingImpact){
        this.wearingImpact = wearingImpact;
    }

    public void startMining(HUD hud1) {
        if (isMining) return;
        HUD hud = HUD.getHud();
        isMining = true;
        miningProgress = 0;
        matchingTool = null;

        String necessaryTool = this.necessaryTool;
        System.out.println("necessaryTool = " + necessaryTool);
        System.out.println("miningProgress = " + currentState.ship.getTools());

        for (Tool t : currentState.ship.getTools()) {
            if (t.getName().equals(necessaryTool)) {
                matchingTool = t;
                break;
            }
        }
        if (matchingTool == null) return;
        int level = matchingTool.getLevel();
        miningDuration = (2f * currentState.currentPlanet.getDifficulty()) / level;

        hud.startMining();
        hud.addNotification("Minage de " + this.name + " en cours...");
    }

    public void checkMining(double dt, HUD hud) {
        if (!isMining) return;
        miningProgress += dt / miningDuration;
        hud.updateMiningProgress(miningProgress);
        if (miningProgress >= 1) {
            if (matchingTool == null) return;
            isMining = false;
            int gain = calculateGain();
            currentState.ship.addRessource(this, gain);
            hud.stopMining();
            hud.addNotification("Minage terminé ! +" + gain + " " + this.name);
        }


    }

    private int calculateGain() {
        if (matchingTool == null) return 0;
        int level = matchingTool.getLevel();
        if (level < 1) return 0;
        return random.nextInt(level) + 1;
    }
    //endregion




}
