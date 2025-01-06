import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CriteriaComponent } from './list/criteria.component';
import { CriteriaDetailComponent } from './detail/criteria-detail.component';
import { CriteriaUpdateComponent } from './update/criteria-update.component';
import CriteriaResolve from './route/criteria-routing-resolve.service';

const criteriaRoute: Routes = [
  {
    path: '',
    component: CriteriaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CriteriaDetailComponent,
    resolve: {
      criteria: CriteriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CriteriaUpdateComponent,
    resolve: {
      criteria: CriteriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CriteriaUpdateComponent,
    resolve: {
      criteria: CriteriaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default criteriaRoute;
