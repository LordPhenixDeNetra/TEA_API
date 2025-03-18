import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { OderDetailsService } from 'app/oder-details/oder-details.service';
import { OderDetailsDTO } from 'app/oder-details/oder-details.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-oder-details-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './oder-details-add.component.html'
})
export class OderDetailsAddComponent {

  oderDetailsService = inject(OderDetailsService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    productId: new FormControl(null, [Validators.required]),
    oderId: new FormControl(null, [Validators.required]),
    quantity: new FormControl(null, [Validators.required]),
    sellerId: new FormControl(null, [Validators.required]),
    deliveryPersonId: new FormControl(null),
    buyerId: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@oderDetails.create.success:Oder Details was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new OderDetailsDTO(this.addForm.value);
    this.oderDetailsService.createOderDetails(data)
        .subscribe({
          next: () => this.router.navigate(['/oderDetailss'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
