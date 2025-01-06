import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVDocsComponent } from './list/tech-cv-docs.component';
import { TechCVDocsDetailComponent } from './detail/tech-cv-docs-detail.component';
import { TechCVDocsUpdateComponent } from './update/tech-cv-docs-update.component';
import TechCVDocsResolve from './route/tech-cv-docs-routing-resolve.service';

const techCVDocsRoute: Routes = [
  {
    path: '',
    component: TechCVDocsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVDocsDetailComponent,
    resolve: {
      techCVDocs: TechCVDocsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVDocsUpdateComponent,
    resolve: {
      techCVDocs: TechCVDocsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVDocsUpdateComponent,
    resolve: {
      techCVDocs: TechCVDocsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVDocsRoute;
