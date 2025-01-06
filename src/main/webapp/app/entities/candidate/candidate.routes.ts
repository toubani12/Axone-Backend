import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CandidateComponent } from './list/candidate.component';
import { CandidateDetailComponent } from './detail/candidate-detail.component';
import { CandidateUpdateComponent } from './update/candidate-update.component';
import CandidateResolve from './route/candidate-routing-resolve.service';

const candidateRoute: Routes = [
  {
    path: '',
    component: CandidateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidateDetailComponent,
    resolve: {
      candidate: CandidateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidateUpdateComponent,
    resolve: {
      candidate: CandidateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidateUpdateComponent,
    resolve: {
      candidate: CandidateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default candidateRoute;
