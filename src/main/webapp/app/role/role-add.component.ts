import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { RoleService } from 'app/role/role.service';
import { RoleDTO } from 'app/role/role.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-role-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './role-add.component.html'
})
export class RoleAddComponent {

  roleService = inject(RoleService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    roleName: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@role.create.success:Role was created successfully.`,
      ROLE_ROLE_NAME_UNIQUE: $localize`:@@Exists.role.roleName:This Role Type is already taken.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new RoleDTO(this.addForm.value);
    this.roleService.createRole(data)
        .subscribe({
          next: () => this.router.navigate(['/roles'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
