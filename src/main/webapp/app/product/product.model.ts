export class ProductDTO {

  constructor(data:Partial<ProductDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  libelle?: string|null;
  stock?: number|null;
  categorie?: number|null;
  seller?: number|null;

}
