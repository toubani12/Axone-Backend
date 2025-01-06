import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVEmployment } from '../tech-cv-employment.model';
import { TechCVEmploymentService } from '../service/tech-cv-employment.service';

const techCVEmploymentResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVEmployment> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVEmploymentService)
      .find(id)
      .pipe(
        mergeMap((techCVEmployment: HttpResponse<ITechCVEmployment>) => {
          if (techCVEmployment.body) {
            return of(techCVEmployment.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVEmploymentResolve;
