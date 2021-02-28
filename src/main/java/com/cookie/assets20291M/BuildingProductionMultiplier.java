package com.cookie.assets20291M;

import com.cookie.BuildingProductionEffect;
import com.cookie.BuildingType;
import com.cookie.SavedCookieClicker;

import java.util.Objects;

/**
 * An effect used internally to multiply building production by a constant.
 * <p>
 * This class was made public because it's useful boilerplate.
 */
public class BuildingProductionMultiplier implements BuildingProductionEffect {
  private final BuildingType target;
  private final double multiplier;

  /**
   * Create a new targeted building production multiplier.
   *
   * @param target     The building target of this doubling effect.
   * @param multiplier The constant multiplier to use for this building target.
   * @throws NullPointerException If the building target is null.
   */
  public BuildingProductionMultiplier(BuildingType target, double multiplier) {
    Objects.requireNonNull(target, "Expected non-null building target.");
    this.target = target;
    this.multiplier = multiplier;
  }

  @Override
  public double getNumber(SavedCookieClicker stats) {
    Objects.requireNonNull(stats); // Need this to follow the spec, otherwise unnecessary.
    return multiplier;
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
