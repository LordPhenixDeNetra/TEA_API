import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { UserService } from 'app/user/user.service';
import { UserDTO } from 'app/user/user.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-user-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './user-add.component.html'
})
export class UserAddComponent implements OnInit {

  userService = inject(UserService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  rolesValues?: Map<number,string>;

  addForm = new FormGroup({
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
      created: $localize`:@@user.create.success:User was created successfully.`,
      USER_UUID_UNIQUE: $localize`:@@Exists.user.uuid:This Uuid is already taken.`,
      USER_EMAIL_UNIQUE: $localize`:@@Exists.user.email:This Email is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.userService.getRolesValues()
        .subscribe({
          next: (data) => this.rolesValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new UserDTO(this.addForm.value);
    this.userService.createUser(data)
        .subscribe({
          next: () => this.router.navigate(['/users'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
