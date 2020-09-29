package com.cookie;

/**
 * A visitor function to apply different logic for different effect types.
 *
 * @param <T> The type returned by this function.
 */
public interface ProductionEffectVisitor<T> {
  /**
   * Apply this function to a building production effect.
   *
   * @param bpe   The BuildingProductionEffect to apply code to.
   * @return The return value of running this arbitrary function.
   */
  T applyToBuildingProductionEffect(BuildingProductionEffect bpe);

  /**
   * Apply this function to a clicking production effect.
   *
   * @param cpe The ClickingProductionEffect to apply code to.
   * @return The return value of running this arbitrary function.
   */
  T applyToClickingProductionEffect(ClickingProductionEffect cpe);
}
