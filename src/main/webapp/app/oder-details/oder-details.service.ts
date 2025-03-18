import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { OderDetailsDTO } from 'app/oder-details/oder-details.model';


@Injectable({
  providedIn: 'root',
})
export class OderDetailsService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/oderDetailss';

  getAllOderDetailses() {
    return this.http.get<OderDetailsDTO[]>(this.resourcePath);
  }

  getOderDetails(id: number) {
    return this.http.get<OderDetailsDTO>(this.resourcePath + '/' + id);
  }

  createOderDetails(oderDetailsDTO: OderDetailsDTO) {
    return this.http.post<number>(this.resourcePath, oderDetailsDTO);
  }

  updateOderDetails(id: number, oderDetailsDTO: OderDetailsDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, oderDetailsDTO);
  }

  deleteOderDetails(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
