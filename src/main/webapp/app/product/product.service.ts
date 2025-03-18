import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ProductDTO } from 'app/product/product.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ProductService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/products';

  getAllProducts() {
    return this.http.get<ProductDTO[]>(this.resourcePath);
  }

  getProduct(id: number) {
    return this.http.get<ProductDTO>(this.resourcePath + '/' + id);
  }

  createProduct(productDTO: ProductDTO) {
    return this.http.post<number>(this.resourcePath, productDTO);
  }

  updateProduct(id: number, productDTO: ProductDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, productDTO);
  }

  deleteProduct(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getCategorieValues() {
    return this.http.get<Record<string, number>>(this.resourcePath + '/categorieValues')
        .pipe(map(transformRecordToMap));
  }

  getSellerValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/sellerValues')
        .pipe(map(transformRecordToMap));
  }

}
