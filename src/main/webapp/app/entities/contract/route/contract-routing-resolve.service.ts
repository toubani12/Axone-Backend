import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';

const contractResolve = (route: ActivatedRouteSnapshot): Observable<null | IContract> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContractService)
      .find(id)
      .pipe(
        mergeMap((contract: HttpResponse<IContract>) => {
          if (contract.body) {
            return of(contract.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contractResolve;
