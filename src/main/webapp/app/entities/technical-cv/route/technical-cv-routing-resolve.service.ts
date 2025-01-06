import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechnicalCV } from '../technical-cv.model';
import { TechnicalCVService } from '../service/technical-cv.service';

const technicalCVResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechnicalCV> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechnicalCVService)
      .find(id)
      .pipe(
        mergeMap((technicalCV: HttpResponse<ITechnicalCV>) => {
          if (technicalCV.body) {
            return of(technicalCV.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default technicalCVResolve;
