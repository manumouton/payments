package tech.manumouton.payments.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import tech.manumouton.payments.model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, ObjectId> {

}
