package com.kopdb.toast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kopdb.toast.Screens.MainMenuScreen;

public class ToastGame extends Game {
	public static float BOX_2D_SCALE = 0.01f;//Multiply going in divide going out
    public static ObjectMap<String, Texture> typeTextures = new ObjectMap<>();
	public static ObjectMap<String, Sound> touchSounds = new ObjectMap<>();
	public static ObjectMap<String, Sound> flickSounds = new ObjectMap<>();
	private static SpriteBatch batch;
	private static World world;
	private static OrthographicCamera camera;
	private static Viewport viewport;

    @Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -300f * BOX_2D_SCALE), true);

        // Set up camera + viewport
		camera = new OrthographicCamera();
		getCamera().setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		float aspect = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		viewport = new FitViewport(540, 540 * aspect, getCamera());
		viewport.apply();
		getCamera().position.set(getCamera().viewportWidth / 2, getCamera().viewportHeight / 2, 0);

        // Fill in typeTexture map
        typeTextures.put("stache", new Texture(Gdx.files.internal("Toasts/StacheToast.png")));
        typeTextures.put("happy", new Texture(Gdx.files.internal("Toasts/HappyToast.png")));
		typeTextures.put("chef", new Texture(Gdx.files.internal("Toasts/cheftoast.png")));
		typeTextures.put("explorer", new Texture(Gdx.files.internal("Toasts/explorertoast.png")));
		typeTextures.put("angry", new Texture(Gdx.files.internal("Toasts/AngryToast.png")));
		typeTextures.put("jelly", new Texture(Gdx.files.internal("Toasts/jellytoast.png")));
		typeTextures.put("sad", new Texture(Gdx.files.internal("Toasts/SadToast.png")));
		typeTextures.put("worried", new Texture(Gdx.files.internal("Toasts/WorriedToast.png")));

		// Fill in touchSounds map
		touchSounds.put("stache", Gdx.audio.newSound(Gdx.files.internal("audio/stachetouch" +
				".wav")));
		touchSounds.put("happy", Gdx.audio.newSound(Gdx.files.internal("audio/happytouch" +
				".wav")));
		touchSounds.put("chef", Gdx.audio.newSound(Gdx.files.internal("audio/cheftouch.wav")));
		touchSounds.put("explorer", Gdx.audio.newSound(Gdx.files.internal("audio/explorertouch.wav")));
		touchSounds.put("angry", Gdx.audio.newSound(Gdx.files.internal("audio/angrytouch" +
				".wav")));
		touchSounds.put("jelly", Gdx.audio.newSound(Gdx.files.internal("audio/jellytouch" +
				".wav")));
		touchSounds.put("sad", Gdx.audio.newSound(Gdx.files.internal("audio/sadtouch.wav")));
		touchSounds.put("worried", Gdx.audio.newSound(Gdx.files.internal("audio/worriedtouch" +
				".wav")));

		// Fill in flickSounds map
		flickSounds.put("stache", Gdx.audio.newSound(Gdx.files.internal("audio/stacheflick" +
				".wav")));
		flickSounds.put("happy", Gdx.audio.newSound(Gdx.files.internal("audio/happyflick.wav")));
		flickSounds.put("chef", Gdx.audio.newSound(Gdx.files.internal("audio/chefflick.wav")));
		flickSounds.put("explorer", Gdx.audio.newSound(Gdx.files.internal("audio/explorerflick.wav")));
		flickSounds.put("angry", Gdx.audio.newSound(Gdx.files.internal("audio/angryflick.wav")));
		flickSounds.put("jelly", Gdx.audio.newSound(Gdx.files.internal("audio/jellyflick.wav")));
		flickSounds.put("sad", Gdx.audio.newSound(Gdx.files.internal("audio/sadflick.wav")));
		flickSounds.put("worried", Gdx.audio.newSound(Gdx.files.internal("audio/worriedflick" +
				".wav")));

		this.setScreen(new MainMenuScreen(this));
	}

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static World getWorld() {
        return world;
    }

    public static OrthographicCamera getCamera() {
        return camera;
    }

    public static Viewport getViewport() {
        return viewport;
    }

	@Override
	public void dispose () {
		getBatch().dispose();
	}
}
