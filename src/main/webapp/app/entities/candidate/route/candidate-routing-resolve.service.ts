import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidate } from '../candidate.model';
import { CandidateService } from '../service/candidate.service';

const candidateResolve = (route: ActivatedRouteSnapshot): Observable<null | ICandidate> => {
  const id = route.params['id'];
  if (id) {
    return inject(CandidateService)
      .find(id)
      .pipe(
        mergeMap((candidate: HttpResponse<ICandidate>) => {
          if (candidate.body) {
            return of(candidate.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default candidateResolve;
