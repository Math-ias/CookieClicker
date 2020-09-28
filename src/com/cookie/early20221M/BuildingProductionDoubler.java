package com.cookie.early20221M;

import com.cookie.effects.BuildingProductionEffect;
import com.cookie.BuildingType;
import com.cookie.SavedCookieClicker;

/**
 * A class to provide a X2 BuildingProductionEffect.
 */
public class BuildingProductionDoubler implements BuildingProductionEffect {
  private final BuildingType target;

  /**
   * Create a new building production doubler.
   * @param target  The building target of this effect.
   */
  public BuildingProductionDoubler(BuildingType target) {
    this.target = target;
  }

  @Override
  public double getEffect(SavedCookieClicker stats) {
    return 2;
  }

  @Override
  public BuildingType getTarget() {
    return target;
  }

  @Override
  public TERM getTerm() {
    return TERM.MULTIPLIER;
  }
}
