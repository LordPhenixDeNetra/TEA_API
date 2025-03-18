import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UserService } from 'app/user/user.service';
import { UserDTO } from 'app/user/user.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-user-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './user-edit.component.html'
})
export class UserEditComponent implements OnInit {

  userService = inject(UserService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  rolesValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    uuid: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    firstname: new FormControl(null, [Validators.maxLength(255)]),
    lastname: new FormControl(null, [Validators.maxLength(255)]),
    email: new FormControl(null, [Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    telephone: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    roles: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@user.update.success:User was updated successfully.`,
      USER_UUID_UNIQUE: $localize`:@@Exists.user.uuid:This Uuid is already taken.`,
      USER_EMAIL_UNIQUE: $localize`:@@Exists.user.email:This Email is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.userService.getRolesValues()
        .subscribe({
          next: (data) => this.rolesValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.userService.getUser(this.currentId!)
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
    const data = new UserDTO(this.editForm.value);
    this.userService.updateUser(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/users'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
