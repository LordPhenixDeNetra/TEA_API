export class ImageProductDTO {

  constructor(data:Partial<ImageProductDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  productId?: number|null;
  image?: string|null;

}
