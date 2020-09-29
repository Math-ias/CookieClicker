package com.cookie;

/**
 * A production effect applying to clicking production.
 */
public interface ClickingProductionEffect extends ProductionEffect {
  @Override
  default <T> T accept(ProductionEffectVisitor<T> visitor) {
    return visitor.applyToClickingProductionEffect(this);
  }

  /**
   * Calculate the effect number to be used in calculation based on game state statistics.
   *
   * @param state The game state to calculate the effect from.
   * @return  The number to be used in calculation.
   * @throws NullPointerException If stats is null.
   */
  double getEffect(CookieClicker state);
}
