package com.cookie;

import com.cookie.mocks.MockBuildingType;
import com.cookie.mocks.MockClickingBuff;
import com.cookie.mocks.MockProductionUpgrade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A big collection of tests to verify the behavior of CookieClicker implementations with minimal
 * assumptions.
 */
public interface CookieClickerTest extends SavedCookieClickerTest {
  /**
   * Provide a fresh instance of the CookieClicker implementation.
   * <p>
   * This instance shouldn't own mock purchases.
   *
   * @return A fresh instance of CookieClicker.
   */
  CookieClicker getImplementation();

  // WARP

  /**
   * Verify warp's promised argument validation for ticks.
   */
  @Test
  default void testWarpIllegalArguments() {
    CookieClicker impl = getImplementation();
    assertThrows(IllegalArgumentException.class, () -> impl.warp(-1));
  }

  /**
   * Verify warp behavior for a trivial case, zero ticks.
   * <p>
   * This test relies on correct getters.
   */
  @Test
  default void testWarpZero() {
    CookieClicker impl = getImplementation();
    // First, let's verify that warping by zero ticks gives us the same state.
    CookieClicker unWarped = impl.warp(0);

    // We don't compare calculated measures.
    assertEquals(impl.getTicks(), unWarped.getTicks());
    assertEquals(impl.getCurrentBank(), unWarped.getCurrentBank());
    assertEquals(impl.getBuildingInventory(), unWarped.getBuildingInventory());
    assertEquals(impl.getProductionUpgrades(), unWarped.getProductionUpgrades());
    assertEquals(impl.getActiveProductionBuffs(), unWarped.getActiveProductionBuffs());
    assertEquals(impl.getCookiesBaked(), unWarped.getCookiesBaked());
    assertEquals(impl.getHandmadeCookies(), unWarped.getHandmadeCookies());
    assertEquals(impl.getCookieClicks(), unWarped.getCookieClicks());
    assertEquals(impl.getClickingRate(), unWarped.getClickingRate());
  }

  double DELTA = 0.1;

  /**
   * Verify warp behavior for a non-trivial case, a non-zero amount of ticks.
   * <p>
   * This depends on correct getters, measures, setClickingRate, and barterBuildings.
   */
  @Test
  default void testWarpNonzero() {
    final int TO_WARP = 30;
    final double CLICKING_RATE = 1;
    final int BUILDINGS_TO_BUY = 1;

    CookieClicker impl = getImplementation();
    // First, we calculate the minimum amount of profit we expect.
    // We attempt to secure a non-zero income for both clicking and building sources.
    BuildingType building = MockBuildingType.RATE1PRICE1;
    double buildingPrice = impl.getBarteringBuildingAmount(building, BUILDINGS_TO_BUY);
    CookieClicker nonzeroIncome = impl.adjustBank(buildingPrice)
            .barterBuildings(building, BUILDINGS_TO_BUY)
            .setClickingRate(CLICKING_RATE);
    double buildingIncome = nonzeroIncome.getRate(building) * TO_WARP;
    double cookieClicks = CLICKING_RATE * TO_WARP;
    double clickingIncome = nonzeroIncome.getCookiesPerClick() * cookieClicks;

    CookieClicker warped = nonzeroIncome.warp(TO_WARP);
    assertEquals(nonzeroIncome.getTicks() + TO_WARP, warped.getTicks());
    assertTrue(warped.getCurrentBank() - nonzeroIncome.getCurrentBank() >=
            buildingIncome + clickingIncome);
    assertEquals(warped.getCurrentBank() - nonzeroIncome.getCurrentBank(),
            warped.getCookiesBaked() - nonzeroIncome.getCookiesBaked(), DELTA);
    assertEquals(cookieClicks,
            warped.getCookieClicks() - nonzeroIncome.getCookieClicks(), DELTA);
    assertEquals(clickingIncome,
            warped.getHandmadeCookies() - nonzeroIncome.getHandmadeCookies(), DELTA);

    assertEquals(nonzeroIncome.getBuildingInventory(), warped.getBuildingInventory());
    assertEquals(nonzeroIncome.getProductionUpgrades(), warped.getProductionUpgrades());
    assertEquals(nonzeroIncome.getClickingRate(), warped.getClickingRate());
  }

  // BARTERBUILDINGS

  /**
   * Verify barterBuildings correctly validates the buildingType argument.
   */
  @Test
  default void testBarterBuildingsIllegalBuildingType() {
    CookieClicker impl = getImplementation();
    assertThrows(NullPointerException.class,
            () -> impl.barterBuildings(null, 1));
  }

  /**
   * Verify barterBuildings correctly validates the amount parameter in different scenarios.
   * <p>
   * This relies on correct adjustBank and getBarteringBuildingAmount.
   */
  @Test
  default void testBarterBuildingsIllegalAmount() {
    final int TO_BUY = 1;

    CookieClicker impl = getImplementation();
    BuildingType building = MockBuildingType.RATE1PRICE1;
    double buildingPrice = impl.getBarteringBuildingAmount(building, TO_BUY);
    // We adjust our state so that we have not enough cookies.
    CookieClicker noFunds = impl.adjustBank(-impl.getCurrentBank());

    // We assume that the implementation does not yet have our mock building.
    assertThrows(IllegalArgumentException.class, () -> noFunds.barterBuildings(building, TO_BUY));
    assertThrows(IllegalArgumentException.class, () -> noFunds.barterBuildings(building, -TO_BUY));

    // What if we had enough cookies to buy? But not the inventory to sell one more than we bought?
    CookieClicker bought = noFunds.adjustBank(buildingPrice)
            .barterBuildings(building, TO_BUY);

    assertThrows(IllegalArgumentException.class,
            () -> bought.barterBuildings(building, -TO_BUY - 1));
  }

  /**
   * Verify the corner case of using barterBuildings with an amount of zero.
   * <p>
   * This test relies on correct getter behavior.
   */
  @Test
  default void testBarterBuildingsZero() {
    CookieClicker impl = getImplementation();
    final BuildingType BUILDING = MockBuildingType.RATE1PRICE1;
    CookieClicker zeroPurchase = impl.barterBuildings(BUILDING, 0);
    assertEquals(impl.getBuildingInventory().getOrDefault(BUILDING, 0),
            zeroPurchase.getBuildingInventory().getOrDefault(BUILDING, 0));
  }

  /**
   * Verify barterBuildings correctly behaves when purchasing and refunding buildings.
   * <p>
   * This relies on correct getters, and adjustBank.
   */
  @Test
  default void testBarterBuildingsBuyingAndSelling() {
    final int TO_BUY_FIRST = 1;
    final int TO_BUY_SECOND = 2;

    // First we make one purchase.
    CookieClicker impl = getImplementation();
    BuildingType building = MockBuildingType.RATE1PRICE1;
    double firstPrice = impl.getBarteringBuildingAmount(building, TO_BUY_FIRST);
    // I do have an assumption here that price won't change after adjusting the bank account.
    // I also assume the mock building isn't in inventory.
    CookieClicker firstPurchaseReady = impl.adjustBank(firstPrice);
    CookieClicker firstPurchased = firstPurchaseReady.barterBuildings(building, TO_BUY_FIRST);
    assertEquals(firstPrice,
            firstPurchaseReady.getCurrentBank() - firstPurchased.getCurrentBank(), DELTA);
    assertEquals(TO_BUY_FIRST,
            firstPurchased.getBuildingInventory().getOrDefault(building, 0), DELTA);

    // Then we make another purchase.
    double secondPrice = impl.getBarteringBuildingAmount(building, TO_BUY_SECOND);
    CookieClicker secondPurchaseReady = firstPurchased.adjustBank(secondPrice);
    CookieClicker secondPurchased = secondPurchaseReady.barterBuildings(building, TO_BUY_SECOND);
    assertEquals(secondPrice,
            secondPurchaseReady.getCurrentBank() - secondPurchased.getCurrentBank(), DELTA);
    assertEquals(TO_BUY_FIRST + TO_BUY_SECOND,
            secondPurchased.getBuildingInventory().getOrDefault(building, 0));

    // We should be able to refund either the first or second purchase, or both of them together.
    double firstRefund = secondPurchased.getBarteringBuildingAmount(building, -TO_BUY_FIRST);
    double secondRefund = secondPurchased.getBarteringBuildingAmount(building, -TO_BUY_SECOND);
    double combinedRefund = secondPurchased.getBarteringBuildingAmount(building, -TO_BUY_FIRST - TO_BUY_SECOND);

    // Let's refund just the first purchase.
    CookieClicker firstRefunded = secondPurchased.barterBuildings(building, -TO_BUY_FIRST);
    assertEquals(firstRefund,
            firstRefunded.getCurrentBank() - secondPurchased.getCurrentBank(), DELTA);
    assertEquals(TO_BUY_SECOND,
            firstRefunded.getBuildingInventory().getOrDefault(building, 0), DELTA);

    // What if we had just refunded the second purchase?
    CookieClicker secondRefunded = secondPurchased.barterBuildings(building, -TO_BUY_SECOND);
    assertEquals(secondRefund,
            secondRefunded.getCurrentBank() - secondPurchased.getCurrentBank(), DELTA);
    assertEquals(TO_BUY_FIRST,
            secondRefunded.getBuildingInventory().getOrDefault(building, 0), DELTA);

    // What if we had refunded both purchases at the same time?
    CookieClicker combinationRefunded = secondPurchased.barterBuildings(building, -TO_BUY_FIRST - TO_BUY_SECOND);
    assertEquals(combinedRefund,
            secondPurchased.getCurrentBank() - combinationRefunded.getCurrentBank(), DELTA);
    assertEquals(0,
            combinationRefunded.getBuildingInventory().getOrDefault(building, 0));
  }

  // BUYUPGRADE

  /**
   * Test that attempting to buy a null upgrade results in a NullPointerException.
   */
  @Test
  default void testBuyUpgradeNullPointer() {
    assertThrows(NullPointerException.class, () -> getImplementation().buyUpgrade(null));
  }

  /**
   * Test that attempting to buy an upgrade with insufficient funds or that's already owned is
   * rejected.
   * <p>
   * This test relies on correct getUpgradePrice and adjustBank behavior.
   */
  @Test
  default void testBuyUpgradeIllegalScenarios() {
    final ProductionUpgrade upgradeNoPrereqs = MockProductionUpgrade.DUD;
    CookieClicker impl = getImplementation();
    double upgradeNPPrice = impl.getUpgradePrice(upgradeNoPrereqs);

    // First, insufficient funds.
    CookieClicker noFunds = impl.adjustBank(-impl.getCurrentBank());
    assertThrows(IllegalArgumentException.class, () -> noFunds.buyUpgrade(upgradeNoPrereqs));

    // Then, if it's already owned.
    CookieClicker upgradeNPBought = impl.adjustBank(upgradeNPPrice)
            .buyUpgrade(upgradeNoPrereqs);
    assertThrows(IllegalArgumentException.class, () -> upgradeNPBought.buyUpgrade(upgradeNoPrereqs));

    // Alternatively, we set up a scenario where the upgrade ...
    // * isn't owned (assumed),
    // * can be afforded
    // * but failed it's purchasable check.

    final ProductionUpgrade upgradePrereqs = MockProductionUpgrade.MUST_OWN_1RATE1PRICE1;
    double upgradePPrice = impl.getUpgradePrice(upgradePrereqs);
    CookieClicker purchasePReady = impl.adjustBank(upgradePPrice);
    assertThrows(IllegalArgumentException.class, () -> purchasePReady.buyUpgrade(upgradePrereqs));
  }

  /**
   * Verify buyUpgrade behavior in the correct scenarios.
   * <p>
   * This relies on correct getter, adjustBank, and barterBuildings behavior.
   */
  @Test
  default void testBuyUpgrade() {
    final ProductionUpgrade UPGRADE_NO_PREREQS = MockProductionUpgrade.DUD;
    final ProductionUpgrade UPGRADE_PREREQS = MockProductionUpgrade.MUST_OWN_1RATE1PRICE1;
    final BuildingType PREREQUISITES = MockBuildingType.RATE1PRICE1; // We will buy only one.

    CookieClicker impl = getImplementation();
    double upgradeNPPrice = impl.getUpgradePrice(UPGRADE_NO_PREREQS);
    double upgradePPrice = impl.getUpgradePrice(UPGRADE_PREREQS);
    double prereqPrice = impl.getBarteringBuildingAmount(PREREQUISITES, 1);
    // We assume buying these items consecutively doesn't change price.

    CookieClicker purchaseReady = impl.adjustBank(upgradeNPPrice + upgradePPrice + prereqPrice);
    CookieClicker purchasedNoPrereqs = purchaseReady.buyUpgrade(UPGRADE_NO_PREREQS);
    assertTrue(purchasedNoPrereqs.getProductionUpgrades().contains(UPGRADE_NO_PREREQS));
    assertEquals(upgradeNPPrice,
            purchaseReady.getCurrentBank() - purchasedNoPrereqs.getCurrentBank(), DELTA);

    // If we fill out prerequisites we should be able to purchase the second upgrade as well.
    CookieClicker purchasedPrereqs = purchasedNoPrereqs.barterBuildings(PREREQUISITES, 1)
            .buyUpgrade(UPGRADE_PREREQS);
    assertTrue(purchasedPrereqs.getProductionUpgrades().contains(UPGRADE_PREREQS));
    assertEquals(upgradePPrice + prereqPrice,
            purchasedNoPrereqs.getCurrentBank() - purchasedPrereqs.getCurrentBank(), DELTA);
  }

  // ADJUSTBANK

  /**
   * Verify adjustBank correctly validates it's arguments.
   */
  @Test
  default void testAdjustBankIllegalArguments() {
    CookieClicker impl = getImplementation();
    assertThrows(IllegalArgumentException.class, () -> impl.adjustBank(-impl.getCurrentBank() - 1));
  }

  /**
   * Verify adjustBank behavior is correct.
   * <p>
   * This test relies on correct getter behavior.
   */
  @Test
  default void testAdjustBank() {
    final double TO_INFUSE = 15.0;

    CookieClicker impl = getImplementation();
    // First, we test that we can add to the bank.
    CookieClicker implInfused = impl.adjustBank(TO_INFUSE);
    assertEquals(impl.getCurrentBank() + TO_INFUSE, implInfused.getCurrentBank(), DELTA);
    assertEquals(impl.getCookiesBaked() + TO_INFUSE, implInfused.getCookiesBaked(), DELTA);

    // Test that we can remove cookies.
    CookieClicker implRemoved = implInfused.adjustBank(-TO_INFUSE);
    assertEquals(impl.getCurrentBank(), implRemoved.getCurrentBank(), DELTA);
    assertEquals(implInfused.getCookiesBaked(), implRemoved.getCookiesBaked(), DELTA);
  }

  // REGISTERBUFF

  /**
   * Verify registerBuff correctly rejects null arguments.
   */
  @Test
  default void testRegisterBuffIllegalArguments() {
    assertThrows(NullPointerException.class, () -> getImplementation().registerBuff(null));
  }

  /**
   * Verify registerBuff behavior when it's done correctly.
   */
  @Test
  default void testRegisterBuff() {
    final ProductionBuff buff = new MockClickingBuff(10, 20);

    CookieClicker impl = getImplementation();
    // We use ProductionBuff equals behavior expectations here.
    CookieClicker registeredBuff = impl.registerBuff(buff);
    assertTrue(registeredBuff.getActiveProductionBuffs().contains(buff));
  }

  // SETCLICKINGRATE

  /**
   * Test that setClickingRate correctly invalidates illegal arguments.
   */
  @Test
  default void testSetClickingRateIllegalArgument() {
    assertThrows(IllegalArgumentException.class, () -> getImplementation().setClickingRate(-1));
  }

  /**
   * Test that setClickingRate behaves correctly.
   */
  @Test
  default void testSetClickingRate() {
    final double CLICKING_RATE = 1;

    CookieClicker clicking = getImplementation().setClickingRate(CLICKING_RATE);
    assertEquals(CLICKING_RATE, clicking.getClickingRate());
  }

  // GETRATE

  /**
   * Test that getRate correctly invalidates illegal arguments.
   */
  @Test
  default void testGetRateIllegalArguments() {
    CookieClicker impl = getImplementation();
    assertThrows(NullPointerException.class, () -> impl.getRate(null));
  }

  /**
   * Test that getRate correctly behaves.
   * <p>
   * This test relies on correct barterBuildings behavior.
   */
  @Test
  default void testGetRate() {
    final BuildingType PURCHASE = MockBuildingType.RATE1PRICE1;

    CookieClicker impl = getImplementation();
    // I assume we don't already own a mock purchase.
    assertEquals(0, impl.getRate(PURCHASE));

    double purchasePrice = impl.getBarteringBuildingAmount(PURCHASE, 1);
    CookieClicker purchased = impl.adjustBank(purchasePrice).barterBuildings(PURCHASE, 1);
    // I assume whatever happens with building rates that there is at least some amount of income.
    assertTrue(purchased.getRate(PURCHASE) > 0);
  }

  // GETBARTERINGBUILDINGAMOUNT

  /**
   * Test that getBarteringBuild correctly invalidates illegal arguments.
   * <p>
   * This relies on correct barterBuildings behavior.
   */
  @Test
  default void testGetBarteringBuildingAmountIllegalArguments() {
    BuildingType PURCHASE = MockBuildingType.RATE1PRICE1;

    CookieClicker impl = getImplementation();
    assertThrows(NullPointerException.class,
            () -> impl.getBarteringBuildingAmount(null, 0));
    // I assume that there are currently no mock purchases already owned,
    // then we can't query the refund price.
    assertThrows(IllegalArgumentException.class,
            () -> impl.getBarteringBuildingAmount(PURCHASE, -1));

    // We can give ourselves one purchase, but it'd still be wrong to query for the refund of two.
    double purchasePrice = impl.getBarteringBuildingAmount(PURCHASE, 1);
    CookieClicker onePurchased = impl.adjustBank(purchasePrice).barterBuildings(PURCHASE, 1);
    assertThrows(IllegalArgumentException.class,
            () -> onePurchased.getBarteringBuildingAmount(PURCHASE, -2));
  }

  // I don't test getBarteringBuildingAmount because too much is up to the implementation.

  // GETUPGRADEPRICE

  /**
   * Test that getUpgradePrice correctly invalidates illegal arguments.
   */
  @Test
  default void testGetUpgradePriceIllegalArguments() {
    ProductionUpgrade UPGRADE = MockProductionUpgrade.DUD;

    CookieClicker impl = getImplementation();
    assertThrows(NullPointerException.class, () -> impl.getUpgradePrice(null));

    // Now we set up already owning the upgrade.
    double upgradePrice = impl.getUpgradePrice(UPGRADE);
    CookieClicker purchased = impl.adjustBank(upgradePrice).buyUpgrade(UPGRADE);
    assertThrows(IllegalArgumentException.class, () -> purchased.getUpgradePrice(UPGRADE));
  }

  // I don't test getUpgradePrice because too much is up to the implementation.
}
