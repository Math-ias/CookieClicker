package com.cookie.effects;

import com.cookie.BuildingType;
import com.cookie.SavedCookieClicker;

/**
 * A production effect applying to the production of a specific building.
 */
public interface BuildingProductionEffect extends ProductionEffect {

  @Override
  default <T> T accept(ProductionEffectVisitor<T> visitor) {
    return visitor.applyToBuildingProductionEffect(this);
  }

  /**
   * Calculate the effect number to be used in calculation based on game state statistics.
   *
   * @param stats The game state statistics to calculate the effect from.
   * @return  The number to be used in calculation.
   * @throws NullPointerException If stats is null.
   */
  double getEffect(SavedCookieClicker stats);

  /**
   * Returns the target for this BuildingProductionEffect.
   * @return  The building that's production rate will be adjusted.
   */
  BuildingType getTarget();
}
