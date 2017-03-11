package com.kopdb.toast.Input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kopdb.toast.Screens.MainMenuScreen;
import com.kopdb.toast.ToastGame;

/**
 * Created by O607 on 2/11/2017.
 */

public class ToasterInputAdapter extends InputAdapter {

    MainMenuScreen mainMenu;
    Sprite lever;
    float leverTop;
    float leverBottom;
    float touchOffset;
    boolean done=false;
    boolean draggingLever = false;

    public ToasterInputAdapter(MainMenuScreen menu, Sprite toasterLever, float top, float bottom) {
        mainMenu = menu;
        lever = toasterLever;
        leverTop = top;
        leverBottom = bottom;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 in = new Vector3(screenX, screenY, 0);
        ToastGame.getCamera().unproject(in);
        screenX=(int)in.x;
        screenY=(int)in.y;
        touchOffset = screenY - lever.getY();

        if (lever.getBoundingRectangle().contains(screenX, screenY)) {
            draggingLever = true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 in = new Vector3(screenX, screenY, 0);
        ToastGame.getCamera().unproject(in);
        screenY=(int)in.y;
        float offsetY = screenY - touchOffset;

        // If the lever is being touched, move it
        if (draggingLever) {
            if (offsetY > leverTop) {
                lever.setY(leverTop);
            } else if (offsetY < leverBottom) {
                lever.setY(leverBottom);
                mainMenu.toasterSwitchSet();
            } else {
                lever.setY(offsetY);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        draggingLever = false;
        return false;
    }
}
//if (done){return true;}
//        Vector3 in = new Vector3(screenX, screenY, 0);
//        ToastGame.getCamera().unproject(in);
//        screenX=(int)in.x;
//        screenY=(int)in.y;
//        if (lever.getBoundingRectangle().contains(screenX, screenY)) {
//        if (screenY-touchOffset< leverBottom) {
//        done = true;
//        mainMenu.toasterSwitchSet();
//        return true;
//        }
//        if (screenY-touchOffset>leverTop) {
//        return true;
//        }
//        lever.setPosition(lever.getX(),screenY-touchOffset);
//        }
//
//        return true;
