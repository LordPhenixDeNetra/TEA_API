export class UserDTO {

  constructor(data:Partial<UserDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  uuid?: string|null;
  firstname?: string|null;
  lastname?: string|null;
  email?: string|null;
  password?: string|null;
  telephone?: string|null;
  roles?: number[]|null;

}
