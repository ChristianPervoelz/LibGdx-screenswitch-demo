package de.pg.screen2d.screenswitch.demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.pg.scene2d.tools.screenswitch.SwitchDirection;
import de.pg.scene2d.tools.screenswitch.SwitchOptions;
import de.pg.scene2d.tools.screenswitch.SwitchType;
import de.pg.scene2d.tools.screenswitch.SwitchableScreen;

import java.util.Collection;
import java.util.HashSet;

public class DemoScreen implements SwitchableScreen {

  private final DemoApp app;
  private Stage stage;
  private String colorUse;
  private Collection<CheckBox> typeBoxes;
  private Collection<CheckBox> dirBoxes;

  public DemoScreen(DemoApp app, final boolean blackBack) {

    this.app = app;
    colorUse = blackBack ? "white" : "black";

    typeBoxes = new HashSet<>();
    dirBoxes = new HashSet<>();

    stage = new Stage(new ScreenViewport());

    Image background = new Image(new Texture((blackBack ? "black" : "white") + "_back.png"));
    background.setBounds(0,0,Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
    stage.addActor(background);

    Skin mySkin = new Skin(Gdx.files.internal("glassy-ui.json"));

    float screenCenter = Gdx.graphics.getWidth() / 2;

    Label name = new Label("Demo Screen " + (blackBack ? "1" : "2"), mySkin, colorUse);
    name.setPosition(screenCenter - name.getWidth() / 2, Gdx.graphics.getHeight() - 2 * name.getHeight());
    stage.addActor(name);

    float nextY = name.getY() - 2*name.getHeight();
    float typeCenter = screenCenter - screenCenter / 2;
    float directionCenter = screenCenter + screenCenter / 2;

    Label type = new Label("Type", mySkin, colorUse);
    type.setPosition(typeCenter - type.getWidth() / 2, nextY - type.getHeight());
    stage.addActor(type);

    Label direction = new Label("Direction", mySkin, colorUse);
    direction.setPosition(directionCenter - direction.getWidth() / 2, nextY - direction.getHeight());
    stage.addActor(direction);

    nextY -= 2*type.getHeight();

    float buttonY = nextY;

    buttonY = Math.min(createCheckboxes(nextY, typeCenter, mySkin, false, SwitchType.values()), buttonY);
    buttonY = Math.min(createCheckboxes(nextY, directionCenter, mySkin, true, SwitchDirection.values()), buttonY);


    TextButton tb = new TextButton("GO", mySkin, "small");
    tb.setPosition(screenCenter - tb.getWidth() / 2, buttonY - tb.getHeight());

//    tb.setTransform(true);
//    tb.setScale(0.5f);

    tb.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        SwitchType type = null;
        SwitchDirection dir = null;
        for (CheckBox cb : dirBoxes) {
          if(cb.isChecked()) {
            dir = (SwitchDirection) cb.getUserObject();
          }
        }

        for (CheckBox cb : typeBoxes) {
          if(cb.isChecked()) {
            type = (SwitchType) cb.getUserObject();
          }
        }

        SwitchOptions options = new SwitchOptions();
        options.direction = dir;
        options.type = type;
        options.duration = 1.f;
        options.interpolation = Interpolation.bounceOut;

//      app.screenSwitchManager.doSwitch(blackBack ? app.screen1 : app.screen2, blackBack ? app.screen2 : app.screen1, options);
      app.screenSwitchManager.doSwitch(blackBack ? app.screen1 : app.screen2, blackBack ? app.screen2 : app.screen1);

      }
    });

    stage.addActor(tb);

//    for (Actor actor : stage.getActors()) {
//      if(actor instanceof Group) {
//        ((Group) actor).setTransform(true);
//      }
//
//      actor.setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0, 0);
//
//      actor.setScale(0.9f);
//      if(actor instanceof Label) {
//        ((Label)actor).setFontScale(0.0000001f);
//      }
//
//      System.out.println(actor.getX() + ", " + actor.getY() + " , " + actor.getWidth() + ", " + actor.getHeight());
//      actor.debug();
//    }


  }

  private float createCheckboxes(float startY, float centerX, Skin mySkin, final boolean dirOrType, Object... objects) {
    ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>();
    buttonGroup.setMaxCheckCount(1);
    buttonGroup.setMinCheckCount(1);

    float biggestWidth = 0;
    float biggestHeight = 0;
    for (Object t: objects) {
      CheckBox cb = new CheckBox(t.toString(), mySkin, "radio-" + colorUse);
      biggestWidth = Math.max(cb.getWidth(), biggestWidth);
      biggestHeight = Math.max(cb.getHeight(), biggestHeight);
      buttonGroup.add(cb);
      cb.setUserObject(t);
      if(dirOrType) {
        dirBoxes.add(cb);
      } else {
        typeBoxes.add(cb);
      }
      stage.addActor(cb);
    }

    for (Actor a : buttonGroup.getButtons()) {
      a.setPosition(centerX - biggestWidth/2, startY - biggestHeight);
      startY = startY - 1.5f*biggestHeight;
    }

    return startY - 3*biggestHeight;
  }


  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.7f, 0.8f, 0.9f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act();
    stage.draw();

    SwitchOptions options = new SwitchOptions();
    options.direction = SwitchDirection.BOTTOM_TO_TOP;
    options.type = SwitchType.SLIDE_OVER;
    options.duration = 1.f;
    options.interpolation = Interpolation.bounceOut;

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

  }

  @Override
  public Stage getStage() {
    return stage;
  }

}
