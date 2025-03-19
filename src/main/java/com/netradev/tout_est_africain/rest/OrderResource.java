package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.OderStatus;
import com.netradev.tout_est_africain.model.OrderDTO;
import com.netradev.tout_est_africain.model.OrderWithDetailsRequest;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.service.OrderService;
import com.netradev.tout_est_africain.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class OrderResource {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderResource(final OrderService orderService, final UserRepository userRepository,
            final ProductRepository productRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        final Long createdId = orderService.create(orderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOrder(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OrderDTO orderDTO) {
        orderService.update(id, orderDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") final Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buyerValues")
    public ResponseEntity<Map<Long, String>> getBuyerValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUuid)));
    }

    @GetMapping("/productsValues")
    public ResponseEntity<Map<Long, Long>> getProductsValues() {
        return ResponseEntity.ok(productRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Product::getId, Product::getId)));
    }



    @GetMapping("/{id}/with-details")
    public ResponseEntity<OrderDTO> getOrderWithDetails(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderWithDetails(id));
    }

    @PostMapping("/with-details")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOrderWithDetails(
        @RequestBody OrderWithDetailsRequest request) {
        Long id = orderService.createOrderWithDetails(request.getOrder(), request.getDetails());
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
        @PathVariable Long id,
        @RequestBody OderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> getOrderTotal(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(orderService.getOrderTotal(id));
    }

}
