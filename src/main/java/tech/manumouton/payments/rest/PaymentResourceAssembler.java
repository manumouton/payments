package tech.manumouton.payments.rest;

import tech.manumouton.payments.model.Payment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PaymentResourceAssembler implements ResourceAssembler<Payment, Resource<Payment>> {

    @Override
    public Resource<Payment> toResource(Payment payment)

    {
        return new Resource<>(payment,
                linkTo(methodOn(PaymentRestController.class).getPayment(payment.getId())).withSelfRel(),
                linkTo(methodOn(PaymentRestController.class).getAllPayments()).withRel("payments"));
    }

}
