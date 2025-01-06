import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContractTypeComponent } from './list/contract-type.component';
import { ContractTypeDetailComponent } from './detail/contract-type-detail.component';
import { ContractTypeUpdateComponent } from './update/contract-type-update.component';
import ContractTypeResolve from './route/contract-type-routing-resolve.service';

const contractTypeRoute: Routes = [
  {
    path: '',
    component: ContractTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractTypeDetailComponent,
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractTypeUpdateComponent,
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractTypeUpdateComponent,
    resolve: {
      contractType: ContractTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractTypeRoute;
