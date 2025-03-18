import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ImageProductDTO } from 'app/image-product/image-product.model';


@Injectable({
  providedIn: 'root',
})
export class ImageProductService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/imageProducts';

  getAllImageProducts() {
    return this.http.get<ImageProductDTO[]>(this.resourcePath);
  }

  getImageProduct(id: number) {
    return this.http.get<ImageProductDTO>(this.resourcePath + '/' + id);
  }

  createImageProduct(imageProductDTO: ImageProductDTO) {
    return this.http.post<number>(this.resourcePath, imageProductDTO);
  }

  updateImageProduct(id: number, imageProductDTO: ImageProductDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, imageProductDTO);
  }

  deleteImageProduct(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
