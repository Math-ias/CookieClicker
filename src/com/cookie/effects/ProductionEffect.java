package com.cookie.effects;

/**
 * An arithmetic effect on in-game production rates.
 *
 * It is recommended to create these statically so that comparisons of effectively the same effect
 * can be equal.
 */
public interface ProductionEffect {
  /**
   * The term the calculated constant of this effect belongs to.
   */
  enum TERM {
    /**
     * Belonging to the multiplicative term. That is the product of unit rate and multipliers.
     */
    MULTIPLIER,
    /**
     * Belonging to the constant term. That is the sum of constants added to the grand total.
     */
    CONSTANT;
  }

  /**
   * Returns the term this effect's constant should be used in.
   *
   * @return Which term this effect's constant should be usd in.
   */
  TERM getTerm();

  /**
   * Accept and dispatch to the correct effect visitor function.
   *
   * @param visitor The visitor function to apply this to.
   * @param <T>     The return type of the visitor.
   * @return The return value of running this visitor code.
   */
  <T> T accept(ProductionEffectVisitor<T> visitor);
}