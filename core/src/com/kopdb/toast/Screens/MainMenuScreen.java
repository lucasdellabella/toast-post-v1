package com.kopdb.toast.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kopdb.toast.Input.ToasterInputAdapter;
import com.kopdb.toast.ToastGame;

/**
 * Created by O607 on 1/27/2017.
 */

public class MainMenuScreen implements Screen {

    private final ToastGame game;
    private Stage stage;
    Sprite toaster;
    Sprite toasterLever;
    Sprite title;
    Sprite arrow;

    private final Texture gameBackgroundImage;
    private final Texture menuBackgroundImage;
    private final float menuBackgroundAspect;
    private final float menuBackgroundHeight;

    private Vector2 camTarget = new Vector2(ToastGame.getCamera().viewportWidth / 2, ToastGame.getCamera().viewportHeight / 2);
    private Boolean startGame = false;
    private float toasterHeight;

    public MainMenuScreen(ToastGame game) {
        this.game = game;
        stage = new Stage(game.getViewport());

        gameBackgroundImage = new Texture(Gdx.files.internal("sky.png"));
        menuBackgroundImage = new Texture(Gdx.files.internal("mainmenu.png"));
        menuBackgroundAspect = (float)menuBackgroundImage.getHeight()/(float)menuBackgroundImage.getWidth();
        menuBackgroundHeight = ToastGame.getCamera().viewportWidth*menuBackgroundAspect;

        // Position toaster
        toaster = new Sprite(new Texture(Gdx.files.internal("Toaster.png")));
        float toasterAspect = 1574f/2048f;
        toasterHeight = toasterAspect*ToastGame.getCamera().viewportWidth*0.8f;
        toaster.setSize(ToastGame.getCamera().viewportWidth*0.8f, toasterHeight);
        toaster.setPosition(ToastGame.getCamera().viewportWidth*0.1f,80);

        // Position lever
        toasterLever = new Sprite(new Texture(Gdx.files.internal("ToasterLever.png")));
        float toasterLeverAspect = 192f/517f;
        toasterLever.setSize(toaster.getWidth()*0.6f, toaster.getWidth()*0.4f*toasterLeverAspect);
        toasterLever.setPosition(toaster.getWidth()*0.2f+toaster.getX(),toasterHeight*0.7f+toaster.getY());

        // Position arrow
        arrow = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        arrow.setSize(arrow.getWidth() * 0.2f, arrow.getHeight() * 0.2f);
        arrow.setPosition(toaster.getX() + toaster.getWidth() / 2 - arrow.getWidth() / 2,
                toaster.getY() + toaster.getHeight() * (5f / 8) - arrow.getHeight());

        // Position title
        title = new Sprite(new Texture(Gdx.files.internal("title.png")));
        float titleAspect = title.getHeight()/title.getWidth();
        title.setSize(ToastGame.getCamera().viewportWidth*0.8f, ToastGame.getCamera().viewportWidth*0.8f*titleAspect);
        title.setPosition(ToastGame.getCamera().viewportWidth*0.1f,
                ToastGame.getCamera().viewportHeight*0.75f);


        // Use stage as input processor
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(new ToasterInputAdapter(this, toasterLever, toasterLever.getY(), toasterHeight*0.4f+toaster.getY()));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //tweens camera to target and launches game if the camera reached the target and the game is meant to launch
        Vector2 camDiff = camTarget.cpy().sub(ToastGame.getCamera().position.x, ToastGame.getCamera().position.y);
        if (startGame && camDiff.len() <= 0.1)
        {
            switchToGameScreen();
            startGame = false;
            return;
        }

        // switch to a constant speed at some point
        if (camDiff.len() <= 10) {
            camDiff.scl(0.1f);
        } else {
            camDiff.scl(0.06f);
        }

        ToastGame.getCamera().translate(camDiff);

//        Gdx.gl.glClearColor(1, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ToastGame.getCamera().update();
        ToastGame.getBatch().setProjectionMatrix(ToastGame.getCamera().combined);
        ToastGame.getBatch().begin();
        ToastGame.getBatch().draw(gameBackgroundImage,
                0,
                menuBackgroundHeight,
                ToastGame.getCamera().viewportWidth,
                ToastGame.getCamera().viewportHeight);
        ToastGame.getBatch().draw(menuBackgroundImage,
                0,
                0,
                ToastGame.getCamera().viewportWidth,
                menuBackgroundHeight);
        toaster.draw(ToastGame.getBatch());
        arrow.draw(ToastGame.getBatch());
        toasterLever.draw(ToastGame.getBatch());

        title.draw(ToastGame.getBatch());

        ToastGame.getBatch().end();
        stage.act(delta);
    }

    public void toasterSwitchSet()
    {
        camTarget.y += menuBackgroundHeight;
        startGame = true;
    }
    private void switchToGameScreen() {
        game.getScreen().dispose();
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //uiSkin.dispose();
    }
}
