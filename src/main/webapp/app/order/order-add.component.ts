import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { OrderService } from 'app/order/order.service';
import { OrderDTO } from 'app/order/order.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-order-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './order-add.component.html'
})
export class OrderAddComponent implements OnInit {

  orderService = inject(OrderService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  buyerValues?: Map<number,string>;
  productsValues?: Map<number,string>;

  addForm = new FormGroup({
    uuid: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    buyer: new FormControl(null),
    products: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@order.create.success:Order was created successfully.`,
      ORDER_UUID_UNIQUE: $localize`:@@Exists.order.uuid:This Uuid is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new OrderDTO(this.addForm.value);
    this.orderService.createOrder(data)
        .subscribe({
          next: () => this.router.navigate(['/orders'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
