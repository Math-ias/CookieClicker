package com.cookie;

import com.cookie.mocks.MockClickingEffects;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A collection of tests to verify ProductionBuff behavior.
 */
public abstract class ProductionBuffTest {
  /**
   * Returns an implementation of production buff to test.
   *
   * @return A new instance of the production buff to test. This should have at least two ticks
   * left.
   */
  abstract ProductionBuff getImplementation();

  /**
   * A test to verify that warp correctly validates arguments.
   */
  @Test
  public void testWarpIllegalArguments() {
    ProductionBuff impl = getImplementation();
    assertThrows(IllegalArgumentException.class, () -> impl.warp(-1));
  }

  /**
   * A test to verify correct behavior of the warp method. Assumes getTimeLeft works correctly.
   */
  @Test
  public void testWarpCorrectBehavior() {
    ProductionBuff impl = getImplementation();
    // Warping by zero seconds means returning an equal record.
    // It should also ensure this optional is non-empty.
    Optional<ProductionBuff> nonWarped = impl.warp(0);
    assertTrue(nonWarped.isPresent());
    assertEquals(impl.getTimeLeft(), nonWarped.get().getTimeLeft());
    assertEquals(impl.getTimeTotal(), nonWarped.get().getTimeTotal());
    assertEquals(impl.getEffects(), nonWarped.get().getEffects());


    // Warping by one second should decrease the number of ticks by one.
    // Everything else should be the same.
    Optional<ProductionBuff> warped = impl.warp(1); // getImplementation ensures this is non-empty.
    assertTrue(warped.isPresent());
    assertEquals(impl.getTimeLeft() - 1, warped.get().getTimeLeft());

    assertEquals(impl.getTimeTotal(), warped.get().getTimeTotal());
    assertEquals(impl.getEffects(), warped.get().getEffects());

    // Finally, warping past the expiration date of this buff results in an empty.
    assertTrue(impl.warp(impl.getTimeLeft()).isEmpty()); // The exact amount.
    assertTrue(impl.warp(impl.getTimeLeft() + 5).isEmpty()); // And past the expiration.
  }

  /**
   * Verify that the collection returned by get effects is immutable.
   */
  @Test
  public void testGetEffectsCollectionImmutability() {
    ProductionBuff impl = getImplementation();
    Collection<ProductionEffect> effects = impl.getEffects();
    // First we check we actually have a collection.
    assertNotNull(effects);
    // Now we try some modifying methods.
    // clear
    assertThrows(UnsupportedOperationException.class,
            () -> effects.clear());
    // add
    assertThrows(UnsupportedOperationException.class,
            () -> effects.add(MockClickingEffects.TIMES2));
  }
}
