export class OderDetailsDTO {

  constructor(data:Partial<OderDetailsDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  productId?: number|null;
  oderId?: number|null;
  quantity?: number|null;
  sellerId?: number|null;
  deliveryPersonId?: number|null;
  buyerId?: number|null;

}
