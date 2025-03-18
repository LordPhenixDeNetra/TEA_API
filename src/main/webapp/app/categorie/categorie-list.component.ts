import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CategorieService } from 'app/categorie/categorie.service';
import { CategorieDTO } from 'app/categorie/categorie.model';


@Component({
  selector: 'app-categorie-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './categorie-list.component.html'})
export class CategorieListComponent implements OnInit, OnDestroy {

  categorieService = inject(CategorieService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  categories?: CategorieDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@categorie.delete.success:Categorie was removed successfully.`,
      'categorie.product.categorie.referenced': $localize`:@@categorie.product.categorie.referenced:This entity is still referenced by Product ${details?.id} via field Categorie.`
    };
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
    this.categorieService.getAllCategories()
        .subscribe({
          next: (data) => this.categories = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.categorieService.deleteCategorie(id)
        .subscribe({
          next: () => this.router.navigate(['/categories'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => {
            if (error.error?.code === 'REFERENCED') {
              const messageParts = error.error.message.split(',');
              this.router.navigate(['/categories'], {
                state: {
                  msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                }
              });
              return;
            }
            this.errorHandler.handleServerError(error.error)
          }
        });
  }

}
