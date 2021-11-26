package de.pg.screen2d.screenswitch.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Interpolation;
import de.pg.scene2d.tools.screenswitch.*;

public class DemoApp extends Game {

  // package protected only to make it accessible
  SwitchableScreen screen1;
  SwitchableScreen screen2;

  ScreenSwitchManager screenSwitchManager;

  @Override
  public void create() {
    screenSwitchManager = new ScreenSwitchManager(this);
    screen1 = new DemoScreen(this, true);
    screen2 = new DemoScreen(this, false);

    System.out.println(getClass().getCanonicalName());

    Transit transit = screenSwitchManager.addPredefined(screen1, screen2);
    transit.type(SwitchType.SLIDE_AWAY)
        .direction(SwitchDirection.TOP_TO_BOTTOM)
        .duration(5.75f)
        .interpolation(Interpolation.swing);
    ;
//
//    screenSwitchManager.registerScreen(screen1, "A beautiful welcome screen");
//    screenSwitchManager.registerScreen(screen2, "Come on player! Do it!");

//    screenSwitchManager.doSwitch("A beautiful welcome screen", "Come on player! Do it!");


    setScreen(screen1);
  }


  @Override
  public void render() {
    // let the transition manager do its work first, before we render
    screenSwitchManager.act();
    super.render();

  }

  @Override
  public void dispose() {
  }
}
