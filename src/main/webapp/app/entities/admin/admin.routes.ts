import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AdminComponent } from './list/admin.component';
import { AdminDetailComponent } from './detail/admin-detail.component';
import { AdminUpdateComponent } from './update/admin-update.component';
import AdminResolve from './route/admin-routing-resolve.service';

const adminRoute: Routes = [
  {
    path: '',
    component: AdminComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdminDetailComponent,
    resolve: {
      admin: AdminResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdminUpdateComponent,
    resolve: {
      admin: AdminResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdminUpdateComponent,
    resolve: {
      admin: AdminResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default adminRoute;
