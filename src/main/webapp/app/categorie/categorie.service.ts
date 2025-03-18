import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CategorieDTO } from 'app/categorie/categorie.model';


@Injectable({
  providedIn: 'root',
})
export class CategorieService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/categories';

  getAllCategories() {
    return this.http.get<CategorieDTO[]>(this.resourcePath);
  }

  getCategorie(id: number) {
    return this.http.get<CategorieDTO>(this.resourcePath + '/' + id);
  }

  createCategorie(categorieDTO: CategorieDTO) {
    return this.http.post<number>(this.resourcePath, categorieDTO);
  }

  updateCategorie(id: number, categorieDTO: CategorieDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, categorieDTO);
  }

  deleteCategorie(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
