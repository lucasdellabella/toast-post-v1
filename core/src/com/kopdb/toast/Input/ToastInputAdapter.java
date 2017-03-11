package com.kopdb.toast.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.kopdb.toast.Toast;
import com.kopdb.toast.ToastGame;

/**
 * Created by lucasdellabella on 1/5/17.
 */
public class ToastInputAdapter extends InputAdapter {

    Array<Toast> toastArray;
    Toast draggingToast;

    public ToastInputAdapter(Array<Toast> toastArray) {
        this.toastArray = toastArray;
    }

    private int convertToYUp(int yDown) {
        return Gdx.graphics.getHeight() - 1 - yDown;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // start tweening
        Vector3 in = new Vector3(screenX, screenY, 0);
        ToastGame.getCamera().unproject(in);
        screenX=(int)in.x;
        screenY=(int)in.y;
        for (Toast toast: toastArray) {
            boolean touched = toast.checkTouchDown(screenX, screenY);
            if (touched) {
                draggingToast = toast;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // release toast
        Vector3 in = new Vector3(screenX, screenY, 0);
        ToastGame.getCamera().unproject(in);
        screenX=(int)in.x;
        screenY=(int)in.y;
        if (draggingToast != null) {
            draggingToast.checkTouchUp(screenX, screenY);
            draggingToast = null;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 in = new Vector3(screenX, screenY, 0);
        ToastGame.getCamera().unproject(in);
        int convertedScreenX=(int)in.x;
        int convertedScreenY=(int)in.y;
        if (draggingToast != null) {
            draggingToast.checkTouchDragged(convertedScreenX, convertedScreenY);
        } else {
            touchDown(screenX, screenY, pointer, 0);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

