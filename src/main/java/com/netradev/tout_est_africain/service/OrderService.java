package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Order;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.OderDetailsDTO;
import com.netradev.tout_est_africain.model.OderStatus;
import com.netradev.tout_est_africain.model.OrderDTO;
import com.netradev.tout_est_africain.repos.OderDetailsRepository;
import com.netradev.tout_est_africain.repos.OrderRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OderDetailsService oderDetailsService;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(final OrderRepository orderRepository, final UserRepository userRepository,
            final ProductRepository productRepository,  final OderDetailsService oderDetailsService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.oderDetailsService = oderDetailsService;
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

    public OrderDTO getOrderWithDetails(Long id) {
        OrderDTO orderDTO = get(id);
        orderDTO.setOrderDetails(oderDetailsService.findByOrderId(id));
        return orderDTO;
    }

    /**
     * Crée une commande avec ses détails en une seule transaction.
     * Vérifie la disponibilité des produits, met à jour les stocks,
     * et enregistre le prix unitaire au moment de la commande.
     *
     * @param orderDTO Le DTO contenant les informations de base de la commande
     * @param details La liste des détails de commande à associer
     * @return L'ID de la commande créée
     * @throws NotFoundException Si un produit, acheteur ou vendeur n'est pas trouvé
     * @throws IllegalStateException Si le stock d'un produit est insuffisant
     */
    @Transactional
    public Long createOrderWithDetails(OrderDTO orderDTO, List<OderDetailsDTO> details) {
        // Vérifier que l'acheteur existe
        User buyer = userRepository.findById(orderDTO.getBuyer())
            .orElseThrow(() -> new NotFoundException("Acheteur non trouvé avec l'ID: " + orderDTO.getBuyer()));

        // Créer la commande principale
        final Order order = new Order();
        mapToEntity(orderDTO, order);

        // Définir le statut initial à PENDING si pas déjà défini
        if (order.getStatus() == null) {
            order.setStatus(OderStatus.PENDING);
        }

        // Sauvegarder la commande pour obtenir son ID
        final Long orderId = orderRepository.save(order).getId();

        // Traiter chaque détail de commande
        for (OderDetailsDTO detailDTO : details) {
            // Récupérer le produit
            Product product = productRepository.findById(detailDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Produit non trouvé avec l'ID: " + detailDTO.getProductId()));

            // Vérifier le stock disponible
            if (product.getStock() < detailDTO.getQuantity()) {
                throw new IllegalStateException("Stock insuffisant pour le produit: " + product.getLibelle()
                    + ". Disponible: " + product.getStock() + ", Demandé: " + detailDTO.getQuantity());
            }

            // Mettre à jour le stock du produit
            product.setStock(product.getStock() - detailDTO.getQuantity().intValue());
            productRepository.save(product);

            // Récupérer le vendeur
            User seller = product.getSeller();
            if (seller == null) {
                throw new NotFoundException("Vendeur non trouvé pour le produit: " + product.getLibelle());
            }

            // Configurer le détail de commande
            detailDTO.setOderId(orderId);
            detailDTO.setBuyerId(buyer.getId());
            detailDTO.setSellerId(seller.getId());

            // Si vous avez ajouté un champ unitPrice dans OderDetailsDTO et OderDetails
            if (product.getPrice() != null) {
                detailDTO.setUnitPrice(product.getPrice());
            }

            // Si vous avez besoin de gérer un livreur, vous pouvez l'assigner ici ou le laisser null
            // detailDTO.setDeliveryPersonId(...);

            // Créer le détail de commande
            oderDetailsService.create(detailDTO);
        }

        // Loguer la création de la commande
        log.info("Commande #{} créée avec succès avec {} articles pour l'acheteur #{}",
            orderId, details.size(), buyer.getId());

        return orderId;
    }




    public void updateOrderStatus(Long id, OderStatus newStatus) {
        Order order = orderRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    private void validateStatusTransition(OderStatus currentStatus, OderStatus newStatus) {
        // Règles de transition
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OderStatus.CONFIRMED && newStatus != OderStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from PENDING to " + newStatus);
                }
                break;
            case CONFIRMED:
                if (newStatus != OderStatus.SHIPPED && newStatus != OderStatus.CANCELLED) {
                    throw new IllegalStateException("Invalid status transition from CONFIRMED to " + newStatus);
                }
                break;
            case SHIPPED:
                if (newStatus != OderStatus.DELIVERED && newStatus != OderStatus.RETURNED) {
                    throw new IllegalStateException("Invalid status transition from SHIPPED to " + newStatus);
                }
                break;
            case DELIVERED:
                if (newStatus != OderStatus.RETURNED) {
                    throw new IllegalStateException("Invalid status transition from DELIVERED to " + newStatus);
                }
                break;
            case CANCELLED:
            case RETURNED:
                throw new IllegalStateException("Cannot change status from " + currentStatus);
        }
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
        orderDTO.setStatus(order.getStatus());

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
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }
        return order;
    }

    public List<OrderDTO> findByStatus(OderStatus status) {
        final List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
            .map(order -> mapToDTO(order, new OrderDTO()))
            .toList();
    }

    public List<OrderDTO> findByBuyerAndStatus(Long buyerId, OderStatus status) {
        User buyer = userRepository.findById(buyerId)
            .orElseThrow(NotFoundException::new);
        final List<Order> orders = orderRepository.findByBuyerAndStatus(buyer, status);
        return orders.stream()
            .map(order -> mapToDTO(order, new OrderDTO()))
            .toList();
    }

    /**
     * Calcule le total d'une commande
     */
    public BigDecimal getOrderTotal(Long orderId) {
        // Vérifier que la commande existe
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException("Commande non trouvée avec l'ID: " + orderId);
        }

        return oderDetailsService.calculateOrderTotal(orderId);
    }

    public boolean uuidExists(final String uuid) {
        return orderRepository.existsByUuidIgnoreCase(uuid);
    }

}
