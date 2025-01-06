import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AppAccountTypeComponent } from './list/app-account-type.component';
import { AppAccountTypeDetailComponent } from './detail/app-account-type-detail.component';
import { AppAccountTypeUpdateComponent } from './update/app-account-type-update.component';
import AppAccountTypeResolve from './route/app-account-type-routing-resolve.service';

const appAccountTypeRoute: Routes = [
  {
    path: '',
    component: AppAccountTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppAccountTypeDetailComponent,
    resolve: {
      appAccountType: AppAccountTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppAccountTypeUpdateComponent,
    resolve: {
      appAccountType: AppAccountTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppAccountTypeUpdateComponent,
    resolve: {
      appAccountType: AppAccountTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appAccountTypeRoute;
