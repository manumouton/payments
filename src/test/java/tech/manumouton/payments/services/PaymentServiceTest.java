package tech.manumouton.payments.services;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import tech.manumouton.payments.model.Payment;
import tech.manumouton.payments.repositories.PaymentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PaymentServiceTest {

    @TestConfiguration
    static class PaymentServiceTestContextConfiguration {
        @Bean
        public PaymentService paymentService() {
            return new PaymentService();
        }
    }

    private ObjectId objectId;

    @Before
    public void setUp() {
        this.objectId = generateObjectId();
        Payment payment1 = Payment.builder().id(objectId).organisationId("testFindPayment").build();
        Payment payment2 = Payment.builder().id(new ObjectId()).organisationId("newSavedPayment").build();

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment1, payment2));
        when(paymentRepository.findById(objectId)).thenReturn(Optional.of(payment1));
        when(paymentRepository.save(payment1)).thenReturn(payment1);
    }

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    public void findAllPayments() {
        List<Payment> allPayments = paymentService.findAllPayments();

        verify(paymentRepository, times(1)).findAll();

        assertNotNull(allPayments);
        assertFalse(allPayments.isEmpty());
        assertEquals(allPayments.size(), 2);
    }

    @Test
    public void findById_whenIdExists() {
        Optional<Payment> payment = paymentService.findById(objectId);

        verify(paymentRepository, times(1)).findById(objectId);

        assertNotNull(payment);
        assertTrue(payment.isPresent());
        assertEquals(payment.get().getId(), objectId);
        assertEquals(payment.get().getOrganisationId(), "testFindPayment");
    }

    @Test
    public void findById_whenIdDoesNotExist() {
        ObjectId testObjectId = generateObjectId();
        Optional<Payment> payment = paymentService.findById(testObjectId);

        verify(paymentRepository, times(1)).findById(testObjectId);

        assertNotNull(payment);
        assertFalse(payment.isPresent());
    }

    @Test
    public void save_whenPaymentExists() {
        Optional<Payment> payment = paymentService.findById(objectId);
        assertNotNull(payment);
        assertTrue(payment.isPresent());

        Payment p = payment.get();
        String updatedOrganisationId = "testUpdatedOrganisationId";
        p.setOrganisationId(updatedOrganisationId);

        Payment savedPayment = paymentService.save(p);
        assertNotNull(savedPayment);

        verify(paymentRepository, times(1)).save(savedPayment);

        Optional<Payment> updatedPayment = paymentService.findById(objectId);
        assertNotNull(updatedPayment);
        assertTrue(updatedPayment.isPresent());

        assertEquals(updatedOrganisationId, updatedPayment.get().getOrganisationId());
        assertEquals(savedPayment.getOrganisationId(), updatedPayment.get().getOrganisationId());
    }

    @Test
    public void deleteById() {
        Optional<Payment> payment = paymentService.findById(objectId);
        assertNotNull(payment);
        assertTrue(payment.isPresent());

        paymentService.deleteById(objectId);

        verify(paymentRepository, times(1)).deleteById(objectId);
    }

    private ObjectId generateObjectId(){
        return new ObjectId();
    }
}
