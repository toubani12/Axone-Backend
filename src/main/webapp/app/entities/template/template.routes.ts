import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TemplateComponent } from './list/template.component';
import { TemplateDetailComponent } from './detail/template-detail.component';
import { TemplateUpdateComponent } from './update/template-update.component';
import TemplateResolve from './route/template-routing-resolve.service';

const templateRoute: Routes = [
  {
    path: '',
    component: TemplateComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemplateDetailComponent,
    resolve: {
      template: TemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemplateUpdateComponent,
    resolve: {
      template: TemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemplateUpdateComponent,
    resolve: {
      template: TemplateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default templateRoute;
