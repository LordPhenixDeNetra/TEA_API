import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { RoleDTO } from 'app/role/role.model';


@Injectable({
  providedIn: 'root',
})
export class RoleService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/roles';

  getAllRoles() {
    return this.http.get<RoleDTO[]>(this.resourcePath);
  }

  getRole(id: number) {
    return this.http.get<RoleDTO>(this.resourcePath + '/' + id);
  }

  createRole(roleDTO: RoleDTO) {
    return this.http.post<number>(this.resourcePath, roleDTO);
  }

  updateRole(id: number, roleDTO: RoleDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, roleDTO);
  }

  deleteRole(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
