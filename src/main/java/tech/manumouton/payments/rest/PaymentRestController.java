package tech.manumouton.payments.rest;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tech.manumouton.payments.model.Payment;
import tech.manumouton.payments.services.ErrorService;
import tech.manumouton.payments.services.PaymentService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/v1", produces = "application/json")
@Api(value = "payments", description = "Payments RESTFul API")
@Slf4j
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private PaymentResourceAssembler paymentResourceAssembler;

    @GetMapping("/payments")
    @ApiOperation(value = "View a list of all available payments", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation")
    })
    public ResponseEntity<Resources<Resource<Payment>>> getAllPayments() {
        List<Resource<Payment>> payments = paymentService.findAllPayments()
                .stream()
                .map(paymentResourceAssembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new Resources<>(
                        payments,
                        linkTo(methodOn(PaymentRestController.class).getAllPayments()).withSelfRel()
                )
        );
    }

    @PostMapping("/payments")
    @ApiOperation(value = "Create a payment")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 415, message = "Unsupported media type")
    })
    public ResponseEntity<?> createPayment(@Valid @RequestBody Payment payment, @ApiIgnore Errors errors) throws URISyntaxException {
        if (errors.hasErrors()) return handleValidationErrors(errors);

        Resource<Payment> resource = paymentResourceAssembler.toResource(paymentService.save(payment));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/payments/{id}")
    @ApiOperation(value = "Get a payment via its ID", response = Payment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public ResponseEntity<Resource<Payment>> getPayment(@PathVariable ObjectId id) {
        return paymentService
                .findById(id)
                .map(paymentResourceAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/payments/{id}")
    @ApiOperation(value = "Update a payment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 415, message = "Unsupported media type")
    })
    public ResponseEntity<?> updatePayment(@Valid @RequestBody Payment payment, @PathVariable ObjectId id) {
        return paymentService
                .findById(id)
                .map(p -> {
                    payment.setId(id);
                    return paymentService.save(payment);
                })
                .map(paymentResourceAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/payments/{id}")
    @ApiOperation(value = "Delete a payment via its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not found")
    })
    public ResponseEntity<?> deletePayment(@PathVariable ObjectId id) {
        if (!paymentService.findById(id).isPresent()) return ResponseEntity.notFound().build();

        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> handleValidationErrors(Errors errors) {
        return ResponseEntity.badRequest().body(errorService.buildBodyResponse(errors));
    }

}
