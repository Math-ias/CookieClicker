package com.cookie;

/**
 * A simulated game of Cookie Clicker queryable, and transformable with user actions.
 * <p>
 * The queries available with this interface are measures and are expected to change discretely with
 * core game values.
 */
public interface CookieClicker extends SavedCookieClicker {

  // Getting new instances.

  /**
   * Provides a new version of this game state fast-forwarded.
   * <p>
   * This method not only changes in-game time, but also progresses game properties and statistics
   * as if the amount of time had really been played out.
   * <p>
   * Game objects encapsulated by this interface, like buffs, should also be progressed. The exact
   * details however are up to interpretation.
   * <p>
   * Note that in-game earning is not calculated directly from the rates of buildings, and the rate
   * of clicking. To find out what measures are used internally, use getRate or getCookiesPerClick.
   * The change in bank should be at least the sum of building, and clicking incomes.
   * <p>
   * Interpretations should not alter building inventory, upgrade inventory, or clicking rate on
   * warp.
   *
   * @param ticks The non-negative number of in-game ticks to fast-forward by. Cookie Clicker runs
   *              at 30FPS.
   * @return The resulting game state after being fast-forwarded.
   * @throws IllegalArgumentException If the provided number of ticks are negative.
   */
  CookieClicker warp(long ticks);

  /**
   * Provides a new version of this post-building-sale (buying or selling).
   * <p>
   * The unit price of a building is not necessarily used. Use getTransactionalBuildingsAmount to
   * find out what amount will paid or refunded in this sale.
   *
   * @param buildingType The building type to purchase or sell.
   * @param amount       The non-zero number of buildings involved in the sale. Positive numbers to
   *                     buy, negative numbers to sell.
   * @return A new state with an adjusted number of the buildingType.
   * @throws NullPointerException     If the buildingType is null.
   * @throws IllegalArgumentException If the amount is inappropriate, attempting to purchase an
   *                                  unaffordable amount or attempting to sell more buildings than
   *                                  are owned.
   */
  CookieClicker transactBuildings(BuildingType buildingType, int amount);

  /**
   * Provides a new version of this game state post-upgrade-purchase.
   * <p>
   * The unit price of an upgrade is not necessarily used. Use getUpgradePrice to find out what
   * amount will be paid for this purchase.
   *
   * @param upgrade The upgrade to buy.
   * @return The game state after a successful upgrade sale.
   * @throws IllegalArgumentException If the upgrade is already owned, is unaffordable, or is not
   *                                  purchasable.
   * @throws NullPointerException     If the upgrade is null.
   */
  CookieClicker buyUpgrade(ProductionUpgrade upgrade);

  /**
   * Provides a new version of this game state with a new buff.
   *
   * @param buff The buff to add to the game.
   * @return The new game state with this new buff.
   * @throws NullPointerException If the buff is null.
   */
  CookieClicker registerBuff(ProductionBuff buff);

  /**
   * Provides a new version of this game state with a cookie-adjusted bank account.
   * <p>
   * This method was provided for the dual-purpose of being able to simulate certain golden cookie
   * prizes, and being able to adjust state to set-up testing scenarios.
   * <p>
   * If the number of cookies added are positive, then the amount is added to cookies baked.
   *
   * @param cookies The number of cookies to change the bank account by.
   * @return The new game state.
   * @throws IllegalArgumentException If the bank account is put into an impossible state
   *                                  (negative).
   */
  CookieClicker adjustBank(double cookies);

  /**
   * Provides a new version of this game state with a new clicking rate.
   *
   * @param rate The new non-negative clicking rate in clicks per tick.
   * @return The new game state.
   * @throws IllegalArgumentException If the rate is negative.
   */
  CookieClicker setClickingRate(double rate);

  // Measures.

  /**
   * Returns the cookie production rate of all buildings of a certain building type.
   *
   * @param target A non-null building type to lookup the rate for.
   * @return The rate in cookies per tick that this building type in total is currently generating.
   * This returns zero if the building is not-owned.
   * @throws NullPointerException If target is null.
   */
  double getRate(BuildingType target);

  /**
   * Returns the price, or refund of this building sale.
   * <p>
   * This will always returns zero for a zero-amount purchase.
   *
   * @param target A non-null building type to lookup the sale amount for.
   * @param amount The non-zero number of buildings involved in the sale. Positive numbers refer to
   *               purchases. Negative numbers refer to sales.
   * @return A positive number for purchases indicating price, a negative number indicating refund.
   * @throws NullPointerException     If the buildingType is null.
   * @throws IllegalArgumentException If there is an attempt to query the refund price for more
   *                                  buildings than are currently owned.
   */
  double getTransactionalBuildingAmount(BuildingType target, int amount);

  /**
   * Returns the price of this upgrade purchase.
   *
   * @param upgrade A non-null upgrade to lookup the buying price for.
   * @return The current price of this upgrade.
   * @throws IllegalArgumentException If the upgrade has already been purchased.
   * @throws NullPointerException     If the upgrade is null.
   */
  double getUpgradePrice(ProductionUpgrade upgrade);

  /**
   * Returns how many cookies are produced by clicking the big cookie once.
   *
   * @return The number of cookies produced by one big cookie click.
   */
  double getCookiesPerClick();

}
