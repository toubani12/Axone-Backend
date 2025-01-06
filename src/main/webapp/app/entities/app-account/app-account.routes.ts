import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AppAccountComponent } from './list/app-account.component';
import { AppAccountDetailComponent } from './detail/app-account-detail.component';
import { AppAccountUpdateComponent } from './update/app-account-update.component';
import AppAccountResolve from './route/app-account-routing-resolve.service';

const appAccountRoute: Routes = [
  {
    path: '',
    component: AppAccountComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppAccountDetailComponent,
    resolve: {
      appAccount: AppAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppAccountUpdateComponent,
    resolve: {
      appAccount: AppAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppAccountUpdateComponent,
    resolve: {
      appAccount: AppAccountResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appAccountRoute;
