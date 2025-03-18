import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ImageProductService } from 'app/image-product/image-product.service';
import { ImageProductDTO } from 'app/image-product/image-product.model';


@Component({
  selector: 'app-image-product-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './image-product-list.component.html'})
export class ImageProductListComponent implements OnInit, OnDestroy {

  imageProductService = inject(ImageProductService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  imageProducts?: ImageProductDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@imageProduct.delete.success:Image Product was removed successfully.`    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.imageProductService.getAllImageProducts()
        .subscribe({
          next: (data) => this.imageProducts = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.imageProductService.deleteImageProduct(id)
        .subscribe({
          next: () => this.router.navigate(['/imageProducts'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
