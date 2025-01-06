import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterview } from '../interview.model';
import { InterviewService } from '../service/interview.service';

const interviewResolve = (route: ActivatedRouteSnapshot): Observable<null | IInterview> => {
  const id = route.params['id'];
  if (id) {
    return inject(InterviewService)
      .find(id)
      .pipe(
        mergeMap((interview: HttpResponse<IInterview>) => {
          if (interview.body) {
            return of(interview.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default interviewResolve;
