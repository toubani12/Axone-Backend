import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecruiter } from '../recruiter.model';
import { RecruiterService } from '../service/recruiter.service';

const recruiterResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecruiter> => {
  const id = route.params['id'];
  if (id) {
    return inject(RecruiterService)
      .find(id)
      .pipe(
        mergeMap((recruiter: HttpResponse<IRecruiter>) => {
          if (recruiter.body) {
            return of(recruiter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default recruiterResolve;
