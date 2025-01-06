import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AppTestTypeComponent } from './list/app-test-type.component';
import { AppTestTypeDetailComponent } from './detail/app-test-type-detail.component';
import { AppTestTypeUpdateComponent } from './update/app-test-type-update.component';
import AppTestTypeResolve from './route/app-test-type-routing-resolve.service';

const appTestTypeRoute: Routes = [
  {
    path: '',
    component: AppTestTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppTestTypeDetailComponent,
    resolve: {
      appTestType: AppTestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppTestTypeUpdateComponent,
    resolve: {
      appTestType: AppTestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppTestTypeUpdateComponent,
    resolve: {
      appTestType: AppTestTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appTestTypeRoute;
