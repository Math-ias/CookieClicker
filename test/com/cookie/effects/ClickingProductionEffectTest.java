package com.cookie.effects;

import com.cookie.effects.ClickingProductionEffect;

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
    assertThrows(NullPointerException.class, () -> impl.getEffect(null));
  }
}
