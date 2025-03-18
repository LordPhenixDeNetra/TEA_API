export class OrderDTO {

  constructor(data:Partial<OrderDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  uuid?: string|null;
  buyer?: number|null;
  products?: number[]|null;

}
