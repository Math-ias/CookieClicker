package com.cookie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A collection of tests for ClickingProductionEffect implementations.
 */
public abstract class ClickingProductionEffectTest {
  abstract ClickingProductionEffect getImplementation();

  @Test
  public void testGetEffectIllegalArguments() {
    ClickingProductionEffect impl = getImplementation();
    assertThrows(NullPointerException.class, () -> impl.getNumber(null));
  }
}
