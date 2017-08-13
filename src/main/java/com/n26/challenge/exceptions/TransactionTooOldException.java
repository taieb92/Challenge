package com.n26.challenge.exceptions;

import org.slf4j.Logger;

/**
 * Exception to be thrown in case of an internal error which is not covered by any of the other
 * exception types.
 */
public class TransactionTooOldException extends ChallengeException {

  private static final long serialVersionUID = 7344741297265420723L;

  private final Name technicalName = Name.TOO_OLD_EXCEPTION;

  /**
   * Creates and logs an exception. Message must be provided, logger should be provided.
   *
   * @param message which initializes the exception, has to be provided
   * @param logger the logger of the instance that threw this exception, can be null but then error
   *        traceability is not ensured.
   */
  public TransactionTooOldException(final String message, final Logger logger) {
    super(message, logger);
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
  public TransactionTooOldException(final String message, final Logger logger, final Exception rootException) {
    super(message, logger, rootException);
  }


  @Override
  public String getTechnicalName() {
    return technicalName.toString();
  }

}
