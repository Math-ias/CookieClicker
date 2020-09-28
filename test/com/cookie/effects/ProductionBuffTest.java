package com.cookie.effects;

import com.cookie.effects.ProductionBuff;
import com.cookie.effects.ProductionEffect;
import com.cookie.mocks.MockClickingEffects;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A collection of tests to verify ProductionBuff behavior.
 */
public abstract class ProductionBuffTest {
  /**
   * Returns an implementation of production buff to test.
   * @return  A new instance of the production buff to test. This should have at least two ticks left.
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
    assertEquals(impl, nonWarped.get());

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

  /**
   * Verify that the ProductionBuff implementation correctly calculates equalness.
   * <p>
   * This test depends on correct warp behavior.
   */
  @Test
  public void testEqualsBehavior() {
    // This testing could be incomplete. I left it without all permutations of properties.
    ProductionBuff impl = getImplementation();
    assertEquals(impl, new BuffWrapper(impl));
    Optional<ProductionBuff> warped = impl.warp(1);
    assert warped.isPresent(); // Should be true according to getImplementation's promise.
    assertNotEquals(impl, warped.get());
  }

  /**
   * A ProductionBuff to another production buff and return the same properties.
   * <p>
   * This was created to test equals behavior and break if just using basic object comparison.
   */
  class BuffWrapper implements ProductionBuff {

    ProductionBuff buff;

    /**
     * Create a new BuffWrapper around an existing ProductionBuff.
     * @param buff  The ProductionBuff to wrap.
     */
    public BuffWrapper(ProductionBuff buff) {
      this.buff = buff;
    }

    @Override
    public long getTimeLeft() {
      return buff.getTimeLeft();
    }

    @Override
    public long getTimeTotal() {
      return buff.getTimeTotal();
    }

    @Override
    public Optional<ProductionBuff> warp(long ticks) {
      return buff.warp(ticks).map(BuffWrapper::new);
    }

    @Override
    public Collection<ProductionEffect> getEffects() {
      return buff.getEffects();
    }
  }
}
