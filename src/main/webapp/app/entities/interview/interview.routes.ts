import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InterviewComponent } from './list/interview.component';
import { InterviewDetailComponent } from './detail/interview-detail.component';
import { InterviewUpdateComponent } from './update/interview-update.component';
import InterviewResolve from './route/interview-routing-resolve.service';

const interviewRoute: Routes = [
  {
    path: '',
    component: InterviewComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InterviewDetailComponent,
    resolve: {
      interview: InterviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InterviewUpdateComponent,
    resolve: {
      interview: InterviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InterviewUpdateComponent,
    resolve: {
      interview: InterviewResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default interviewRoute;
