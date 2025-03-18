package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.OderDetails;
import com.netradev.tout_est_africain.model.OderDetailsDTO;
import com.netradev.tout_est_africain.repos.OderDetailsRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OderDetailsService {

    private final OderDetailsRepository oderDetailsRepository;

    public OderDetailsService(final OderDetailsRepository oderDetailsRepository) {
        this.oderDetailsRepository = oderDetailsRepository;
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
