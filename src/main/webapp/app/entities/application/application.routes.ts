import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ApplicationComponent } from './list/application.component';
import { ApplicationDetailComponent } from './detail/application-detail.component';
import { ApplicationUpdateComponent } from './update/application-update.component';
import ApplicationResolve from './route/application-routing-resolve.service';

const applicationRoute: Routes = [
  {
    path: '',
    component: ApplicationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationDetailComponent,
    resolve: {
      application: ApplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationUpdateComponent,
    resolve: {
      application: ApplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default applicationRoute;
