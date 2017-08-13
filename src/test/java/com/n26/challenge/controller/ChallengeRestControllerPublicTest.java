package com.n26.challenge.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.challenge.exceptions.ImpossibleFutureTransactionException;
import com.n26.challenge.exceptions.TransactionTooOldException;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;
import com.n26.challenge.service.StatisticsService;
import com.n26.challenge.service.TransactionService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ChallengeRestControllerPublic.class)
public class ChallengeRestControllerPublicTest {


  private static final String REQUEST_MAPPING_BASE = "/api/v1";


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionService transactionService;

  @MockBean
  private StatisticsService statisticsService;



  @Test
  public void testValidTransactionRequest() throws Exception {

    mockMvc.perform(
        post(REQUEST_MAPPING_BASE + "/transactions").content(asJsonString(new Transaction()))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().isCreated());

  }

  @Test
  public void testTooOldTransactionRequest() throws Exception {
    doThrow(TransactionTooOldException.class).when(transactionService).addTransaction(
        any(Transaction.class));
    mockMvc.perform(
        post(REQUEST_MAPPING_BASE + "/transactions").content(asJsonString(new Transaction()))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().isNoContent());

  }

  @Test
  public void testFutureTransactionRequest() throws Exception {
    doThrow(ImpossibleFutureTransactionException.class).when(transactionService).addTransaction(
        any(Transaction.class));
    mockMvc.perform(
        post(REQUEST_MAPPING_BASE + "/transactions").content(asJsonString(new Transaction()))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().isNotAcceptable());

  }

  @Test
  public void testMalFormattedTransactionRequest() throws Exception {
    doThrow(HttpMessageNotReadableException.class).when(transactionService).addTransaction(
        any(Transaction.class));
    mockMvc.perform(
        post(REQUEST_MAPPING_BASE + "/transactions").content(asJsonString(new Transaction()))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().isBadRequest());

  }


  @Test
  public void testTransactionRequestUnknownExceptionHandeling() throws Exception {
    doThrow(Exception.class).when(transactionService).addTransaction(any(Transaction.class));
    mockMvc.perform(
        post(REQUEST_MAPPING_BASE + "/transactions").content(asJsonString(new Transaction()))
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(
        status().isConflict());

  }


  @Test
  public void testStatisticsRequest() throws Exception {

    Statistics stats = new Statistics(25, 15, 10, 2);
    when(statisticsService.getStatistics()).thenReturn(stats);
    mockMvc.perform(get(REQUEST_MAPPING_BASE + "/statistics").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.sum").value(25))
        .andExpect(jsonPath("$.avg").value(12.5)).andExpect(jsonPath("$.max").value(15))
        .andExpect(jsonPath("$.min").value(10)).andExpect(jsonPath("$.count").value(2));
  }

  @Test
  public void testStatisticsRequestInit() throws Exception {

    Statistics stats = new Statistics();
    when(statisticsService.getStatistics()).thenReturn(stats);
    mockMvc.perform(get(REQUEST_MAPPING_BASE + "/statistics").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.sum").value(0))
        .andExpect(jsonPath("$.avg").value(0)).andExpect(jsonPath("$.max").value(Double.NaN))
        .andExpect(jsonPath("$.min").value(Double.NaN)).andExpect(jsonPath("$.count").value(0));
  }

  public static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }



}
