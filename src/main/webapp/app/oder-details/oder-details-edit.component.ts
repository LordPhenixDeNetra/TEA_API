import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { OderDetailsService } from 'app/oder-details/oder-details.service';
import { OderDetailsDTO } from 'app/oder-details/oder-details.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-oder-details-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './oder-details-edit.component.html'
})
export class OderDetailsEditComponent implements OnInit {

  oderDetailsService = inject(OderDetailsService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    productId: new FormControl(null, [Validators.required]),
    oderId: new FormControl(null, [Validators.required]),
    quantity: new FormControl(null, [Validators.required]),
    sellerId: new FormControl(null, [Validators.required]),
    deliveryPersonId: new FormControl(null),
    buyerId: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@oderDetails.update.success:Oder Details was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.oderDetailsService.getOderDetails(this.currentId!)
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
    const data = new OderDetailsDTO(this.editForm.value);
    this.oderDetailsService.updateOderDetails(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/oderDetailss'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
