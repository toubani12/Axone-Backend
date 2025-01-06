import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractType } from '../contract-type.model';
import { ContractTypeService } from '../service/contract-type.service';

const contractTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IContractType> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContractTypeService)
      .find(id)
      .pipe(
        mergeMap((contractType: HttpResponse<IContractType>) => {
          if (contractType.body) {
            return of(contractType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contractTypeResolve;
