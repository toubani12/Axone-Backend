import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICriteria } from '../criteria.model';
import { CriteriaService } from '../service/criteria.service';

const criteriaResolve = (route: ActivatedRouteSnapshot): Observable<null | ICriteria> => {
  const id = route.params['id'];
  if (id) {
    return inject(CriteriaService)
      .find(id)
      .pipe(
        mergeMap((criteria: HttpResponse<ICriteria>) => {
          if (criteria.body) {
            return of(criteria.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default criteriaResolve;
