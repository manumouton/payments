package tech.manumouton.payments.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.manumouton.payments.model.Payment;
import tech.manumouton.payments.repositories.PaymentRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> findAllPayments(){
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(ObjectId id){
        return paymentRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        paymentRepository.deleteById(id);
    }

    public Payment save(@Valid Payment payment){
        return paymentRepository.save(payment);
    }

}
