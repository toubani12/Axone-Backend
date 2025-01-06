import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NDAComponent } from './list/nda.component';
import { NDADetailComponent } from './detail/nda-detail.component';
import { NDAUpdateComponent } from './update/nda-update.component';
import NDAResolve from './route/nda-routing-resolve.service';

const nDARoute: Routes = [
  {
    path: '',
    component: NDAComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NDADetailComponent,
    resolve: {
      nDA: NDAResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NDAUpdateComponent,
    resolve: {
      nDA: NDAResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NDAUpdateComponent,
    resolve: {
      nDA: NDAResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default nDARoute;
