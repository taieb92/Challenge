package com.n26.challenge.exceptions;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For throwing exceptions anywhere in this project, use the methods of this class. They create and
 * log exceptions in a unified way, creating a correlation ID (corrId) for every thrown exception.
 * With this ID, an exception can be traced through the logs.
 *
 */
public abstract class ChallengeException extends Exception {

  private static final long serialVersionUID = -2535119518364731375L;

  private static final Logger log = LoggerFactory.getLogger(ChallengeException.class);

  /**
   * Legal values for field technicalName.
   */
  public enum Name {
    /**
     * Transaction is too old
     */
    TOO_OLD_EXCEPTION,
    /**
     * Transaction is in the future
     */
    IMPOSSIBLE_FUTURE_TRANSACTION_EXCEPTION


  }


  /**
   * Unique identifier for the specific instance of the exception. Allows tracking of exceptions at
   * frontend and in log files.
   */
  private final UUID corrId;

  /**
   * Creates and logs an exception. Message must be provided, logger should be provided.
   *
   * @param message which initializes the exception, has to be provided
   * @param logger the logger of the instance that threw this exception, can be null but then error
   *        traceability is not ensured.
   */
  protected ChallengeException(final String message, Logger logger) {
    super(message);
    this.corrId = UUID.randomUUID();

    if (logger == null) {
      logger = log;
    }
    if (getMessage() == null) {
      error(logger, "[corrId=" + getCorrId() + "] Undefined internal exception was triggered.");
    } else {
      error(logger, "[corrId=" + getCorrId() + "] Exception: ", this);
    }
  }

  /**
   * Creates and logs an exception, together with the root exception which triggered this exception.
   * Message must be provided, logger should be provided.
   *
   * @param message which initializes the exception, has to be provided
   * @param logger the logger of the instance that threw this exception, can be null but then error
   *        traceability is not ensured.
   * @param rootException the exception which triggered the creation of this exception, can be
   *        omitted (use other constructor) or null (nothing happens)
   */
  protected ChallengeException(final String message, final Logger logger,
      final Exception rootException) {
    this(message, logger);

    if (rootException != null) {
      error(logger, "[corrId={}] Root Exception Message: " + getCorrId() + " " +
          rootException.getMessage());
      error(logger, "Root Exception: ", rootException);
      super.initCause(rootException);
    }
  }

  /**
   * Get the technical name of this Exception.
   *
   * @return the technical name
   */
  public abstract String getTechnicalName();

  /**
   * Get the unique correlation ID to identify this exact instance of the exception.
   *
   * @return the correlationId
   */
  public UUID getCorrId() {
    return corrId;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getTechnicalName() + " with message " + getMessage() + " " + getCorrId();
  }

  private final void error(final Logger logger, final String message) {
    if (logger != null) {
      logger.error(message);
    } else {
      log.error(message);
    }
  }

  private final void error(final Logger logger, final String message, final Throwable throwable) {
    if (logger != null) {
      logger.error(message, throwable);
    } else {
      log.error(message, throwable);
    }
  }

  /**
   * Guaranteed to be stable for equal instances.
   * @return a stable number which should be reasonably well distributed.
   *
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    return corrId.hashCode();
  }

  /**
   * Instances must represent the same underlying problem (ie have the same correlation id).
   * @return true for instances with the same correlation id.
   */
  @Override
  public boolean equals(final Object other) {

    if (other == this) {
      return true;
    }
    if (other == null || !(other instanceof ChallengeException)) {
      return false;
    }
    final ChallengeException o = (ChallengeException) other;
    return o.corrId.equals(this.corrId) && o.getTechnicalName().equals(this.getTechnicalName());
  }
}
