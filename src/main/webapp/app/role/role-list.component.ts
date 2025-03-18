import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { RoleService } from 'app/role/role.service';
import { RoleDTO } from 'app/role/role.model';


@Component({
  selector: 'app-role-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './role-list.component.html'})
export class RoleListComponent implements OnInit, OnDestroy {

  roleService = inject(RoleService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  roles?: RoleDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@role.delete.success:Role was removed successfully.`    };
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
    this.roleService.getAllRoles()
        .subscribe({
          next: (data) => this.roles = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.roleService.deleteRole(id)
        .subscribe({
          next: () => this.router.navigate(['/roles'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
