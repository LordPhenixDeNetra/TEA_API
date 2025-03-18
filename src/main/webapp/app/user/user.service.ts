import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { UserDTO } from 'app/user/user.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class UserService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/users';

  getAllUsers() {
    return this.http.get<UserDTO[]>(this.resourcePath);
  }

  getUser(id: number) {
    return this.http.get<UserDTO>(this.resourcePath + '/' + id);
  }

  createUser(userDTO: UserDTO) {
    return this.http.post<number>(this.resourcePath, userDTO);
  }

  updateUser(id: number, userDTO: UserDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, userDTO);
  }

  deleteUser(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getRolesValues() {
    return this.http.get<Record<string, number>>(this.resourcePath + '/rolesValues')
        .pipe(map(transformRecordToMap));
  }

}
