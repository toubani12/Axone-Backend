import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CustomQuestionComponent } from './list/custom-question.component';
import { CustomQuestionDetailComponent } from './detail/custom-question-detail.component';
import { CustomQuestionUpdateComponent } from './update/custom-question-update.component';
import CustomQuestionResolve from './route/custom-question-routing-resolve.service';

const customQuestionRoute: Routes = [
  {
    path: '',
    component: CustomQuestionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomQuestionDetailComponent,
    resolve: {
      customQuestion: CustomQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomQuestionUpdateComponent,
    resolve: {
      customQuestion: CustomQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomQuestionUpdateComponent,
    resolve: {
      customQuestion: CustomQuestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default customQuestionRoute;
