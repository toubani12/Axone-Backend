import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DomainComponent } from './list/domain.component';
import { DomainDetailComponent } from './detail/domain-detail.component';
import { DomainUpdateComponent } from './update/domain-update.component';
import DomainResolve from './route/domain-routing-resolve.service';

const domainRoute: Routes = [
  {
    path: '',
    component: DomainComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomainDetailComponent,
    resolve: {
      domain: DomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomainUpdateComponent,
    resolve: {
      domain: DomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomainUpdateComponent,
    resolve: {
      domain: DomainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default domainRoute;
