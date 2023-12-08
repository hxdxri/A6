/*
  CMPT 270 Course Material
  Copyright (c) 2003-2021
  J.P. Tremblay and Grant Cheston
  All rights reserved.

  This document contains resources for homework assigned to students of
  CMPT 270 and shall not be distributed without permission.  Posting this
  file to a public or private website, or providing this file to any person
  not registered in CMPT 270 constitutes Academic Misconduct according to
  the University of Saskatchewan Policy on Academic Misconduct.
 */

package model;


import javax.swing.Timer;
import java.awt.event.ActionListener;

/**
 * The player in the space invaders game.
 */
public class Player extends GameObject {
    private static long startTime;
    public static final int WIDTH = 46;
    public static final int HEIGHT = 25;

    /** The distance to move when it is time to move. */
    public static final int MOVE_DISTANCE = 15;

    /** The decrease in the score every time hit. */
    public static final int HIT_DECREMENT = 20;

    /* The initial number of lives for the Player. */
    public static final int INITIAL_NUM_LIVES = 4;

    /* The number of lives remaining for the Player. */
    protected int lives;

    /* The current score for the Player. */
    protected int score;

    /** How frequently (in terms of ticks) the player is to change image. */
    public static final int CHANGE_FREQ = 0;

    /** A boolean attribute in Player to indicate whether the laser is recharging. */
    public static boolean isRecharging = false;

    /** A Timer object (java.swing.Timer) for the recharge delay */
    public static Timer rechargeLaser;

    /** A Timer object for the energy level and low power status */
    public static Timer retotalEnergyLevel;

    /** A timer object for recharging energy level by 1*/
    public static Timer incrementEnergyLevel;

    /** A timer for limiting the player's movement */
    public static Timer limitMobility;

    /** An integer attribute to represent the energy level of the tank */
    public int energyLevel = 5;

    /** A boolean attribute to indicate low power status */

    public boolean lowPowerStatus = false;

    /** A boolean attribute to indicate if a player can move */
    public boolean canMove = true;

    /**
     * Initialize the player.
     */
    public Player(int x, int y, Game game) {

        super(x, y, game, "player");
        startTime = System.currentTimeMillis();

        width = WIDTH;
        height = HEIGHT;
        lives = INITIAL_NUM_LIVES;
        score = 0;
        ActionListener rechargeLaser = e -> isRecharging = false;
        ActionListener rechargeTotalEnergyLevel = e -> {
            lowPowerStatus = false;
        };
        ActionListener rechargeEnergyLevel = e -> {if(energyLevel < 5){energyLevel++;}};
        ActionListener allowPlayerMovement = e -> {
            canMove = true;
        };

        Player.rechargeLaser = new Timer(200, rechargeLaser);
        retotalEnergyLevel = new Timer(10000, rechargeTotalEnergyLevel);
        incrementEnergyLevel = new Timer(350 , rechargeEnergyLevel);
        limitMobility = new Timer(500, allowPlayerMovement);

    }

    /**
     * No actions for the player at clock ticks.
     */
    protected void update() {
    }

    /**
     * Move to the left.
     */
    public void moveLeft() {
        if(canMove){
            x = Math.max(x - MOVE_DISTANCE, 0);
            if(lowPowerStatus){
                canMove = false;
            }
        }
    }

    /**
     * Move to the right.
     */
    public void moveRight() {
        if(canMove){
            x = Math.min(x + MOVE_DISTANCE, game.getWidth() - width);
            if(lowPowerStatus){
                canMove = false;
            }
        }
    }

    /**
     * If canFire, fire a laser.
     */
    public void fire() {
         if(!isRecharging && !lowPowerStatus){

             // if player decides to fire again in less than 350ms, restart
             // timer and do not increment energy level
             if (incrementEnergyLevel.isRunning()){
                 incrementEnergyLevel.restart();
             }

             int laserX = x + (width - Laser.WIDTH) / 2;
             int laserY = y - Laser.HEIGHT;
             game.addLaser(new Laser(laserX, laserY, game));

             isRecharging = true;
             rechargeLaser.start();

             energyLevel--;
             // player has depleted energy level, now in low power status
             if (energyLevel <= 0){
                 lowPowerStatus = true;
                 canMove = false;
                 limitMobility.start();
                 retotalEnergyLevel.start();
             }
             incrementEnergyLevel.start();
        }
    }

    /**
     * Handle the collision with another object.
     * 
     * @param other the object that collided with this instance
     */
    protected void collide(GameObject other) {
        lives = lives - 1;
        moveToLeftSide();
        if (lives == 0) {
            isDead = true;
        }
        score = score - HIT_DECREMENT;
    }

    /**
     * Move to the left side.
     */
    public void moveToLeftSide() {
        x = 0;
    }

    /**
     * @return the number of lives still remaining
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set a new value for the number of lives.
     * 
     * @param lives the new value for the lives field
     */
    public void setLives(int lives) {
        this.lives = lives;
        if (lives == 0) {
            isDead = true;
        }
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param amount the amount by which the score is to be increased
     */
    public void increaseScore(int amount) {
        score = score + amount;
    }
}

