package com.cookie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A collection of tests for BuildingProductionEffect implementations.
 */
public abstract class BuildingProductionEffectTest {
  abstract BuildingProductionEffect getImplementation();

  @Test
  public void testGetEffectIllegalArguments() {
    BuildingProductionEffect impl = getImplementation();
    assertThrows(NullPointerException.class, () -> impl.getNumber(null));
  }
}