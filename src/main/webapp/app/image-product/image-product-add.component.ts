import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ImageProductService } from 'app/image-product/image-product.service';
import { ImageProductDTO } from 'app/image-product/image-product.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-image-product-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './image-product-add.component.html'
})
export class ImageProductAddComponent {

  imageProductService = inject(ImageProductService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    productId: new FormControl(null),
    image: new FormControl(null, [Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@imageProduct.create.success:Image Product was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ImageProductDTO(this.addForm.value);
    this.imageProductService.createImageProduct(data)
        .subscribe({
          next: () => this.router.navigate(['/imageProducts'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
