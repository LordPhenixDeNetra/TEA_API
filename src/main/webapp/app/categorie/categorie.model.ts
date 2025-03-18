export class CategorieDTO {

  constructor(data:Partial<CategorieDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  categorieName?: string|null;

}
