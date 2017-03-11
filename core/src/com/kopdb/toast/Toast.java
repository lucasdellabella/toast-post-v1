package com.kopdb.toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by Richard Kopelow on 1/5/2017.
 */
public class Toast implements Disposable
{
    final private float FLICK_THRESHOLD = (float) Math.pow(2.5, 2);
    private Sprite sprite;
    private BodyDef bodyDef;
    private PolygonShape shape;
    private Body body;
    private Vector2 moveTarget;
    private Vector2 touchOffset;
    private Vector2 lastPosition;
    private Vector2 newPos;
    private Vector2 origin;
    private Vector2 flickStartPos;
    private long touchTime;
    private long lastTouchTime;
    private String type;
    private Sound touchSound;
    private Sound flickSound;
    private boolean flicked = false;
    private boolean curBodyNeedsToSlow = false;
    private float flickTimeCounter = 0;
    private final float FLICK_TIME_MAX = 0.4f;

    private int tmpCnt = 0;


    public Toast(String toastType) {

        type = toastType;
        sprite = new Sprite(ToastGame.typeTextures.get(toastType));
        touchSound = ToastGame.touchSounds.get(toastType);
        flickSound = ToastGame.flickSounds.get(toastType);

        float scaleFactor = 0.125f;

        sprite.setSize(sprite.getWidth() * scaleFactor, sprite.getHeight() * scaleFactor);
        origin = new Vector2(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setOrigin(origin.x,origin.y);
        sprite.setPosition(MathUtils.random(ToastGame.getCamera().viewportWidth - sprite.getWidth()), 0);

        bodyDef = new BodyDef();
        getBodyDef().type = BodyDef.BodyType.DynamicBody;
        getBodyDef().position.set(getSprite().getX() * ToastGame.BOX_2D_SCALE,
                - getSprite().getHeight() * ToastGame.BOX_2D_SCALE);

        shape = new PolygonShape();
        getShape().setAsBox(getSprite().getWidth()/2 * ToastGame.BOX_2D_SCALE, getSprite().getHeight()/2 * ToastGame.BOX_2D_SCALE, new Vector2(0,0),0);

        setBody(ToastGame.getWorld().createBody(getBodyDef()));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = getShape();
        fixtureDef.density = 1f;
        getBody().createFixture(fixtureDef);
        getBody().setType(BodyDef.BodyType.StaticBody);
        getBody().setType(BodyDef.BodyType.DynamicBody);

        float distToMiddle = ToastGame.getCamera().viewportWidth / 2 - (sprite.getX() + sprite.getWidth() / 2);
        float xVel = distToMiddle / 100 * MathUtils.random(100)
                + Math.signum(distToMiddle) * MathUtils.random(100f);
        float yVel = 420 + MathUtils.random(350);
        getBody().setLinearVelocity(xVel * ToastGame.BOX_2D_SCALE, yVel * ToastGame.BOX_2D_SCALE);

        // Something is wrong with motion and collision of bodies when they are rotating
        //getBody().setAngularVelocity(MathUtils.random(-1, 1) * MathUtils.random(0, 50));
        lastPosition = body.getPosition().cpy();
    }

    public void update() {
        if (moveTarget != null) {
            lastPosition = body.getPosition().cpy();

            // Vector used to move the current toast
            Vector2 impulseAmount = new Vector2(
                    (moveTarget.x - body.getPosition().x - body.getLinearVelocity().x * 0.08f) * 3f,
                    (moveTarget.y - body.getPosition().y - body.getLinearVelocity().y * 0.08f) * 1.5f);

            // Increase or clear the flickTimeCounter based on whether we are flicking or not
            if (body.getLinearVelocity().len2() <= 0.01f) {
                flickTimeCounter = 0;
            } else {
                flickTimeCounter += Gdx.graphics.getDeltaTime();
            }

            newPos = body.getPosition();
            body.applyLinearImpulse(impulseAmount, body.getWorldCenter(), true);
        }

        sprite.setPosition(body.getPosition().x / ToastGame.BOX_2D_SCALE - origin.x,
                body.getPosition().y / ToastGame.BOX_2D_SCALE - origin.y);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean checkTouchDown(int x, int y) {
        boolean toastTouched = sprite.getBoundingRectangle().contains(x, y);
        if (toastTouched) {
            // save x & y relative bottom left corner of body
            touchOffset = body.getPosition().cpy().scl(1 / ToastGame.BOX_2D_SCALE).sub(x, y);
            // where we want the toast to move to
            moveTarget = new Vector2(x + touchOffset.x, y + touchOffset.y);
            flickStartPos = moveTarget.cpy();
            moveTarget.scl(ToastGame.BOX_2D_SCALE);
            // remember moveTarget
            lastPosition = body.getPosition().cpy();
            lastTouchTime = 0;
            touchTime = System.currentTimeMillis();
            body.setLinearVelocity(0, 0);
//            body.setType(BodyDef.BodyType.KinematicBody);
            touchSound.play();
            tmpCnt += 1;
        }

        return toastTouched;
    }

    // TODO: use pointers to handle drag off and release?
    // Got a divide by zero exception from
    // velocity.scl(1000 / (System.currentTimeMillis() - lastTouchTime));
    public void checkTouchUp(int x, int y) {
        if (moveTarget != null) {

            touchOffset = body.getPosition().cpy().scl(1 / ToastGame.BOX_2D_SCALE).sub(x, y);
            moveTarget = new Vector2(x + touchOffset.x, y + touchOffset.y);
            moveTarget = new Vector2(x, y);

            // also the
            Vector2 dragAmount = new Vector2(0, 0);
            dragAmount.add(moveTarget).sub(flickStartPos);

            // Do not flick if the dragAmount was small
            if (flickTimeCounter < FLICK_TIME_MAX && dragAmount.len() > 50f) {
               // set velocity based on position relative to initial pos / time
                // soft cap on scaleVal to prevent excessive speed of toast
                float scaleVal = 0.005f / ((flickTimeCounter + 0.055f) / 2f);
                scaleVal = scaleVal > 100
                        ? 100 + Math.min((float) Math.sqrt(scaleVal - 100), 130)
                        : scaleVal;

                dragAmount.scl(scaleVal);
                body.setLinearVelocity(dragAmount);
            }

            // decide whether the release counted as a flick
            if (body.getLinearVelocity().len2() >= FLICK_THRESHOLD) {
                flicked = true;
                touchSound.stop();
                flickSound.play();
            }

            moveTarget = null;
            flickStartPos = null;
            flickTimeCounter = 0;
        }
    }

    public void checkTouchDragged(int x, int y) {
        if (moveTarget != null) {
            moveTarget = new Vector2(x + touchOffset.x, y + touchOffset.y);
            moveTarget.scl(ToastGame.BOX_2D_SCALE);
            lastTouchTime = touchTime;
            touchTime = System.currentTimeMillis();
            tmpCnt += 1;
        } else {
            checkTouchDown(x, y);
        }

    }

    public Sprite getSprite() {
        return sprite;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public PolygonShape getShape() {
        return shape;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void dispose() {
        ToastGame.getWorld().destroyBody(getBody());
    }


    public String getType() {
        return type;
    }

    public boolean isFlicked() {
        return flicked;
    }
}
