package com.cookie;

import com.cookie.mocks.MockBuildingType;
import com.cookie.mocks.MockClickingBuff;
import com.cookie.mocks.MockProductionUpgrade;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test suite mixin for SavedCookieClicker implementations.
 */
public interface SavedCookieClickerTest {

  /**
   * Return a new instance of a SavedCookieClicker to test on.
   *
   * @return A non-null instance of SavedCookieClicker unused before.
   */
  SavedCookieClicker getImplementation();

  /**
   * Verify that getBuildingInventory implementations do not return a modifiable map.
   */
  @Test
  default void testGetBuildingInventoryMapImmutability() {
    SavedCookieClicker impl = getImplementation();
    Map<BuildingType, Integer> inventory = impl.getBuildingInventory();
    // First we check we actually have a map.
    assertNotNull(inventory);
    // Now we try some modifying methods.
    // clear
    assertThrows(UnsupportedOperationException.class,
            () -> inventory.clear());
    // put
    assertThrows(UnsupportedOperationException.class,
            () -> inventory.put(MockBuildingType.RATE1PRICE1, 1));
  }

  /**
   * Verify that getProductionUpgrades implementations do not return a modifiable collection.
   */
  @Test
  default void testGetProductionUpgradesCollectionImmutability() {
    SavedCookieClicker impl = getImplementation();
    Set<ProductionUpgrade> upgrades = impl.getProductionUpgrades();
    // First we check we actually have a collection.
    assertNotNull(upgrades);
    // Now we try some modifying methods.
    // clear
    assertThrows(UnsupportedOperationException.class,
            () -> upgrades.clear());
    // add
    assertThrows(UnsupportedOperationException.class,
            () -> upgrades.add(MockProductionUpgrade.DUD));
  }

  /**
   * Verify that getActiveBuffs implementations do not return a modifiable collection.
   */
  @Test
  default void testGetActiveBuffsCollectionImmutability() {
    SavedCookieClicker impl = getImplementation();
    Collection<ProductionBuff> buffs = impl.getActiveProductionBuffs();
    // First we check we actually have a collection.
    assertNotNull(buffs);
    // Now we try some modifying methods.
    // clear
    assertThrows(UnsupportedOperationException.class,
            () -> buffs.clear());
    // add
    assertThrows(UnsupportedOperationException.class,
            () -> buffs.add(new MockClickingBuff(1, 2)));
  }
}
