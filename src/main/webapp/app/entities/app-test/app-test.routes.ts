import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AppTestComponent } from './list/app-test.component';
import { AppTestDetailComponent } from './detail/app-test-detail.component';
import { AppTestUpdateComponent } from './update/app-test-update.component';
import AppTestResolve from './route/app-test-routing-resolve.service';

const appTestRoute: Routes = [
  {
    path: '',
    component: AppTestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppTestDetailComponent,
    resolve: {
      appTest: AppTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppTestUpdateComponent,
    resolve: {
      appTest: AppTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppTestUpdateComponent,
    resolve: {
      appTest: AppTestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default appTestRoute;
