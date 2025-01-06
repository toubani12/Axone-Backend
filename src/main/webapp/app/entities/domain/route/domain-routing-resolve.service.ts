import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomain } from '../domain.model';
import { DomainService } from '../service/domain.service';

const domainResolve = (route: ActivatedRouteSnapshot): Observable<null | IDomain> => {
  const id = route.params['id'];
  if (id) {
    return inject(DomainService)
      .find(id)
      .pipe(
        mergeMap((domain: HttpResponse<IDomain>) => {
          if (domain.body) {
            return of(domain.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default domainResolve;
