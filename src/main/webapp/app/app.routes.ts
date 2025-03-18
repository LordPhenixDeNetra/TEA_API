import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { UserListComponent } from './user/user-list.component';
import { UserAddComponent } from './user/user-add.component';
import { UserEditComponent } from './user/user-edit.component';
import { RoleListComponent } from './role/role-list.component';
import { RoleAddComponent } from './role/role-add.component';
import { RoleEditComponent } from './role/role-edit.component';
import { ProductListComponent } from './product/product-list.component';
import { ProductAddComponent } from './product/product-add.component';
import { ProductEditComponent } from './product/product-edit.component';
import { CategorieListComponent } from './categorie/categorie-list.component';
import { CategorieAddComponent } from './categorie/categorie-add.component';
import { CategorieEditComponent } from './categorie/categorie-edit.component';
import { OrderListComponent } from './order/order-list.component';
import { OrderAddComponent } from './order/order-add.component';
import { OrderEditComponent } from './order/order-edit.component';
import { OderDetailsListComponent } from './oder-details/oder-details-list.component';
import { OderDetailsAddComponent } from './oder-details/oder-details-add.component';
import { OderDetailsEditComponent } from './oder-details/oder-details-edit.component';
import { ImageProductListComponent } from './image-product/image-product-list.component';
import { ImageProductAddComponent } from './image-product/image-product-add.component';
import { ImageProductEditComponent } from './image-product/image-product-edit.component';
import { ErrorComponent } from './error/error.component';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'users',
    component: UserListComponent,
    title: $localize`:@@user.list.headline:Users`
  },
  {
    path: 'users/add',
    component: UserAddComponent,
    title: $localize`:@@user.add.headline:Add User`
  },
  {
    path: 'users/edit/:id',
    component: UserEditComponent,
    title: $localize`:@@user.edit.headline:Edit User`
  },
  {
    path: 'roles',
    component: RoleListComponent,
    title: $localize`:@@role.list.headline:Roles`
  },
  {
    path: 'roles/add',
    component: RoleAddComponent,
    title: $localize`:@@role.add.headline:Add Role`
  },
  {
    path: 'roles/edit/:id',
    component: RoleEditComponent,
    title: $localize`:@@role.edit.headline:Edit Role`
  },
  {
    path: 'products',
    component: ProductListComponent,
    title: $localize`:@@product.list.headline:Products`
  },
  {
    path: 'products/add',
    component: ProductAddComponent,
    title: $localize`:@@product.add.headline:Add Product`
  },
  {
    path: 'products/edit/:id',
    component: ProductEditComponent,
    title: $localize`:@@product.edit.headline:Edit Product`
  },
  {
    path: 'categories',
    component: CategorieListComponent,
    title: $localize`:@@categorie.list.headline:Categories`
  },
  {
    path: 'categories/add',
    component: CategorieAddComponent,
    title: $localize`:@@categorie.add.headline:Add Categorie`
  },
  {
    path: 'categories/edit/:id',
    component: CategorieEditComponent,
    title: $localize`:@@categorie.edit.headline:Edit Categorie`
  },
  {
    path: 'orders',
    component: OrderListComponent,
    title: $localize`:@@order.list.headline:Orders`
  },
  {
    path: 'orders/add',
    component: OrderAddComponent,
    title: $localize`:@@order.add.headline:Add Order`
  },
  {
    path: 'orders/edit/:id',
    component: OrderEditComponent,
    title: $localize`:@@order.edit.headline:Edit Order`
  },
  {
    path: 'oderDetailss',
    component: OderDetailsListComponent,
    title: $localize`:@@oderDetails.list.headline:Oder Detailses`
  },
  {
    path: 'oderDetailss/add',
    component: OderDetailsAddComponent,
    title: $localize`:@@oderDetails.add.headline:Add Oder Details`
  },
  {
    path: 'oderDetailss/edit/:id',
    component: OderDetailsEditComponent,
    title: $localize`:@@oderDetails.edit.headline:Edit Oder Details`
  },
  {
    path: 'imageProducts',
    component: ImageProductListComponent,
    title: $localize`:@@imageProduct.list.headline:Image Products`
  },
  {
    path: 'imageProducts/add',
    component: ImageProductAddComponent,
    title: $localize`:@@imageProduct.add.headline:Add Image Product`
  },
  {
    path: 'imageProducts/edit/:id',
    component: ImageProductEditComponent,
    title: $localize`:@@imageProduct.edit.headline:Edit Image Product`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
