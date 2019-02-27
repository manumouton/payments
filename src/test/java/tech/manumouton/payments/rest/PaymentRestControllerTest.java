package tech.manumouton.payments.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tech.manumouton.payments.model.Payment;
import tech.manumouton.payments.services.PaymentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.PropertyAccessor.CREATOR;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class PaymentRestControllerTest {

    final static String CORRECT_PAYMENT_FILE = "src/test/resources/inputSamples/payment.json";
    final static String INVALID_PAYMENT_FILE = "src/test/resources/inputSamples/invalid_payment.json";
    final static String EMPTY_PAYMENT_FILE = "src/test/resources/inputSamples/wrongInput.json";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void getAllPayments() throws Exception {
        Payment payment = mockPayment();
        List<Payment> allPayments = Arrays.asList(payment);

        when(paymentService.findAllPayments()).thenReturn(allPayments);

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        validatePaymentJsonContent(resultActions, payment, "_embedded.payments[0].");
    }

    @Test
    public void getPayment_whenExisting() throws Exception {
        Payment payment = mockfindById();

        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        validatePaymentJsonContent(resultActions, payment, "");

    }

    @Test
    public void getPayment_whenNotExisting() throws Exception {
        mockMvc.perform(
                get("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePayment_whenExisting() throws Exception {
        Payment payment = mockfindById();

        mockMvc.perform(
                delete("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePayment_whenNotExisting() throws Exception {
        mockMvc.perform(
                delete("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createPayment_whenSuccessfulInput() throws Exception {
        Payment payment = mockSave(mockPayment());

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getWorkingPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isCreated());

        validatePaymentJsonContent(resultActions, payment, "");

    }

    @Test
    public void createPayment_whenInvalidInput() throws Exception {
        mockMvc.perform(
                post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getInvalidPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(2));
    }


    @Test
    public void createPayment_whenWrongMediaTypeInput() throws Exception {
        Payment payment = mockSave(mockPayment());

        mockMvc.perform(
                post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(Files.readAllBytes(getWorkingPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void createPayment_whenWrongInput() throws Exception {
        Payment payment = mockSave(mockPayment());

        mockMvc.perform(
                post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getWrongInputJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePayment_whenSuccessfulInput() throws Exception {
        Payment payment = mockSave(mockfindById());

        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getWorkingPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isOk());

        validatePaymentJsonContent(resultActions, payment, "");
    }

    @Test
    public void updatePayment_whenInvalidInput() throws Exception {
        Payment payment = mockSave(mockfindById());

        mockMvc.perform(
                put("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getInvalidPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(2));
    }

    @Test
    public void updatePayment_whenNotExistingPayment() throws Exception {
        mockMvc.perform(
                put("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(getWorkingPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePayment_whenWrongMediaTypeInput() throws Exception {
        Payment payment = mockSave(mockfindById());

        mockMvc.perform(
                put("/api/v1/payments/507f1f77bcf86cd799439011")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(Files.readAllBytes(getWorkingPaymentJsonFile().toPath())))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    private Payment mockfindById() throws IOException {
        Payment payment = mockPayment();
        ObjectId id = new ObjectId("507f1f77bcf86cd799439011");
        when(paymentService.findById(id)).thenReturn(Optional.of(payment));
        return payment;
    }

    private Payment mockSave(Payment payment) {
        when(paymentService.save(payment)).thenReturn(payment);
        return payment;
    }

    private Payment mockPayment() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.setVisibility(FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(CREATOR, JsonAutoDetect.Visibility.ANY);
        return objectMapper.readValue(getWorkingPaymentJsonFile(), Payment.class);
    }

    private File getWorkingPaymentJsonFile(){
        String path = CORRECT_PAYMENT_FILE;
        return getPaymentJsonFile(path);
    }

    private File getInvalidPaymentJsonFile(){
        String path = INVALID_PAYMENT_FILE;
        return getPaymentJsonFile(path);
    }

    private File getWrongInputJsonFile(){
        String path = EMPTY_PAYMENT_FILE;
        return getPaymentJsonFile(path);
    }

    private File getPaymentJsonFile(String path) {
        return new File(path);
    }

    private void validatePaymentJsonContent(ResultActions resultActions, Payment payment, String jsonPathPrefix) throws Exception {
        resultActions
                .andExpect(jsonPath("$." + jsonPathPrefix + "type").value(payment.getType()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "version").value(payment.getVersion()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "organisation_id").value(payment.getOrganisationId()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.amount").value(payment.getAttributes().getAmount()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.account_name").value(payment.getAttributes().getBeneficiaryParty().getAccountName()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.account_number").value(payment.getAttributes().getBeneficiaryParty().getAccountNumber()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.account_number_code").value(payment.getAttributes().getBeneficiaryParty().getAccountNumberCode()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.account_type").value(payment.getAttributes().getBeneficiaryParty().getAccountType()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.address").value(payment.getAttributes().getBeneficiaryParty().getAddress()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.bank_id").value(payment.getAttributes().getBeneficiaryParty().getBankId()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.bank_id_code").value(payment.getAttributes().getBeneficiaryParty().getBankIdCode()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.beneficiary_party.name").value(payment.getAttributes().getBeneficiaryParty().getName()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.bearer_code").value(payment.getAttributes().getChargesInformation().getBearerCode()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.sender_charges[0].amount").value(payment.getAttributes().getChargesInformation().getSenderCharges().get(0).getAmount()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.sender_charges[0].currency").value(payment.getAttributes().getChargesInformation().getSenderCharges().get(0).getCurrency()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.sender_charges[1].amount").value(payment.getAttributes().getChargesInformation().getSenderCharges().get(1).getAmount()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.sender_charges[1].currency").value(payment.getAttributes().getChargesInformation().getSenderCharges().get(1).getCurrency()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.receiver_charges[0].amount").value(payment.getAttributes().getChargesInformation().getReceiverCharges().get(0).getAmount()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.charges_information.receiver_charges[0].currency").value(payment.getAttributes().getChargesInformation().getReceiverCharges().get(0).getCurrency()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.currency").value(payment.getAttributes().getCurrency()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.account_name").value(payment.getAttributes().getDebtorParty().getAccountName()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.account_number").value(payment.getAttributes().getDebtorParty().getAccountNumber()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.account_number_code").value(payment.getAttributes().getDebtorParty().getAccountNumberCode()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.address").value(payment.getAttributes().getDebtorParty().getAddress()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.bank_id").value(payment.getAttributes().getDebtorParty().getBankId()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.bank_id_code").value(payment.getAttributes().getDebtorParty().getBankIdCode()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.debtor_party.name").value(payment.getAttributes().getDebtorParty().getName()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.end_to_end_reference").value(payment.getAttributes().getEndToEndReference()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.fx.contract_reference").value(payment.getAttributes().getFx().getContractReference()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.fx.exchange_rate").value(payment.getAttributes().getFx().getExchangeRate()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.fx.original_amount").value(payment.getAttributes().getFx().getOriginalAmount()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.fx.original_currency").value(payment.getAttributes().getFx().getOriginalCurrency()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.numeric_reference").value(payment.getAttributes().getNumericReference()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.payment_id").value(payment.getAttributes().getPaymentId()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.payment_purpose").value(payment.getAttributes().getPaymentPurpose()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.payment_scheme").value(payment.getAttributes().getPaymentScheme()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.payment_type").value(payment.getAttributes().getPaymentType()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.processing_date").value(new SimpleDateFormat("yyyy-MM-dd").format(payment.getAttributes().getProcessingDate())))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.reference").value(payment.getAttributes().getReference()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.scheme_payment_sub_type").value(payment.getAttributes().getSchemePaymentSubType()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.scheme_payment_type").value(payment.getAttributes().getSchemePaymentType()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.sponsor_party.account_number").value(payment.getAttributes().getSponsorParty().getAccountNumber()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.sponsor_party.bank_id").value(payment.getAttributes().getSponsorParty().getBankId()))
                .andExpect(jsonPath("$." + jsonPathPrefix + "attributes.sponsor_party.bank_id_code").value(payment.getAttributes().getSponsorParty().getBankIdCode()));
    }
}
