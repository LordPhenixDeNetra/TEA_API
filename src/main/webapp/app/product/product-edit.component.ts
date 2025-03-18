import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProductService } from 'app/product/product.service';
import { ProductDTO } from 'app/product/product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-product-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './product-edit.component.html'
})
export class ProductEditComponent implements OnInit {

  productService = inject(ProductService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  categorieValues?: Map<number,string>;
  sellerValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    libelle: new FormControl(null, [Validators.maxLength(255)]),
    stock: new FormControl(null),
    categorie: new FormControl(null),
    seller: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@product.update.success:Product was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.productService.getProduct(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new ProductDTO(this.editForm.value);
    this.productService.updateProduct(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/products'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
