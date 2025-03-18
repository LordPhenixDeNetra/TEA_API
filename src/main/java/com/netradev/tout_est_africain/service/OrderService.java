package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Order;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.OrderDTO;
import com.netradev.tout_est_africain.repos.OrderRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(final OrderRepository orderRepository, final UserRepository userRepository,
            final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setUuid(order.getUuid());
        orderDTO.setBuyer(order.getBuyer() == null ? null : order.getBuyer().getId());
        orderDTO.setProducts(order.getProducts().stream()
                .map(product -> product.getId())
                .toList());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setUuid(orderDTO.getUuid());
        final User buyer = orderDTO.getBuyer() == null ? null : userRepository.findById(orderDTO.getBuyer())
                .orElseThrow(() -> new NotFoundException("buyer not found"));
        order.setBuyer(buyer);
        final List<Product> products = productRepository.findAllById(
                orderDTO.getProducts() == null ? Collections.emptyList() : orderDTO.getProducts());
        if (products.size() != (orderDTO.getProducts() == null ? 0 : orderDTO.getProducts().size())) {
            throw new NotFoundException("one of products not found");
        }
        order.setProducts(new HashSet<>(products));
        return order;
    }

    public boolean uuidExists(final String uuid) {
        return orderRepository.existsByUuidIgnoreCase(uuid);
    }

}
