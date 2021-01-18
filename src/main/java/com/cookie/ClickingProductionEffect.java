package com.cookie;

/**
 * An effect on the cookie production of clicking the big cookie.
 */
public interface ClickingProductionEffect extends ProductionEffect {
  @Override
  default <T> T accept(ProductionEffectVisitor<T> visitor) {
    return visitor.applyToClickingProductionEffect(this);
  }

  /**
   * Calculate the factor or constant to be used in calculation based on game state.
   * <p>
   * Clicking effects are intentionally calculated from a higher order interface than {@link
   * BuildingProductionEffect#getNumber} to allow access to measures like building rates.
   *
   * @param state The game state to calculate the effect from.
   * @return The number to be used in calculation.
   * @throws NullPointerException If stats is null.
   */
  double getNumber(CookieClicker state);
}
