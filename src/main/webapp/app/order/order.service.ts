import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { OrderDTO } from 'app/order/order.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class OrderService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/orders';

  getAllOrders() {
    return this.http.get<OrderDTO[]>(this.resourcePath);
  }

  getOrder(id: number) {
    return this.http.get<OrderDTO>(this.resourcePath + '/' + id);
  }

  createOrder(orderDTO: OrderDTO) {
    return this.http.post<number>(this.resourcePath, orderDTO);
  }

  updateOrder(id: number, orderDTO: OrderDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, orderDTO);
  }

  deleteOrder(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getBuyerValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/buyerValues')
        .pipe(map(transformRecordToMap));
  }

  getProductsValues() {
    return this.http.get<Record<string, number>>(this.resourcePath + '/productsValues')
        .pipe(map(transformRecordToMap));
  }

}
