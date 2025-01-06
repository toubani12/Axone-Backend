import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RequestComponent } from './list/request.component';
import { RequestDetailComponent } from './detail/request-detail.component';
import { RequestUpdateComponent } from './update/request-update.component';
import RequestResolve from './route/request-routing-resolve.service';

const requestRoute: Routes = [
  {
    path: '',
    component: RequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequestDetailComponent,
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequestUpdateComponent,
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequestUpdateComponent,
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default requestRoute;
