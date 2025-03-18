import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProductService } from 'app/product/product.service';
import { ProductDTO } from 'app/product/product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-product-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './product-add.component.html'
})
export class ProductAddComponent implements OnInit {

  productService = inject(ProductService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  categorieValues?: Map<number,string>;
  sellerValues?: Map<number,string>;

  addForm = new FormGroup({
    libelle: new FormControl(null, [Validators.maxLength(255)]),
    stock: new FormControl(null),
    categorie: new FormControl(null),
    seller: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@product.create.success:Product was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.productService.getCategorieValues()
        .subscribe({
          next: (data) => this.categorieValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.productService.getSellerValues()
        .subscribe({
          next: (data) => this.sellerValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ProductDTO(this.addForm.value);
    this.productService.createProduct(data)
        .subscribe({
          next: () => this.router.navigate(['/products'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
