package com.n26.challenge.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.exceptions.ChallengeException;
import com.n26.challenge.exceptions.ImpossibleFutureTransactionException;
import com.n26.challenge.exceptions.TransactionTooOldException;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.service.StatisticsService;
import com.n26.challenge.service.TransactionService;

/**
 * REST entry point for the challenge methods . The main use case for our API is to calculate
 * realtime statistic from the last 60 seconds. Our API contains two methods, one of them is called
 * every time a transaction is made. It is also the sole input of this rest API. The other one
 * returns the statistic based of the transactions of the last 60 seconds.
 */
@RestController
@RequestMapping(value = "api/v1/*")
public class ChallengeRestControllerPublic {

  private static final Logger log = LoggerFactory.getLogger(ChallengeRestControllerPublic.class);

  @Autowired
  protected TransactionService transactionService;
  @Autowired
  protected StatisticsService statisticsService;



  /**
   * Every Time a new transaction happened, this endpoint will be called.
   *
   * @param transaction the transaction details as a body in a {@link Transaction} form. Important
   *        to know that UTC is used as time stamp
   * @return Empty body with either 201 or 204. 201 - in case of success. 204 - if transaction is
   *         older than 60 seconds
   * @throws ChallengeException if the transaction is older that 60 seconds or if it is in the
   *         future.
   *
   */
  @RequestMapping(value = "/transactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @ApiOperation(value = "createTransaction", nickname = "createTransaction")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "in case of success"),
      @ApiResponse(code = 204, message = "if the transaction is older than 60 seconds"),
      @ApiResponse(code = 400, message = "if the body model is wrongly formatted"),
      @ApiResponse(code = 406, message = "if transaction is in the future"),
      @ApiResponse(code = 409, message = "in case of an unknown exception")})
  public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction)
      throws ChallengeException {
    log.info("adding the transaction {}", transaction);
    transactionService.addTransaction(transaction);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }



  /**
   * This endpoint returns the statistic based on the transactions which happened in the last 60
   * seconds.
   *
   * @return a {@link Statistics} object containing the statistic based on the transactions which
   *         happened in the last 60 seconds.
   *
   */
  @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json ;charset=utf-8")
  @ResponseBody
  @ApiOperation(value = "getStatistics", nickname = "getStatistics")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "in case of success", response = Statistics.class),
      @ApiResponse(code = 409, message = "in case of an unknown exception")})
  public Statistics getStatistics() {
    log.debug("getting statistics at {}", Instant.now().toString());
    return statisticsService.getStatistics();
  }

  /**
   * Method to handle a TransactionTooOldException. HTTP status 204.
   *
   * @param exception the {@link TransactionTooOldException} to handle
   * @return an empty body
   */
  @ExceptionHandler(TransactionTooOldException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void handleTransactionTooOldExceptionException(final TransactionTooOldException exception) {}

  /**
   * Method to handle a ImpossibleFutureTransactionException. HTTP status 406.
   *
   * @param exception the {@link ImpossibleFutureTransactionException} to handle
   * @throws IOException not supposed to occur
   */
  @ExceptionHandler(ImpossibleFutureTransactionException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public void handleImpossibleFutureTransactionExceptionException(
      final ImpossibleFutureTransactionException exception, HttpServletResponse response)
      throws IOException {
    log.warn(
        "The user is trying to save a transaction that happened in the future, weird behaviour",
        exception);
    response.sendError(HttpStatus.NOT_ACCEPTABLE.value(),
        "The user is trying to save a transaction that happened in the future, weird behaviour");
  }

  /**
   * Method to handle a HttpMessageNotReadableException. HTTP status 400.
   *
   * @param exception the {@link HttpMessageNotReadableException} to handle
   * @throws IOException not supposed to occur
   */
  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public void handleHttpMessageNotReadableExceptionException(
      final HttpMessageNotReadableException exception, HttpServletResponse response)
      throws IOException {
    log.debug("User sent wrong data ", exception);
    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please check the request body model");
  }

  /**
   * Method to handle a all sort of unexpected Exceptions. HTTP status 409.
   *
   * @param exception the {@link Exception} to handle
   * @throws IOException not supposed to occur
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public void defaultErrorHandler(final Exception exception, HttpServletResponse response)
      throws IOException {
    log.error("An unexpected exception just eccured ", exception);
    response.sendError(HttpStatus.CONFLICT.value(), "An unknown exception just eccured ");
  }


}
