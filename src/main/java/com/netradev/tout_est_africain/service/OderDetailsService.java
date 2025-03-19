package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.OderDetails;
import com.netradev.tout_est_africain.model.OderDetailsDTO;
import com.netradev.tout_est_africain.model.OrderDTO;
import com.netradev.tout_est_africain.repos.OderDetailsRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.util.NotFoundException;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OderDetailsService {

    private final OderDetailsRepository oderDetailsRepository;
    private final ProductRepository productRepository;

    public OderDetailsService(final OderDetailsRepository oderDetailsRepository, final ProductRepository productRepository) {
        this.oderDetailsRepository = oderDetailsRepository;
        this.productRepository = productRepository;
    }

    public List<OderDetailsDTO> findAll() {
        final List<OderDetails> oderDetailses = oderDetailsRepository.findAll(Sort.by("id"));
        return oderDetailses.stream()
                .map(oderDetails -> mapToDTO(oderDetails, new OderDetailsDTO()))
                .toList();
    }

    public OderDetailsDTO get(final Long id) {
        return oderDetailsRepository.findById(id)
                .map(oderDetails -> mapToDTO(oderDetails, new OderDetailsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OderDetailsDTO oderDetailsDTO) {
        final OderDetails oderDetails = new OderDetails();
        mapToEntity(oderDetailsDTO, oderDetails);
        return oderDetailsRepository.save(oderDetails).getId();
    }

    public void update(final Long id, final OderDetailsDTO oderDetailsDTO) {
        final OderDetails oderDetails = oderDetailsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(oderDetailsDTO, oderDetails);
        oderDetailsRepository.save(oderDetails);
    }

    public List<OderDetailsDTO> findByOrderId(Long orderId) {
        List<OderDetails> details = oderDetailsRepository.findByOderId(orderId);
        return details.stream()
            .map(detail -> mapToDTO(detail, new OderDetailsDTO()))
            .toList();
    }

    /**
     * Calcule le montant total d'une commande en additionnant le prix de chaque article.
     *
     * @param orderId L'ID de la commande dont on veut calculer le total
     * @return Le montant total de la commande
     */
    public BigDecimal calculateOrderTotal(Long orderId) {
        List<OderDetails> details = oderDetailsRepository.findByOderId(orderId);

        return details.stream()
            .map(detail -> detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

//    public OrderDTO getOrderWithDetails(Long id) {
//        OrderDTO orderDTO = get(id);
//        orderDTO.setOrderDetails(oderDetailsService.findByOrderId(id));
//        return orderDTO;
//    }

    public void delete(final Long id) {
        oderDetailsRepository.deleteById(id);
    }

    private OderDetailsDTO mapToDTO(final OderDetails oderDetails,
            final OderDetailsDTO oderDetailsDTO) {
        oderDetailsDTO.setId(oderDetails.getId());
        oderDetailsDTO.setProductId(oderDetails.getProductId());
        oderDetailsDTO.setOderId(oderDetails.getOderId());
        oderDetailsDTO.setQuantity(oderDetails.getQuantity());
        oderDetailsDTO.setSellerId(oderDetails.getSellerId());
        oderDetailsDTO.setDeliveryPersonId(oderDetails.getDeliveryPersonId());
        oderDetailsDTO.setBuyerId(oderDetails.getBuyerId());
        oderDetailsDTO.setUnitPrice(oderDetails.getUnitPrice());
        if (productRepository != null) {
            productRepository.findById(oderDetails.getProductId())
                .ifPresent(product -> oderDetailsDTO.setProductName(product.getLibelle()));
        }
        return oderDetailsDTO;
    }

    private OderDetails mapToEntity(final OderDetailsDTO oderDetailsDTO,
            final OderDetails oderDetails) {
        oderDetails.setProductId(oderDetailsDTO.getProductId());
        oderDetails.setOderId(oderDetailsDTO.getOderId());
        oderDetails.setQuantity(oderDetailsDTO.getQuantity());
        oderDetails.setSellerId(oderDetailsDTO.getSellerId());
        oderDetails.setDeliveryPersonId(oderDetailsDTO.getDeliveryPersonId());
        oderDetails.setBuyerId(oderDetailsDTO.getBuyerId());
        return oderDetails;
    }

}
