import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INDA } from '../nda.model';
import { NDAService } from '../service/nda.service';

const nDAResolve = (route: ActivatedRouteSnapshot): Observable<null | INDA> => {
  const id = route.params['id'];
  if (id) {
    return inject(NDAService)
      .find(id)
      .pipe(
        mergeMap((nDA: HttpResponse<INDA>) => {
          if (nDA.body) {
            return of(nDA.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default nDAResolve;
