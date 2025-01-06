import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EmployerComponent } from './list/employer.component';
import { EmployerDetailComponent } from './detail/employer-detail.component';
import { EmployerUpdateComponent } from './update/employer-update.component';
import EmployerResolve from './route/employer-routing-resolve.service';

const employerRoute: Routes = [
  {
    path: '',
    component: EmployerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployerDetailComponent,
    resolve: {
      employer: EmployerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployerUpdateComponent,
    resolve: {
      employer: EmployerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployerUpdateComponent,
    resolve: {
      employer: EmployerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default employerRoute;
