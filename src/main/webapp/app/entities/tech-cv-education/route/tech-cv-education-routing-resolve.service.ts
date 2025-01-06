import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITechCVEducation } from '../tech-cv-education.model';
import { TechCVEducationService } from '../service/tech-cv-education.service';

const techCVEducationResolve = (route: ActivatedRouteSnapshot): Observable<null | ITechCVEducation> => {
  const id = route.params['id'];
  if (id) {
    return inject(TechCVEducationService)
      .find(id)
      .pipe(
        mergeMap((techCVEducation: HttpResponse<ITechCVEducation>) => {
          if (techCVEducation.body) {
            return of(techCVEducation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default techCVEducationResolve;
