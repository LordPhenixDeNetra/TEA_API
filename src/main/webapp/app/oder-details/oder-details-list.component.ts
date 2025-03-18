import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { OderDetailsService } from 'app/oder-details/oder-details.service';
import { OderDetailsDTO } from 'app/oder-details/oder-details.model';


@Component({
  selector: 'app-oder-details-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './oder-details-list.component.html'})
export class OderDetailsListComponent implements OnInit, OnDestroy {

  oderDetailsService = inject(OderDetailsService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  oderDetailses?: OderDetailsDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@oderDetails.delete.success:Oder Details was removed successfully.`    };
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
    this.oderDetailsService.getAllOderDetailses()
        .subscribe({
          next: (data) => this.oderDetailses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.oderDetailsService.deleteOderDetails(id)
        .subscribe({
          next: () => this.router.navigate(['/oderDetailss'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
