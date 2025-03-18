import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CategorieService } from 'app/categorie/categorie.service';
import { CategorieDTO } from 'app/categorie/categorie.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-categorie-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './categorie-add.component.html'
})
export class CategorieAddComponent {

  categorieService = inject(CategorieService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    categorieName: new FormControl(null, [Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@categorie.create.success:Categorie was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CategorieDTO(this.addForm.value);
    this.categorieService.createCategorie(data)
        .subscribe({
          next: () => this.router.navigate(['/categories'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
