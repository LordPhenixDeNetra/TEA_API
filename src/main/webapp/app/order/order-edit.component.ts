import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { OrderService } from 'app/order/order.service';
import { OrderDTO } from 'app/order/order.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-order-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './order-edit.component.html'
})
export class OrderEditComponent implements OnInit {

  orderService = inject(OrderService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  buyerValues?: Map<number,string>;
  productsValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    uuid: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    buyer: new FormControl(null),
    products: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@order.update.success:Order was updated successfully.`,
      ORDER_UUID_UNIQUE: $localize`:@@Exists.order.uuid:This Uuid is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.orderService.getBuyerValues()
        .subscribe({
          next: (data) => this.buyerValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.orderService.getProductsValues()
        .subscribe({
          next: (data) => this.productsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.orderService.getOrder(this.currentId!)
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
    const data = new OrderDTO(this.editForm.value);
    this.orderService.updateOrder(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/orders'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
