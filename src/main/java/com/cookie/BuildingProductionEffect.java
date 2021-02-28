package com.cookie;

/**
 * An effect on the cookie production of a building type.
 */
public interface BuildingProductionEffect extends ProductionEffect {

  @Override
  default <T> T accept(ProductionEffectVisitor<T> visitor) {
    return visitor.applyToBuildingProductionEffect(this);
  }

  /**
   * Calculate the factor or number of this effect based on game statistics.
   *
   * @param stats The game state statistics to calculate the effect from.
   * @return The number to be used in calculation.
   * @throws NullPointerException If stats is null.
   */
  double getNumber(SavedCookieClicker stats);

  /**
   * Returns the affected building type.
   *
   * @return The affected building type of this effect.
   */
  BuildingType getTarget();
}

