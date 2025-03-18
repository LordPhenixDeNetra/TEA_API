package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Categorie;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.model.CategorieDTO;
import com.netradev.tout_est_africain.repos.CategorieRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import com.netradev.tout_est_africain.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;
    private final ProductRepository productRepository;

    public CategorieService(final CategorieRepository categorieRepository,
            final ProductRepository productRepository) {
        this.categorieRepository = categorieRepository;
        this.productRepository = productRepository;
    }

    public List<CategorieDTO> findAll() {
        final List<Categorie> categories = categorieRepository.findAll(Sort.by("id"));
        return categories.stream()
                .map(categorie -> mapToDTO(categorie, new CategorieDTO()))
                .toList();
    }

    public CategorieDTO get(final Long id) {
        return categorieRepository.findById(id)
                .map(categorie -> mapToDTO(categorie, new CategorieDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategorieDTO categorieDTO) {
        final Categorie categorie = new Categorie();
        mapToEntity(categorieDTO, categorie);
        return categorieRepository.save(categorie).getId();
    }

    public void update(final Long id, final CategorieDTO categorieDTO) {
        final Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categorieDTO, categorie);
        categorieRepository.save(categorie);
    }

    public void delete(final Long id) {
        categorieRepository.deleteById(id);
    }

    private CategorieDTO mapToDTO(final Categorie categorie, final CategorieDTO categorieDTO) {
        categorieDTO.setId(categorie.getId());
        categorieDTO.setCategorieName(categorie.getCategorieName());
        return categorieDTO;
    }

    private Categorie mapToEntity(final CategorieDTO categorieDTO, final Categorie categorie) {
        categorie.setCategorieName(categorieDTO.getCategorieName());
        return categorie;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Product categorieProduct = productRepository.findFirstByCategorie(categorie);
        if (categorieProduct != null) {
            referencedWarning.setKey("categorie.product.categorie.referenced");
            referencedWarning.addParam(categorieProduct.getId());
            return referencedWarning;
        }
        return null;
    }

}
