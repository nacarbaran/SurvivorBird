package com.barannacar.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.Button;
import java.util.Random;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class SurvivorBird extends ApplicationAdapter {

    SpriteBatch batch;
    Texture background, bird, enemy1, enemy2, enemy3;
    float birdX = 0;
    float birdY = 0;
    float iconSizeX = 0;
    float iconSizeY = 0;
    int gameState = 0;
    float velocity = 0;
    float gravity = 0.5f;
    float enemyVelocity = 10;
    Random random;

    int score = 0;
    int scoredEnemy = 0;

    BitmapFont font, font2;

    Circle birdCircle;
    Circle[] enemyCircles;
    Circle[] enemyCircles2;
    Circle[] enemyCircles3;

    ShapeRenderer shapeRenderer;

    int numberOfEnemies = 4;
    float[] enemyX = new float[numberOfEnemies];
    float distance = 0;

    float[] enemyOffSet = new float[numberOfEnemies];
    float[] enemyOffSet2 = new float[numberOfEnemies];
    float[] enemyOffSet3 = new float[numberOfEnemies];

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        bird = new Texture("bird.png");
        enemy1 = new Texture("enemy.png");
        enemy2 = new Texture("enemy.png");
        enemy3 = new Texture("enemy.png");

        distance = (float) Gdx.graphics.getWidth() / 2;
        random = new Random();

        birdX = (float) Gdx.graphics.getWidth() / 2 - (float) bird.getHeight() / 2;
        birdY = (float) Gdx.graphics.getHeight() / 3;

        shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();
        font2 = new BitmapFont();
        font.setColor(Color.WHITE);
        font2.setColor(Color.WHITE);
        font.getData().scale(4);
        font2.getData().scale(6);

        birdCircle = new Circle();
        enemyCircles = new Circle[numberOfEnemies];
        enemyCircles2 = new Circle[numberOfEnemies];
        enemyCircles3 = new Circle[numberOfEnemies];

        iconSizeX = (float) Gdx.graphics.getWidth() / 14;
        iconSizeY = (float) Gdx.graphics.getHeight() / 6;

        for (int i = 0; i < numberOfEnemies; i++) {

            enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            enemyX[i] = Gdx.graphics.getWidth() - (float) enemy1.getWidth() / 2 + i * distance;

            enemyCircles[i] = new Circle();
            enemyCircles2[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (enemyX[scoredEnemy] < birdX) {
                score++;

                if (scoredEnemy < (numberOfEnemies - 1)) {
                    scoredEnemy++;
                } else scoredEnemy = 0;
            }

            for (int i = 0; i < numberOfEnemies; i++) {

                if (enemyX[i] < (float) enemy1.getWidth() / 20) {
                    enemyX[i] = enemyX[i] + numberOfEnemies * distance;

                    enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                } else {
                    enemyX[i] = enemyX[i] - enemyVelocity;
                }

                batch.draw(enemy1, enemyX[i], (float) Gdx.graphics.getHeight() / 2 + enemyOffSet[i], iconSizeX, iconSizeY);
                batch.draw(enemy2, enemyX[i], (float) Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], iconSizeX, iconSizeY);
                batch.draw(enemy3, enemyX[i], (float) Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], iconSizeX, iconSizeY);

                enemyCircles[i] = new Circle(enemyX[i] + (iconSizeX / 2), (float) Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + (iconSizeY / 2), iconSizeX / 2);
                enemyCircles2[i] = new Circle(enemyX[i] + (iconSizeX / 2), (float) Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + (iconSizeY / 2), iconSizeX / 2);
                enemyCircles3[i] = new Circle(enemyX[i] + (iconSizeX / 2), (float) Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + (iconSizeY / 2), iconSizeX / 2);
            }


            if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.DPAD_CENTER)) {
                velocity = -14;
            }

            if (birdY > -100 && birdY < Gdx.graphics.getHeight() +50) {
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            } else {
                gameState = 2;
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.DPAD_CENTER)) {
                gameState = 1;
            }
        } else if (gameState == 2) {

            font2.draw(batch, "Game Over! Tap To Play Again!", 100, Gdx.graphics.getHeight() / 2);

            if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.DPAD_CENTER)) {
                gameState = 1;
                birdY = (float) Gdx.graphics.getHeight() / 3;

                for (int i = 0; i < numberOfEnemies; i++) {

                    enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                    enemyX[i] = Gdx.graphics.getWidth() - (float) enemy1.getWidth() / 2 + i * distance;

                    enemyCircles[i] = new Circle();
                    enemyCircles2[i] = new Circle();
                    enemyCircles3[i] = new Circle();
                }

                velocity = 0;
                score = 0;
                scoredEnemy = 0;
            }
        }

        batch.draw(bird, birdX, birdY, iconSizeX, iconSizeY);

        font.draw(batch, String.valueOf(score), 100, 200);

        batch.end();

        birdCircle.set(birdX + (iconSizeX / 2), birdY + (iconSizeY / 2), iconSizeX / 2);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < numberOfEnemies; i++) {
            //shapeRenderer.circle(enemyX[i] + (iconSizeX/2), (float) Gdx.graphics.getHeight()/2 + enemyOffSet[i] + (iconSizeY/2), iconSizeX/2);
            //shapeRenderer.circle(enemyX[i] + (iconSizeX/2), (float) Gdx.graphics.getHeight()/2 + enemyOffSet2[i] + (iconSizeY/2), iconSizeX/2);
            //shapeRenderer.circle(enemyX[i] + (iconSizeX/2), (float) Gdx.graphics.getHeight()/2 + enemyOffSet3[i] + (iconSizeY/2), iconSizeX/2);

            if (Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i])
                || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
                gameState = 2;
            }

        }
        //shapeRenderer.end();

    }

    @Override
    public void dispose() {
    }
}
